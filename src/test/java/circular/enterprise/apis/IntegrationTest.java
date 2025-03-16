package circular.enterprise.apis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class IntegrationTest {
    private static final String PRIVATE_KEY = System.getenv("CIRCULAR_PRIVATE_KEY") != null ? System.getenv("CIRCULAR_PRIVATE_KEY") : ""; // Replace with your private key
    private static final String ADDRESS = System.getenv("CIRCULAR_ADDRESS") != null ? System.getenv("CIRCULAR_ADDRESS") : ""; // Replace with your wallet address
    private CEPAccount account;

    @BeforeEach
    void setUp() {
        // Skip tests if environment variables are not set
        if (PRIVATE_KEY == null || PRIVATE_KEY.isEmpty() || ADDRESS == null || ADDRESS.isEmpty()) {
            System.out.println("Skipping tests: CIRCULAR_PRIVATE_KEY and CIRCULAR_ADDRESS environment variables must be set");
            return;
        }
        account = new CEPAccount();
    }

    @Test
    void testCircularOperations() throws Exception {
        // Skip test if environment variables are not set
        if (PRIVATE_KEY == null || PRIVATE_KEY.isEmpty() || ADDRESS == null || ADDRESS.isEmpty()) {
            return;
        }

        // Open account
        assertTrue(account.open(ADDRESS));
        
        // Set network to testnet and configure NAG URL
        String networkUrl = account.setNetwork("testnet");
        assertNotNull(networkUrl);
        account.setNagUrl(networkUrl);
        
        // Update account to get current nonce
        assertTrue(account.updateAccount());
        
        // Create a test message and sign it
        String message = "Test message from Java implementation";
        String signature = account.signData(message, PRIVATE_KEY);
        System.out.println("Message signature: " + signature);
        
        // Create and submit a certificate
        String certificateData = String.format(
            "{\"message\":\"%s\",\"signature\":\"%s\",\"timestamp\":%d}",
            message,
            signature,
            System.currentTimeMillis()
        );
        
        account.submitCertificate(certificateData, PRIVATE_KEY);
        
        // Wait for transaction to be processed
        String txId = account.getLatestTxID();
        if (txId != null && !txId.isEmpty()) {
            Map<String, Object> outcome = account.getTransactionOutcome(txId, 30, 2);
            assertEquals(200, outcome.get("Result"));
            
            // Query the transaction
            Map<String, Object> txData = account.getTransactionById(txId, 0, 10);
            assertEquals(200, txData.get("Result"));
        }
    }

    @Test
    void testCertificateOperations() throws Exception {
        // Skip test if environment variables are not set
        if (PRIVATE_KEY == null || PRIVATE_KEY.isEmpty() || ADDRESS == null || ADDRESS.isEmpty()) {
            return;
        }

        // Open account first
        assertTrue(account.open(ADDRESS));
        String networkUrl = account.setNetwork("devnet");
        account.setNagUrl(networkUrl);
        
        // Create and submit a test certificate
        String certificateData = "{\"test\":\"data\",\"timestamp\":\"" + System.currentTimeMillis() + "\"}";
        
        // Test certificate submission
        assertDoesNotThrow(() -> {
            account.submitCertificate(certificateData, PRIVATE_KEY);
        }, "Certificate submission should not throw an exception");
        
        // If a transaction ID is available, test transaction query
        String txId = account.getLatestTxID();
        if (txId != null && !txId.isEmpty()) {
            Map<String, Object> txData = account.getTransactionById(txId, 0, 10);
            assertNotNull(txData, "Transaction data should not be null");
            assertEquals(200, txData.get("Result"), "Transaction query should be successful");
        }
    }

    @Test
    void testHelloWorldCertification() throws Exception {
        // Skip test if environment variables are not set
        if (PRIVATE_KEY == null || PRIVATE_KEY.isEmpty() || ADDRESS == null || ADDRESS.isEmpty()) {
            return;
        }

        // Open account
        assertTrue(account.open(ADDRESS));
        System.out.println("Account opened successfully");
        
        // Set network to testnet and configure NAG URL
        String networkUrl = account.setNetwork("testnet");
        assertNotNull(networkUrl);
        account.setNagUrl(networkUrl);
        System.out.println("Connected to testnet: " + networkUrl);
        
        // Update account to get current nonce
        assertTrue(account.updateAccount());
        System.out.println("Account updated, current nonce: " + account.getNonce());
        
        // Create and sign "Hello World" message
        String message = "Hello World";
        String signature = account.signData(message, PRIVATE_KEY);
        System.out.println("Message signature: " + signature);
        
        // Create and submit the certificate
        String certificateData = String.format(
            "{\"message\":\"%s\",\"signature\":\"%s\",\"timestamp\":%d}",
            message,
            signature,
            System.currentTimeMillis()
        );
        
        System.out.println("Submitting certificate: " + certificateData);
        account.submitCertificate(certificateData, PRIVATE_KEY);
        
        // Get and save the transaction ID with retries
        String txId = null;
        int maxRetries = 5;
        int retryCount = 0;
        while (txId == null || txId.isEmpty()) {
            txId = account.getLatestTxID();
            if (txId == null || txId.isEmpty()) {
                if (retryCount >= maxRetries) {
                    fail("Failed to get transaction ID after " + maxRetries + " attempts");
                }
                System.out.println("Transaction ID not available yet, retrying in 2 seconds...");
                Thread.sleep(2000);
                retryCount++;
            }
        }
        System.out.println("Transaction ID: " + txId);
        
        // Wait for transaction to be processed and get outcome (increased timeout to 120 seconds)
        System.out.println("Waiting for transaction to be processed...");
        Map<String, Object> outcome = account.getTransactionOutcome(txId, 120, 5);
        assertEquals(200, outcome.get("Result"), "Transaction should be successful");
        System.out.println("Transaction processed successfully");
        
        // Get the block number from the transaction response
        Map<String, Object> response = (Map<String, Object>) outcome.get("Response");
        System.out.println("Transaction response: " + response);
        assertNotNull(response, "Transaction response should not be null");

        String blockID = (String) response.get("BlockID");
        assertNotNull(blockID, "Block number should not be null");
        System.out.println("Block number: " + blockID);
        
        // Query the transaction directly using block number
        Map<String, Object> txData = account.getTransaction(blockID, txId);
        assertEquals(200, txData.get("Result"), "Direct transaction query should be successful");
        System.out.println("Transaction verified in block: " + blockID);
        
        // Print full transaction details
        System.out.println("Transaction details: " + txData.get("Response"));

        account.close();
    }
} 
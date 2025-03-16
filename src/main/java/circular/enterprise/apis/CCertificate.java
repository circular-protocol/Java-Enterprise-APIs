package circular.enterprise.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Circular Certificate Class for certificate chaining
 */
public class CCertificate {
    private String data;
    private String previousTxID;
    private String previousBlock;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CCertificate() {
        this.data = null;
        this.previousTxID = null;
        this.previousBlock = null;
    }

    /**
     * Insert application data into the certificate
     * @param data Data content
     */
    public void setData(String data) {
        this.data = Helper.stringToHex(data);
    }

    /**
     * Extract application data from the certificate
     * @return Data content
     */
    public String getData() {
        return Helper.hexToString(this.data);
    }

    /**
     * Get the certificate in JSON format
     * @return JSON-encoded certificate
     * @throws Exception if JSON serialization fails
     */
    public String getJsonCertificate() throws Exception {
        Map<String, Object> certificate = new HashMap<>();
        certificate.put("data", getData());
        certificate.put("previousTxID", previousTxID);
        certificate.put("previousBlock", previousBlock);
        certificate.put("version", Helper.LIB_VERSION);
        
        return objectMapper.writeValueAsString(certificate);
    }

    /**
     * Get the size of the certificate in bytes
     * @return Size of the certificate
     */
    public int getCertificateSize() {
        try {
            return getJsonCertificate().getBytes(StandardCharsets.UTF_8).length;
        } catch (Exception e) {
            return 0;
        }
    }

    // Getters and setters for previousTxID and previousBlock
    public String getPreviousTxID() {
        return previousTxID;
    }

    public void setPreviousTxID(String previousTxID) {
        this.previousTxID = previousTxID;
    }

    public String getPreviousBlock() {
        return previousBlock;
    }

    public void setPreviousBlock(String previousBlock) {
        this.previousBlock = previousBlock;
    }
} 
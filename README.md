# Circular Enterprise APIs - Java Implementation

Official Circular Protocol Enterprise APIs for Data Certification - Java Implementation

## Features

- Account management and blockchain interaction
- Certificate creation and submission
- Transaction tracking and verification
- Secure digital signatures using ECDSA (secp256k1)
- RFC 6979 compliant deterministic signatures

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Dependencies

- OkHttp3 for HTTP requests
- Jackson for JSON processing
- Bouncy Castle for cryptographic operations
- JUnit Jupiter for testing

## Installation

1. Clone the repository
2. Build with Maven:
```bash
mvn clean install
```

## Usage Example



## API Documentation

### CEPAccount Class

Main class for interacting with the Circular blockchain:

- `open(String address)` - Open an account
- `updateAccount()` - Update account data and nonce
- `setNetwork(String network)` - Set blockchain network
- `signData(String message, String privateKeyHex)` - Sign data with private key
- `submitCertificate(String data, String privateKeyHex)` - Submit a certificate
- `getTransactionById(String txId, long start, long end)` - Get transaction details
- `getTransactionOutcome(String txId, int timeoutSec, int intervalSec)` - Poll for transaction outcome

### CCertificate Class

Class for managing certificates:

- `setData(String data)` - Set certificate data
- `getData()` - Get certificate data
- `getJsonCertificate()` - Get certificate in JSON format
- `getCertificateSize()` - Get certificate size in bytes

## Testing

To run the tests, you need to set up the following environment variables:

```bash
export CIRCULAR_PRIVATE_KEY="your_64_character_private_key_here"
export CIRCULAR_ADDRESS="your_wallet_address_here"
```

The private key should be a 64-character (32-byte) hex string, and the address should be a valid Ethereum-style address (40 characters + 0x prefix).

### Running Tests

```bash
mvn clean test
```

## Building

```bash
mvn clean package
```

## License

MIT License - see LICENSE file for details

## Credits

CIRCULAR GLOBAL LEDGERS, INC. - USA

- Original Python Version: Gianluca De Novi, PhD
- Java Implementation: Danny De Novi 

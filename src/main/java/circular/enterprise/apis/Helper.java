package circular.enterprise.apis;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;

/**
 * CIRCULAR Enterprise APIs for Data Certification
 *
 * License : Open Source for private and commercial use
 *
 * CIRCULAR GLOBAL LEDGERS, INC. - USA
 *
 * Version : 1.0.0
 *
 * Creation: 13/3/2025
 * Update  : 13/3/2025
 *
 * Originator: Gianluca De Novi, PhD
 * Contributors: Danny De Novi
 */
public class Helper {
    // Global Constants
    public static final String LIB_VERSION = "1.0.13";
    public static final String NETWORK_URL = "https://circularlabs.io/network/getNAG?network=";
    public static final String DEFAULT_CHAIN = "0x8a20baa40c45dc5055aeb26197c203e576ef389d9acb171bd62da11dc5ad72b2";
    public static final String DEFAULT_NAG = "https://nag.circularlabs.io/NAG.php?cep=";

    /**
     * Function to add a leading zero to numbers less than 10
     * @param num Number to pad
     * @return Padded number
     */
    public static String padNumber(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    /**
     * Function to get the current timestamp in the format YYYY:MM:DD-HH:MM:SS
     * @return Formatted timestamp
     */
    public static String getFormattedTimestamp() {
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd-HH:mm:ss");
        return nowUtc.format(formatter);
    }

    /**
     * Removes '0x' from hexadecimal numbers if they have it
     * @param hexStr Hexadecimal string
     * @return Cleaned hexadecimal string
     */
    public static String hexFix(String hexStr) {
        return hexStr != null ? hexStr.replace("0x", "") : "";
    }

    /**
     * Convert a string to its hexadecimal representation without '0x'
     * @param str Input string
     * @return Hexadecimal representation
     */
    public static String stringToHex(String str) {
        if (str == null) return "";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Convert a hexadecimal string to a regular string
     * @param hexStr Hexadecimal string
     * @return Regular string
     */
    public static String hexToString(String hexStr) {
        if (hexStr == null || hexStr.length() % 2 != 0) return "";
        
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
} 
package com.reelpay.api.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * HMAC utility class for cryptographic operations
 * 
 * This class provides HMAC-based encryption methods for various algorithms
 * including HMAC-SHA1, HMAC-MD5, HMAC-SHA256, and HMAC-SHA512.
 * 
 * @author ReelPay SDK Team
 * @version 1.0.1
 */
public class HmacUtil {
    
    /** HMAC-SHA1 algorithm constant */
    public static final String HMAC_SHA1 = "HmacSHA1";
    
    /** HMAC-MD5 algorithm constant */
    public static final String HMAC_MD5 = "HmacMD5";
    
    /** HMAC-SHA256 algorithm constant */
    public static final String HMAC_SHA256 = "HmacSHA256";
    
    /** HMAC-SHA512 algorithm constant */
    public static final String HMAC_SHA512 = "HmacSHA512";

    /**
     * Encrypt input using HMAC algorithm
     *
     * @param input Input string to encrypt
     * @param key Secret key for encryption
     * @param algorithm HMAC algorithm to use (HMAC_SHA1, HMAC_MD5, HMAC_SHA256, HMAC_SHA512)
     * @return Encrypted hexadecimal string
     * @throws IllegalArgumentException if parameters are invalid
     * @throws RuntimeException if encryption fails
     */
    public static String encrypt(String input, String key, String algorithm) {
        validateParameters(input, key, algorithm);
        
        try {
            // Create secret key from the provided key bytes
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);

            // Initialize MAC with the secret key
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
            
            // Encrypt the input
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = mac.doFinal(inputBytes);
            
            // Convert to hexadecimal string
            return bytesToHexString(encryptedBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unsupported HMAC algorithm: " + algorithm, e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key for HMAC encryption", e);
        } catch (Exception e) {
            throw new RuntimeException("HMAC encryption failed", e);
        }
    }

    /**
     * Convert byte array to hexadecimal string
     *
     * @param bytes Byte array to convert
     * @return Hexadecimal string representation
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Byte array cannot be null");
        }
        
        StringBuilder hexString = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Validate input parameters for encryption
     * 
     * @param input Input string to validate
     * @param key Secret key to validate
     * @param algorithm Algorithm to validate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private static void validateParameters(String input, String key, String algorithm) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (algorithm == null || algorithm.trim().isEmpty()) {
            throw new IllegalArgumentException("Algorithm cannot be null or empty");
        }
        
        // Validate algorithm is supported
        if (!isSupportedAlgorithm(algorithm)) {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm + 
                ". Supported algorithms: " + HMAC_MD5 + ", " + HMAC_SHA1 + ", " + HMAC_SHA256 + ", " + HMAC_SHA512);
        }
    }

    /**
     * Check if the algorithm is supported
     * 
     * @param algorithm Algorithm to check
     * @return true if algorithm is supported, false otherwise
     */
    private static boolean isSupportedAlgorithm(String algorithm) {
        return HMAC_MD5.equals(algorithm) || 
               HMAC_SHA1.equals(algorithm) || 
               HMAC_SHA256.equals(algorithm) || 
               HMAC_SHA512.equals(algorithm);
    }

    /**
     * Test method demonstrating HMAC encryption with different algorithms
     * 
     * This method is for testing purposes and should not be used in production code.
     * 
     * @param args Command line arguments (not used)
     */
    public static void test(String[] args) {
        String testInput = "abc";
        String testKey = "123456";
        
        System.out.println("Testing HMAC encryption with input: '" + testInput + "' and key: '" + testKey + "'");
        System.out.println();
        
        try {
            String md5Result = encrypt(testInput, testKey, HMAC_MD5);
            System.out.println("HMAC-MD5: " + md5Result);

            String sha1Result = encrypt(testInput, testKey, HMAC_SHA1);
            System.out.println("HMAC-SHA1: " + sha1Result);

            String sha256Result = encrypt(testInput, testKey, HMAC_SHA256);
            System.out.println("HMAC-SHA256: " + sha256Result);

            String sha512Result = encrypt(testInput, testKey, HMAC_SHA512);
            System.out.println("HMAC-SHA512: " + sha512Result);
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

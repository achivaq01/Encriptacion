package com.project.encryption;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class HybridFileEncryption {

    public static void main(String[] args) {
        try {
            // Generate or load public and private keys
            PublicKey publicKey = readPublicKey("data/key_public.key");
            PrivateKey privateKey = readPrivateKey("data/key_private.key");

            if (publicKey == null || privateKey == null) {
                // If keys are not found, generate new keys and store them
                KeyPair keyPair = generateKeyPair();
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();
                //System.out.println(publicKey.toString() + "\n" + privateKey.toString());

                storePublicKey("data/key_public.key", publicKey);
                storePrivateKey("data/key_private.key", privateKey);
            }

            // Encrypt a file using a symmetric key, and then encrypt the symmetric key with the public key
            String password = "your_password";
            encryptFile("data/encrypt.txt", "data/encryptedFile.enc", publicKey, password);
            decryptFile("data/encryptedFile.enc", "data/decryptedText.txt", privateKey, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // You can choose a different key size
        return keyPairGenerator.generateKeyPair();
    }

    public static void storePublicKey(String filePath, PublicKey publicKey) throws Exception {
        byte[] encoded = publicKey.getEncoded();
        String encodedBase64 = Base64.getEncoder().encodeToString(encoded);
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" + encodedBase64 + "\n-----END PUBLIC KEY-----";

        Files.write(Path.of(filePath), publicKeyPEM.getBytes(), StandardOpenOption.CREATE);
    }

    public static void storePrivateKey(String filePath, PrivateKey privateKey) throws Exception {
        byte[] encoded = privateKey.getEncoded();
        String encodedBase64 = Base64.getEncoder().encodeToString(encoded);
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" + encodedBase64 + "\n-----END PRIVATE KEY-----";

        Files.write(Path.of(filePath), privateKeyPEM.getBytes(), StandardOpenOption.CREATE);
    }

    public static PublicKey readPublicKey(String filePath) throws Exception {
        try {
            String keyPEM = Files.readString(Path.of(filePath));
            keyPEM = keyPEM.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(keyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            return null; // Return null if the file is not found or keys cannot be read
        }
    }

    public static PrivateKey readPrivateKey(String filePath) throws Exception {
        try {
            String keyPEM = Files.readString(Path.of(filePath));
            keyPEM = keyPEM.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(keyPEM);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            return null; // Return null if the file is not found or keys cannot be read
        }
    }

    public static void encryptFile(String inputFile, String outputFile, PublicKey publicKey, String password) throws Exception {
        // Generate a symmetric key (e.g., AES) from the password
        SecretKey symmetricKey = generateSymmetricKey(password);

        // Create and initialize the Cipher for symmetric encryption
        Cipher symmetricCipher = Cipher.getInstance("AES");
        symmetricCipher.init(Cipher.ENCRYPT_MODE, symmetricKey);

        // Read the input file and encrypt its content with the symmetric key
        byte[] inputBytes = Files.readAllBytes(Path.of(inputFile));
        byte[] encryptedBytes = symmetricCipher.doFinal(inputBytes);

        // Save the encrypted content to the output file
        Files.write(Path.of(outputFile), encryptedBytes, StandardOpenOption.CREATE);

        // Encrypt the symmetric key with the recipient's public RSA key
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedSymmetricKey = rsaCipher.doFinal(symmetricKey.getEncoded());

        // Save the encrypted symmetric key to a separate file
        Files.write(Path.of("encryptedSymmetricKey.enc"), encryptedSymmetricKey, StandardOpenOption.CREATE);
    }

    public static void decryptFile(String inputFile, String outputFile, PrivateKey privateKey, String password) throws Exception {
        // Decrypt the symmetric key using the private RSA key
        byte[] encryptedSymmetricKey = Files.readAllBytes(Path.of("encryptedSymmetricKey.enc"));
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedSymmetricKey = rsaCipher.doFinal(encryptedSymmetricKey);

        // Reconstruct the symmetric key
        SecretKey symmetricKey = new SecretKeySpec(decryptedSymmetricKey, 0, decryptedSymmetricKey.length, "AES");

        // Create and initialize the Cipher for symmetric decryption
        Cipher symmetricCipher = Cipher.getInstance("AES");
        symmetricCipher.init(Cipher.DECRYPT_MODE, symmetricKey);

        // Read the encrypted file and decrypt its content with the symmetric key
        byte[] encryptedBytes = Files.readAllBytes(Path.of(inputFile));
        byte[] decryptedBytes = symmetricCipher.doFinal(encryptedBytes);

        // Save the decrypted content to the output file
        Files.write(Path.of(outputFile), decryptedBytes, StandardOpenOption.CREATE);
    }

    private static SecretKey generateSymmetricKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Use a key derivation function (KDF) to derive a key from the password
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}

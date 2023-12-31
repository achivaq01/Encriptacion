package com.project.Utils;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class UtilsKeyPair {
    public static void generateKeysToFile(String filePath) throws NoSuchAlgorithmException, IOException, PGPException {
        File publicKeyFile = new File(filePath + "/key_public.key");
        File privateKeyFile = new File(filePath + "/key_private.key");

        JcaPGPKeyPair keys = generateKeys();
        PGPPublicKey publicKey = keys.getPublicKey();
        PGPPrivateKey privateKey = keys.getPrivateKey();

        try (FileOutputStream fileOutputStream = new FileOutputStream(publicKeyFile)) {
            fileOutputStream.write(publicKey.getEncoded());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(privateKeyFile)) {

        }
    }

    public static JcaPGPKeyPair generateKeys() throws NoSuchAlgorithmException, PGPException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        return new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keyPairGenerator.generateKeyPair(), Date.from(Instant.ofEpochSecond(System.currentTimeMillis())));
    }
}
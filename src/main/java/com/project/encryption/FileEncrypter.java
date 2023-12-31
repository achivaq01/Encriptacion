package com.project.encryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

import java.io.*;
import java.security.SecureRandom;
import java.security.Security;

public class FileEncrypter {
    OutputStream fileToEncrypt;
    InputStream publicKeyFile;
    InputStream privateKeyFile;

    public FileEncrypter() {
        super();
    }

    public void setFileToEncrypt(OutputStream fileToEncrypt) {
        this.fileToEncrypt = fileToEncrypt;
    }

    public void setPublicKeyFile(InputStream publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public void setPrivateKeyFile(InputStream privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public void encrypt() throws Exception {
        if (fileToEncrypt == null || publicKeyFile == null) {
            throw new Exception();
        }

        InputStream armoredInputStram = PGPUtil.getDecoderStream(publicKeyFile);
        BcKeyFingerprintCalculator fingerprintCalculator = new BcKeyFingerprintCalculator();
        PGPPublicKeyRing publicKeyRing = new PGPPublicKeyRing(armoredInputStram, fingerprintCalculator);

        Security.addProvider(new BouncyCastleProvider());
        PGPEncryptedDataGenerator dataGenerator = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPUtil.SHA1)
                        .setWithIntegrityPacket(true)
                        .setSecureRandom(new SecureRandom())
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME)
        );
        dataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(
                publicKeyRing.getPublicKey()
        ));

        OutputStream cipherOutStream = dataGenerator.open(fileToEncrypt, new byte[2]);
        int data;
        while ((data = publicKeyFile.read()) != -1) {
            cipherOutStream.write(data);
        }

        cipherOutStream.close();
    }

    public void decrypt(File fileToDecrypt) throws IOException {
        InputStream armoredInputStram = PGPUtil.getDecoderStream(privateKeyFile);
        BcKeyFingerprintCalculator fingerprintCalculator = new BcKeyFingerprintCalculator();
    }


}

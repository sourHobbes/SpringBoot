package com.vmware.sdugar.model;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by sourabhdugar on 3/29/16.
 */
//@Configuration
public class PkCrypt {

    final private Cipher pkCipher;
    final private File publicKeyFile;
    final private File privateKeyFile;

    public PkCrypt(String publicKeyFile, String privateKeyFile)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        pkCipher = Cipher.getInstance("RSA");
        this.publicKeyFile = new File(publicKeyFile);
        this.privateKeyFile = new File(privateKeyFile);
    }

    public void saveKey(File out, String aesKey)
            throws IOException, GeneralSecurityException {
        // read public key to be used to encrypt the AES key
        byte[] encodedKey = new byte[(int)publicKeyFile.length()];
        new FileInputStream(publicKeyFile).read(encodedKey);

        // create public key
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk = kf.generatePublic(publicKeySpec);

        // write AES key
        pkCipher.init(Cipher.ENCRYPT_MODE, pk);
        CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), pkCipher);
        os.write(aesKey.getBytes());
        os.close();
    }

    public String loadKey(File in)
            throws GeneralSecurityException, IOException {
        // read private key to be used to decrypt the AES key
        byte[] encodedKey = new byte[(int)privateKeyFile.length()];
        new FileInputStream(privateKeyFile).read(encodedKey);

        //create public key
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pk = kf.generatePrivate(privateKeySpec);

        // read AES key
        pkCipher.init(Cipher.DECRYPT_MODE, pk);
        FileInputStream ifs = new FileInputStream(in);
        byte[] aesKey = new byte[1];
        StringBuilder sb = new StringBuilder();
        CipherInputStream is = new CipherInputStream(ifs, pkCipher);
        while (is.read(aesKey) != -1) {
            sb.append((char)aesKey[0]);
        }
        System.out.println(sb);
        SecretKeySpec aeskeySpec = new SecretKeySpec(aesKey, "AES");
        return new String(sb.toString());
    }
}

package com.vmware.sdugar.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by sourabhdugar on 3/29/16.
 */
@Configuration
public class DsaSigner {

    private final KeyPairGenerator keyGen;
    private final SecureRandom random;
    private final PrivateKey priv;
    private final PublicKey pub;
    File publicKeyFile = new File("/Users/sourabhdugar/java/public.der");
    File privateKeyFile = new File("/Users/sourabhdugar/java/private.der");

    private final static Logger log = LoggerFactory.getLogger(DsaSigner.class);

    public DsaSigner()
            throws NoSuchProviderException, NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        random = SecureRandom.getInstance("SHA1PRNG", "SUN");

        keyGen.initialize(1024, random);

        KeyPair pair = keyGen.generateKeyPair();
        priv = pair.getPrivate();
        pub = pair.getPublic();
    }

    public byte[] sign(String data)
            throws InvalidKeyException, SignatureException,
                   InvalidKeySpecException, NoSuchAlgorithmException,
                   IOException {
        Signature dsa = null;
        try {
            dsa = Signature.getInstance("SHA1withRSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception occured while signing", e);
        }
        byte[] encodedKey = new byte[(int)privateKeyFile.length()];
        new FileInputStream(privateKeyFile).read(encodedKey);

        //create public key
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pk = kf.generatePrivate(privateKeySpec);

        dsa.initSign(pk);
        dsa.update(data.getBytes());
        return dsa.sign();
    }

    public boolean verify(byte[] signature, String data)
            throws SignatureException, InvalidKeyException,
                   InvalidKeySpecException, NoSuchAlgorithmException,
                   IOException {
        Signature dsa = null;
        try {
            dsa = Signature.getInstance("SHA1withRSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception occured while signing", e);
        }
        byte[] encodedKey = new byte[(int)publicKeyFile.length()];
        new FileInputStream(publicKeyFile).read(encodedKey);

        // create public key
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk = kf.generatePublic(publicKeySpec);

        dsa.initVerify(pk);
        dsa.update(data.getBytes());
        return dsa.verify(signature);
    }
}

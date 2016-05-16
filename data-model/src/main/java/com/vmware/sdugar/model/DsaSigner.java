package com.vmware.sdugar.model;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by sourabhdugar on 3/29/16.
 */
@Configuration
public class DsaSigner {

    private final KeyPairGenerator keyGen;
    private final SecureRandom random;
    private final PrivateKey priv;
    private final PublicKey pub;
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    File publicKeyFile = new File("/Users/sourabhdugar/java/keys/domain.crt");
    File privateKeyFile = new File("/Users/sourabhdugar/java/keys/rsaprivkey.der");
    private static final String PROVIDER_NAME = BouncyCastleProvider.PROVIDER_NAME;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final static Logger log = LoggerFactory.getLogger(DsaSigner.class);

    public DsaSigner()
            throws NoSuchProviderException, NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance("RSA", PROVIDER_NAME);
        //random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        random = SecureRandom.getInstanceStrong();

        KeyPair pair = keyGen.generateKeyPair(); // "BC" default key strength is 2048..
        priv = pair.getPrivate();
        pub = pair.getPublic();
    }

    public byte[] sign(String data)
            throws InvalidKeyException, SignatureException,
                   InvalidKeySpecException, NoSuchAlgorithmException,
                   IOException {
        Signature dsa = null;
        try {
            dsa = Signature.getInstance(SIGNATURE_ALGORITHM);
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
            IOException, CertificateException {
        Signature dsa = null;
        try {
            dsa = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception occured while signing", e);
        }
        byte[] encodedKey = new byte[(int)publicKeyFile.length()];
        new FileInputStream(publicKeyFile).read(encodedKey);

        // create public key from public key der
        //X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        //KeyFactory kf = KeyFactory.getInstance("RSA");
        //PublicKey pk = kf.generatePublic(publicKeySpec);

        // public key from certificate file
        FileInputStream certFileStream = new FileInputStream(publicKeyFile);
        Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(certFileStream);
        PublicKey pk = cert.getPublicKey();

        dsa.initVerify(pk);
        dsa.update(data.getBytes());
        return dsa.verify(signature);
    }
}

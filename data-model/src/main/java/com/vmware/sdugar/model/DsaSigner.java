package com.vmware.sdugar.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.security.*;

/**
 * Created by sourabhdugar on 3/29/16.
 */
@Configuration
public class DsaSigner {

    private final KeyPairGenerator keyGen;
    private final SecureRandom random;
    private final PrivateKey priv;
    private final PublicKey pub;

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
            throws InvalidKeyException, SignatureException {
        Signature dsa = null;
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error("Exception occured while signing", e);
        }
        dsa.initSign(priv);
        dsa.update(data.getBytes());
        return dsa.sign();
    }

    public boolean verify(byte[] signature, String data)
            throws SignatureException, InvalidKeyException {
        Signature dsa = null;
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error("Exception occured while signing", e);
        }
        dsa.initVerify(pub);
        dsa.update(data.getBytes());
        return dsa.verify(signature);
    }
}

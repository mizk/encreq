package com.mouyati.encreq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;

@Service
public class RSAServiceImpl implements RSAService {
    private Key privateKey;
    private Key publicKey;
    private static final Logger logger = LoggerFactory.getLogger(RSAServiceImpl.class);

    public void generateKey(int bitSize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(bitSize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException ex) {

        }
    }

    public byte[] encrypt(byte[] bytes, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] buffer = cipher.doFinal(bytes);
            return buffer;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public byte[] decrypt(byte[] bytes, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] buffer = cipher.doFinal(bytes);
            return buffer;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public Key publicKey() {
        return publicKey;
    }

    public Key privateKey() {
        return privateKey;
    }

    public String publicKeyContent() {
        if (publicKey == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String privateKeyContent() {
        if (privateKey == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

}

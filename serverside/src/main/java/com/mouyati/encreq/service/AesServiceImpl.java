package com.mouyati.encreq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

@Service
public class AesServiceImpl implements AesService {
    private static final Logger logger = LoggerFactory.getLogger(AesServiceImpl.class);

    @Override
    public byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Key k = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, k, ivParameterSpec);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Key k = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, k, ivParameterSpec);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

package com.mouyati.encreq.service;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;


public interface RSAService {
    void generateKey(int bitSize);

    Key publicKey();

    Key privateKey();

    String publicKeyContent();

    String privateKeyContent();

    byte[] encrypt(byte[] bytes, Key key);

    byte[] decrypt(byte[] bytes, Key key);
}

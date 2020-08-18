package com.mouyati.encreq.service;

public interface AesService {
    byte[] encrypt(byte[] data,byte[] key,byte[] iv);
    byte[] decrypt(byte[]data,byte[] key,byte[] iv);
}

package com.gtappdevelopers.encryptionanddecryption;

import android.media.MediaRecorder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    public final static int READ_BUFFER = 1024;
    private final static String ALGO_IMG_ENCRYPTOR = "AES/CBC/PKCS5Padding";
    private final static String ALGO_SECRET_KEY = "AES";

    public static void encryptFile(String keystr, String spec, InputStream in, OutputStream out) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        try{
            IvParameterSpec iv = new IvParameterSpec(spec.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(keystr.getBytes("UTF-8"),ALGO_SECRET_KEY);
            Cipher c = Cipher.getInstance(ALGO_IMG_ENCRYPTOR);
            c.init(Cipher.ENCRYPT_MODE,keySpec,iv);
            out = new CipherOutputStream(out,c);
            int count = 0;
            byte[] buffer = new byte[READ_BUFFER];
            while((count = in.read(buffer))>0)
                out.write(buffer,0,count);
        }finally {
            out.close();
        }
    }

    public static void decryptFile(String keystr, String spec, InputStream in, OutputStream out) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        try{
            IvParameterSpec iv = new IvParameterSpec(spec.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(keystr.getBytes("UTF-8"),ALGO_SECRET_KEY);
            Cipher c = Cipher.getInstance(ALGO_IMG_ENCRYPTOR);
            c.init(Cipher.DECRYPT_MODE,keySpec,iv);
            out = new CipherOutputStream(out,c);
            int count = 0;
            byte[] buffer = new byte[READ_BUFFER];
            while((count = in.read(buffer))>0)
                out.write(buffer,0,count);
        }finally {
            out.close();
        }
    }


}

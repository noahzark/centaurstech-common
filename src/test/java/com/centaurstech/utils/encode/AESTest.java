package com.centaurstech.utils.encode;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

public class AESTest {

    public static String SecretKey2String(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey String2SecretKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    @Test
    public void testAESCipher() throws Exception {
        String key = "vTwa+2iRgvFMyH4WuYMx0A==";
        String iv = "5a2wShLw7EWa8Fiw+cWYcQ==";

        String encryptResult = AESCipher.aesEncryptString(
                "abcdefghijklmnopqRSTUVWXYZ1234567890!@#$%^&*()_+~",
                key,
                iv
        );
        System.out.println(encryptResult);

        String decyryptResult = AESCipher.aesDecryptString(
                encryptResult,
                key,
                iv
        );
        System.out.println(decyryptResult);
    }

    @Test
    public void testAESCipherSimple() throws Exception {
        String key = "1234567890123456";

        String encryptResult = AESCipher.aesEncryptString(
                "abcdefghijklmnopqRSTUVWXYZ1234567890!@#$%^&*()_+~",
                key
        );
        System.out.println(encryptResult);

        String decyryptResult = AESCipher.aesDecryptString(
                encryptResult,
                key
        );
        System.out.println(decyryptResult);
    }

    @Test
    public void test128EncodeAndDecode() throws Exception {
        String test = "abcdefghijklmnopqRSTUVWXYZ1234567890!@#$%^&*()_+~";
        String password = UUID.randomUUID().toString().replaceAll("-", "").substring(16);

        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[8];
        rand.nextBytes(salt);

        /* Derive the key, given password and salt. */
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        System.out.println("secret: " + SecretKey2String(secret));

        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal(test.getBytes("UTF-8"));

        String cipherString = new String();
        System.out.println("iv: " + new String(Base64.getEncoder().encodeToString(iv)));
        System.out.println("ciphertext: " + new String(Base64.getEncoder().encode(ciphertext)));

        /* Decrypt the message, given derived key and initialization vector. */
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
        System.out.println(plaintext);
    }

    @Test
    public void test256EncodeAndDecode() throws Exception {
        Security.setProperty("crypto.policy", "unlimited");

        String test = "abcdefghijklmnopqRSTUVWXYZ1234567890!@#$%^&*()_+~";
        String password = UUID.randomUUID().toString().replaceAll("-", "");

        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[8];
        rand.nextBytes(salt);

        /* Derive the key, given password and salt. */
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal(test.getBytes("UTF-8"));

        String cipherString = new String(Base64.getEncoder().encode(ciphertext));
        System.out.println(cipherString);

        /* Decrypt the message, given derived key and initialization vector. */
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
        System.out.println(plaintext);
    }

}

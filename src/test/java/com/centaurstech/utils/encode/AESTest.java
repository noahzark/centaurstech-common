package com.centaurstech.utils.encode;

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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AESTest {

    public static String SecretKey2String(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey String2SecretKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    @Test
    //测试带初始化向量的AES加密算法的编码与解码
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
    //测试带不带初始化向量的AES加密算法的编码与解码
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
    //迭代次数为65535和密钥长度为128的加密与解密测试
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

        System.out.println("iv: " + new String(Base64.getEncoder().encodeToString(iv)));
        System.out.println("ciphertext: " + new String(Base64.getEncoder().encode(ciphertext)));

        /* Decrypt the message, given derived key and initialization vector. */
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
        System.out.println(plaintext);
    }

    @Test
    //密钥长度为256的加密与解密测试
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

    @Test
    //AES加密与AES+Base64编码测试(ECB模式)
    public void testECB256EncodeAndDecode() throws Exception {
        final SecretKeySpec key = new SecretKeySpec(
                Md5.digest("2IBtBXdrqC3kCBs4gaceL7nl2nnFadQv").toLowerCase().getBytes(),
                AESCipher.ALGORITHM);

        String base64Data  = "m4NnwrtY0jhpDgNp65H1V/0OWMtSoTYhhY89MHbflhmnaHq9ZKjx9ABq6Jpg4SccA876HVy7J9P85NpdvCMNGInZ4fANDRE+YfZe4HeF+bbFj6JETcEFPpE9YW+bTbC0D+gl/otScJfvB2QUK7+EeBGPHN1EWX9zbr2Gw6AUaORdFk3mGxV5dtjuwWQrv5juzkXDs33Z2dUMslO+i3j0cTDHqwS4hptx2j6h2HvzgzltFbjo7nysU+4rArqJvrGW/9r18e1St9XgG21NALqixfaSmqetOR4zLVL4/+z3CEz8cg5r+/4GUOTf3XFmLCZ/wEkRQhKRNVibG0NFfiG3KnqArMJ/dheQHCd7qL+XX/ZV6tj8RLMgL7R6hOiR03Ljyikdxq9M3K9CTYgf03pHJd3geXX1LgXrLxc1flL6NW+zD3ZayGYpr1WpLsSMG7z8W5j1pme6cRj3n2+CwSFnOnOkxaFuLKoJAJIqM3gbC0eN++vY73RKphlI4zZqg6o5s3MXI6ju1yoi/ZQ+XbTg2JttsdbU0aySernKwkt0rYMf0j/Mcvo2axgHbI3w/iTm141WxHUjkQ+ga2X1pOWdGifGhSmMP8oGaA/WD5MAsK1qXX0eFvUWS/PTauCSTWq5Cmr8loA/KL3jgvB0nyR4mfccB+tPy4Ny7kzOlr/VNeb0ULf96R0AWFWCtdt8AmujAP0DYiM5FSmYLI0XRhpSDjnEbBM8+isNE1GlAVR3NzzemwQORihScovpAktbRSN/d3N+NgTjSoVDiJvCOxCs3thX9qt9iwYbA+/X/gv8lza2FZyIzwkQxGRcYl8JWKpXzNW8EWUNVnSLdHvQttDeV3CvgP/x579RGd6whyFYS6AaI0qw7oTjCFh2EHS/VzGvFuv166ZlVIJ4MNvg79O9h63ZOSE1LzVqEsVh8fDCfM2GgJ9aUdl95Djgunit4yIZOdoigR3f/BEHKrYCEham11rYohaAXs4XAXWihsV3WD5j4G/P+txvjAwujvf4HDwzHgFsmSml013U2mUiy+v4zw==";

        String result = new String(
                AESCipher.cipherOperationECB(
                        Base64Util.decode(base64Data),
                        key,
                        AESCipher.ALGORITHM_ECB_PADDING,
                        Cipher.DECRYPT_MODE
                ));
        System.out.println(result);

        String encoded = Base64Util.encode(
                AESCipher.cipherOperationECB(
                        result.getBytes(),
                        key,
                        AESCipher.ALGORITHM_ECB_PADDING,
                        Cipher.ENCRYPT_MODE
                ));
        System.out.println(encoded);
        assertThat(base64Data.equals(encoded), is(true));
    }

}

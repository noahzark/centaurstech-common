package com.centaurstech.utils.encode;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by feliciano on 7/2/16.
 */
public class XXTEATest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        String test = "abcdefghijklmnopqRSTUVWXYZ1234567890!@#$%^&*()_+~";
        String pass = "abcdefg1234567890!@#$";

        String encrypted = XXTEA.encryptToBase64String(test, pass);

        System.out.println(encrypted);

        String decrypted = XXTEA.decryptBase64StringToString(encrypted, pass);

        System.out.println(decrypted);

        Assert.assertFalse(pass.equals(decrypted));
    }

}

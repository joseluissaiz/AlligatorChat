package app.services;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoResolver {

  public String encrypt(String password, String key) throws
          NoSuchAlgorithmException, NoSuchPaddingException,
          InvalidKeyException, IllegalBlockSizeException,
          BadPaddingException, UnsupportedEncodingException {
    byte[] KeyData = key.getBytes();
    SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, KS);
    return Base64.getEncoder().
            encodeToString(cipher.doFinal(password.getBytes(StandardCharsets.UTF_8)));

  }

  public String decrypt(String encryptedText, String key)
          throws NoSuchAlgorithmException, NoSuchPaddingException,
          InvalidKeyException, IllegalBlockSizeException,
          BadPaddingException {
    byte[] KeyData = key.getBytes();
    SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
    byte[] ecryptedtexttobytes = Base64.getDecoder().
            decode(encryptedText);
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.DECRYPT_MODE, KS);
    byte[] decrypted = cipher.doFinal(ecryptedtexttobytes);
    return new String(decrypted, StandardCharsets.UTF_8);

  }

}



import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.KeyPair
import sun.misc.BASE64Encoder
import sun.misc.BASE64Decoder
import java.security.interfaces.RSAPublicKey
import java.security.spec.EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.security.KeyFactory

object test {
  def main(args: Array[String]): Unit = {
    
    val mssg = "My Name is Satbeer.My Name is Satbeer."
    val encryptionKey = "MZygpewJsCpRrfOr"
    val keySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    val encryptedBytes = cipher.doFinal(mssg.getBytes("UTF-8"))
    println(new String(encryptedBytes))
    val cipher2 = Cipher.getInstance("AES")
    cipher2.init(Cipher.DECRYPT_MODE, keySpec)
    val plainBytes = cipher2.doFinal(encryptedBytes);
    println(new String(plainBytes))
   
    // RSA encryption
    val keygen =  KeyPairGenerator.getInstance("RSA")
    keygen.initialize(1024)
    val keyPair = keygen.generateKeyPair()
    val keyPair2 = keygen.generateKeyPair()
    val pvtKey = keyPair.getPrivate;
    val pubKey = keyPair.getPublic;
    val encoder = new BASE64Encoder
    val pubKeyStr = encoder.encode(pubKey.getEncoded)
    println("Public key:" + pubKeyStr)
    val decoder = new BASE64Decoder
    val pubKeyDecoded = decoder.decodeBuffer(pubKeyStr)
    val encodedKeySpec = new X509EncodedKeySpec(pubKeyDecoded)
    val keyFactory = KeyFactory.getInstance("RSA");
    val newPubKey = keyFactory.generatePublic(encodedKeySpec)
    val cipherRsa = Cipher.getInstance("RSA")
    cipherRsa.init(Cipher.ENCRYPT_MODE, newPubKey)
    val rsaText = cipherRsa.doFinal(mssg.getBytes("UTF-8"))
    println("RSA Encrypted text:" + new String(rsaText))
    val cipherRsa2 = Cipher.getInstance("RSA")
    cipherRsa2.init(Cipher.DECRYPT_MODE, pvtKey)
    val rsaDecryptedText = cipherRsa2.doFinal(rsaText)
    println("RSA decrypted text:" + new String(rsaDecryptedText))
    
  }
}
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/** Demonstrates authenticated encryption using standard JCE, not a complete wire protocol. @author Snehanjan Chatterjee */
public final class CoreAuthenticatedEncryption {
    /** Encrypts/decrypts with a caller-owned key and one correctly sized nonce. */
    private static byte[] transform(final int mode, final SecretKey key, final byte[] nonce, final byte[] input)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(mode, key, new GCMParameterSpec(128, nonce));
        return cipher.doFinal(input);
    }
    /** Uses a fresh ephemeral key/nonce and demonstrates rejection of modified ciphertext. */
    public static void main(final String[] args) throws GeneralSecurityException {
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        final SecretKey key = generator.generateKey();
        final byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);
        final byte[] ciphertext = transform(Cipher.ENCRYPT_MODE, key, nonce, "message".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(transform(Cipher.DECRYPT_MODE, key, nonce, ciphertext), StandardCharsets.UTF_8));
        ciphertext[ciphertext.length - 1] ^= 1;
        try { transform(Cipher.DECRYPT_MODE, key, nonce, ciphertext); throw new IllegalStateException("tampered input accepted"); }
        catch (final AEADBadTagException expected) { System.out.println("tamper rejected"); }
    }
}

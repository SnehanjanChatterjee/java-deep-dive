import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

/** Signs a message with an ephemeral key and verifies integrity with the public key. @author Snehanjan Chatterjee */
public final class SignatureLesson {
    /** Demonstrates valid and changed-message verification without printing key material. */
    public static void main(final String[] args) throws GeneralSecurityException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        final KeyPair keys = generator.generateKeyPair();
        final byte[] message = "message".getBytes(StandardCharsets.UTF_8);
        final Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(keys.getPrivate()); signer.update(message);
        final byte[] signature = signer.sign();
        final Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(keys.getPublic()); verifier.update(message);
        System.out.println(verifier.verify(signature));
        verifier.initVerify(keys.getPublic()); verifier.update("changed".getBytes(StandardCharsets.UTF_8));
        System.out.println(verifier.verify(signature));
    }
}

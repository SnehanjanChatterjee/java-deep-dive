package base;

/** Declares members at different source-access boundaries. */
public class Parent {
    protected final int value = 2;
    final int packageValue = 3;
    private final int secret = 4;

    /** Allows subclass construction without publishing an ordinary public constructor. */
    protected Parent() { }

    /** Publishes a selected private value through a deliberate operation. */
    public final int secretValue() { return secret; }
}

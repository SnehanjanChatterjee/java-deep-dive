package client;

import base.PackagePeer;
import base.Parent;
import java.util.Objects;

/** Demonstrates a protected receiver restriction across a package boundary. */
public final class Child extends Parent {
    /** Reads inherited protected state through this subclass's receiver type. */
    public int read(final Child other) { return Objects.requireNonNull(other, "other").value; }

    /** Compares legal subclass and declaring-package accesses. */
    public static void main(final String[] args) {
        final Child child = new Child();
        System.out.println(child.read(new Child()));
        System.out.println(PackagePeer.read(child));
        System.out.println(child.secretValue());
    }
}

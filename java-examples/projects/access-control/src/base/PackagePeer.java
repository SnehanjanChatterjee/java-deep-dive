package base;

import java.util.Objects;

/** Uses the access shared by every class in the declaring package. */
public final class PackagePeer {
    /** Reads package and protected members through an arbitrary Parent reference. */
    public static int read(final Parent parent) {
        Objects.requireNonNull(parent, "parent");
        return parent.value + parent.packageValue;
    }
}

package study.client;

import study.api.Greeter;

/** Is compiled once against v1, then launched against different library binaries. @author Snehanjan Chatterjee */
public final class Main {
    /** Makes source compatibility and binary linkage independently observable. */
    public static void main(final String[] args) { System.out.println(new Greeter().greet("Mira")); }
}

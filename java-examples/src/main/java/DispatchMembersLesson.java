/** Separates virtual method selection from field and static-member selection. */
public final class DispatchMembersLesson {
    static class Parent {
        final String label = "parent field";
        /** Describes the declaring type. */
        static String kind() { return "parent static"; }
        /** Supplies overridable behavior. */
        String describe() { return "parent method"; }
    }
    static final class Child extends Parent {
        final String label = "child field";
        /** Hides Parent.kind; it does not override it. */
        static String kind() { return "child static"; }
        /** Supplies the child's virtual implementation. */
        @Override String describe() { return "child method"; }
        /** Explicitly invokes the inherited implementation. */
        String parentDescription() { return super.describe(); }
    }
    /** Observes one object through two declared types. */
    public static void main(final String[] args) {
        final Child child = new Child();
        final Parent parent = child;
        System.out.println(parent.label);
        System.out.println(child.label);
        System.out.println(Parent.kind());
        System.out.println(Child.kind());
        System.out.println(parent.describe());
        System.out.println(child.parentDescription());
        System.out.println(parent == child);
    }
}

/** Implements a bounded trial-division interview exercise without array or collection helpers. */
public final class PrimeTrialDivisionLesson {
    /** Returns whether number is prime; all values below two are non-prime. */
    public static boolean isPrime(final int number) {
        if (number < 2) { return false; }
        for (int divisor = 2; divisor <= number / divisor; divisor++) {
            if (number % divisor == 0) { return false; }
        }
        return true;
    }
    /** Shows composite, smallest-prime and prime inputs. */
    public static void main(final String[] args) {
        System.out.println(isPrime(1));
        System.out.println(isPrime(2));
        System.out.println(isPrime(49));
        System.out.println(isPrime(97));
    }
}

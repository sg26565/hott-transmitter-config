import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PrimeTest {
    public static void main(final String[] args) {
        final long startTime = System.currentTimeMillis();
        final List<Long> primes = new ArrayList<>();
        primes.add(2l);

        Stream.iterate(3L, number -> number + 2).filter(number -> {
            final long limit = (long) Math.sqrt(number);
            return primes.stream().filter(prime -> prime <= limit).noneMatch(prime -> number % prime == 0);
        }).map(prime -> {
            primes.add(prime);
            return prime;
        }).anyMatch(prime -> prime > 999999l);

        final long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        final int primeCount = primes.size();
        final long lastPrime = primes.get(primeCount - 1);

        System.out.printf("%d, %d, %d, %d%n", primeCount, lastPrime, elapsedTime, primeCount / elapsedTime);
    }
}

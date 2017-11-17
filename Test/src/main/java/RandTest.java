public class RandTest {
    private static long seed;

    public static void main(final String[] args) {
        seed = 1;

        for (int i = 0; i < 100; i++)
            System.out.printf("%2d: %4d%n", i, rand() & 0xFF);
    }

    private static int rand() {
        return (int) ((seed = seed * 214013L + 2531011L) >> 16 & 0x7fff);
    }
}

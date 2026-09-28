import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Benchmark {

    private static final int WARM_UP_RUNS = 300;
    private static final int TIMED_RUNS = 5;

    public static BenchmarkResult measure(
            List<Session> input,
            Function<List<Session>, Result> algorithm
    ) {

        List<Session> sortedInput = new ArrayList<>(input);

        sortedInput.sort(
                Comparator.comparingInt(Session::getStart)
        );

        /*
         * Warm-up.
         */
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            algorithm.apply(sortedInput);
        }

        /*
         * Five timed runs.
         */
        double[] times = new double[TIMED_RUNS];

        Result finalResult = null;

        for (int i = 0; i < TIMED_RUNS; i++) {

            long startTime = System.nanoTime();

            finalResult = algorithm.apply(sortedInput);

            long endTime = System.nanoTime();

            times[i] =
                    (endTime - startTime) / 1_000_000.0;
        }

        double median = median(times);

        return new BenchmarkResult(
                input.size(),
                finalResult.getRoomCount(),
                finalResult.getOperations(),
                median
        );
    }

    private static double median(double[] values) {

        double[] copy = values.clone();

        java.util.Arrays.sort(copy);

        if (copy.length % 2 == 1) {
            return copy[copy.length / 2];
        }

        int middle = copy.length / 2;

        return (copy[middle - 1] + copy[middle]) / 2.0;
    }
}
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class ExperimentRunner {

    private static final String CSV_HEADER =
            "n,family,rooms,maxOverlap,heapOperations,heapMs,"
                    + "listOperations,listMs,optimal";

    public static void runExperiment(
            String name,
            List<Session> sessions,
            Function<List<Session>, Result> heapAlgorithm,
            Function<List<Session>, Result> listAlgorithm
    ) {

        BenchmarkResult heapResult =
                Benchmark.measure(
                        sessions,
                        heapAlgorithm
                );

        BenchmarkResult listResult =
                Benchmark.measure(
                        sessions,
                        listAlgorithm
                );

         /* Shared work, timed separately. Both allocators start by copying the input and sorting it by start time. This block times only that copy-and-sort, so the report can say how
         * much of each measured time is common to both methods.*/
        
        BenchmarkResult sharedWork =
                Benchmark.measure(
                        sessions,
                        input -> {
                            List<Session> copy = new ArrayList<>(input);
                            copy.sort(
                                    Comparator.comparingInt(Session::getStart)
                            );
                            return new Result(
                                    copy.size(),
                                    new ArrayList<>(),
                                    0
                            );
                        }
                );

        int lowerBound =
                OptimalityChecker.maximumOverlap(sessions);

        System.out.println();
        System.out.println("========== " + name + " ==========");

        System.out.println("Sessions: " + sessions.size());

        System.out.println(
                "Maximum simultaneous sessions: "
                        + lowerBound
        );

        System.out.println();
        System.out.println("Heap version");
        System.out.println(
                "Rooms: "
                        + heapResult.getRooms()
        );
        System.out.println(
                "Operations: "
                        + heapResult.getOperations()
        );
        System.out.printf(
                Locale.ROOT,
                "Median time: %.4f ms%n",
                heapResult.getMedianMilliseconds()
        );

        System.out.println();
        System.out.println("List version");
        System.out.println(
                "Rooms: "
                        + listResult.getRooms()
        );
        System.out.println(
                "Operations: "
                        + listResult.getOperations()
        );
        System.out.printf(
                Locale.ROOT,
                "Median time: %.4f ms%n",
                listResult.getMedianMilliseconds()
        );

        System.out.println();
        System.out.printf(
                Locale.ROOT,
                "Shared work (copy + sort of the input), median: %.4f ms%n",
                sharedWork.getMedianMilliseconds()
        );

        System.out.println();
        System.out.println(
                "Heap optimal: "
                        + (heapResult.getRooms() == lowerBound)
        );

        System.out.println(
                "List optimal: "
                        + (listResult.getRooms() == lowerBound)
        );

        System.out.println(
                "Same room count: "
                        + (heapResult.getRooms()
                        == listResult.getRooms())
        );
    }

    public static void runScalingExperiment() {

        int[] sizes = {
                100,
                500,
                1000,
                2000,
                3000,
                4000,
                5000
        };

        int fixedRoomCount = 8;

        new File("results").mkdirs();

        System.out.println();
        System.out.println("SCALING EXPERIMENT");
        System.out.println(CSV_HEADER);

        try (PrintWriter csvWriter =
                     new PrintWriter(
                             new FileWriter("results/scaling_results.csv")
                     )) {

            csvWriter.println(CSV_HEADER);

            for (int n : sizes) {

                scalingRow(
                        n,
                        "growing",
                        DataGenerator.generateGrowingRooms(n),
                        csvWriter
                );

                scalingRow(
                        n,
                        "fixed",
                        DataGenerator.generateFixedRooms(n, fixedRoomCount),
                        csvWriter
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not write results/scaling_results.csv: "
                            + e.getMessage()
            );
        }

        System.out.println();
        System.out.println(
                "Scaling results written to results/scaling_results.csv"
        );
    }

    /*
     * Runs both allocators on one generated instance, checks that
     * both room counts equal the independently computed maximum
     * overlap (the lower bound), then prints and saves one CSV row.
     */
    private static void scalingRow(
            int n,
            String family,
            List<Session> sessions,
            PrintWriter csvWriter
    ) {

        BenchmarkResult heap =
                Benchmark.measure(
                        sessions,
                        HeapRoomAllocator::allocate
                );

        BenchmarkResult list =
                Benchmark.measure(
                        sessions,
                        ListRoomAllocator::allocate
                );

        int lowerBound =
                OptimalityChecker.maximumOverlap(sessions);

        boolean optimal =
                heap.getRooms() == lowerBound
                        && list.getRooms() == lowerBound;

        String row = String.format(
                Locale.ROOT,
                "%d,%s,%d,%d,%d,%.4f,%d,%.4f,%b",
                n,
                family,
                heap.getRooms(),
                lowerBound,
                heap.getOperations(),
                heap.getMedianMilliseconds(),
                list.getOperations(),
                list.getMedianMilliseconds(),
                optimal
        );

        System.out.println(row);
        csvWriter.println(row);
    }
}
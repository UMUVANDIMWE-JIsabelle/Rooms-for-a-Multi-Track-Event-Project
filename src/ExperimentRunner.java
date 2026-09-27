import java.util.List;
import java.util.function.Function;

public class ExperimentRunner {

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
                "Median time: %.4f ms%n",
                listResult.getMedianMilliseconds()
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

        System.out.println();
        System.out.println("SCALING EXPERIMENT");
        System.out.println(
                "n, family, rooms, heapOperations, heapMs, "
                        + "listOperations, listMs"
        );

        for (int n : sizes) {

            List<Session> growing =
                    DataGenerator.generateGrowingRooms(n);

            BenchmarkResult heapGrowing =
                    Benchmark.measure(
                            growing,
                            HeapRoomAllocator::allocate
                    );

            BenchmarkResult listGrowing =
                    Benchmark.measure(
                            growing,
                            ListRoomAllocator::allocate
                    );

            System.out.printf(
                    "%d,growing,%d,%d,%.4f,%d,%.4f%n",
                    n,
                    heapGrowing.getRooms(),
                    heapGrowing.getOperations(),
                    heapGrowing.getMedianMilliseconds(),
                    listGrowing.getOperations(),
                    listGrowing.getMedianMilliseconds()
            );

            List<Session> fixed =
                    DataGenerator.generateFixedRooms(
                            n,
                            8
                    );

            BenchmarkResult heapFixed =
                    Benchmark.measure(
                            fixed,
                            HeapRoomAllocator::allocate
                    );

            BenchmarkResult listFixed =
                    Benchmark.measure(
                            fixed,
                            ListRoomAllocator::allocate
                    );

            System.out.printf(
                    "%d,fixed,%d,%d,%.4f,%d,%.4f%n",
                    n,
                    heapFixed.getRooms(),
                    heapFixed.getOperations(),
                    heapFixed.getMedianMilliseconds(),
                    listFixed.getOperations(),
                    listFixed.getMedianMilliseconds()
            );
        }
    }
}
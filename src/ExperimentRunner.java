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
}
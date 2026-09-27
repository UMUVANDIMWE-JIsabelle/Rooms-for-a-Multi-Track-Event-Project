public class BenchmarkResult {

    private final int n;
    private final int rooms;
    private final long operations;
    private final double medianMilliseconds;

    public BenchmarkResult(
            int n,
            int rooms,
            long operations,
            double medianMilliseconds
    ) {
        this.n = n;
        this.rooms = rooms;
        this.operations = operations;
        this.medianMilliseconds = medianMilliseconds;
    }

    public int getN() {
        return n;
    }

    public int getRooms() {
        return rooms;
    }

    public long getOperations() {
        return operations;
    }

    public double getMedianMilliseconds() {
        return medianMilliseconds;
    }
}
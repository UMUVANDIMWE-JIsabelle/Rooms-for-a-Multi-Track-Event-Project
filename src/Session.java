public class Session {
    private final String id;
    private final int start;
    private final int end;

    public Session(String id, int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException(
                    "End time must be after start time."
            );
        }

        this.id = id;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return id + " (" + start + "-" + end + ")";
    }
}
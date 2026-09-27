import java.util.List;

public class Result {

    private final int roomCount;
    private final List<RoomAssignment> assignments;
    private final long operations;

    public Result(
            int roomCount,
            List<RoomAssignment> assignments,
            long operations
    ) {
        this.roomCount = roomCount;
        this.assignments = assignments;
        this.operations = operations;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public List<RoomAssignment> getAssignments() {
        return assignments;
    }

    public long getOperations() {
        return operations;
    }
}
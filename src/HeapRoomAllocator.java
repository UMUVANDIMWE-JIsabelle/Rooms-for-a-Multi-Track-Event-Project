import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class HeapRoomAllocator {

    public static Result allocate(List<Session> input) {

        List<Session> sessions = new ArrayList<>(input);

        sessions.sort(
                Comparator.comparingInt(Session::getStart)
        );

       
         /* Operation counter. It counts every comparison of two finish times that the algorithm performs:
         *   1. each comparison made INSIDE the PriorityQueue while it restores the heap order (counted in the comparator);
         *   2. the one check per session of the earliest-free room's finish time against the session's start time.
         * The list version counts the same kind of step: one comparison of a room's finish time with the session's start.*/
        
        long[] operations = {0};

        PriorityQueue<Room> rooms = new PriorityQueue<>(
                (a, b) -> {
                    operations[0]++;

                    int byFinishTime = Integer.compare(
                            a.getAvailableAt(),
                            b.getAvailableAt()
                    );

                    if (byFinishTime != 0) {
                        return byFinishTime;
                    }

                    return Integer.compare(
                            a.getRoomNumber(),
                            b.getRoomNumber()
                    );
                }
        );

        List<RoomAssignment> assignments = new ArrayList<>();

        for (Session session : sessions) {

            if (!rooms.isEmpty()) {

                Room earliestRoom = rooms.peek();

                operations[0]++;

                if (earliestRoom.getAvailableAt() <= session.getStart()) {

                    rooms.poll();

                    earliestRoom.setAvailableAt(session.getEnd());

                    rooms.add(earliestRoom);

                    assignments.add(
                            new RoomAssignment(
                                    session,
                                    earliestRoom.getRoomNumber()
                            )
                    );

                    continue;
                }
            }

            int newRoomNumber = rooms.size() + 1;

            Room newRoom =
                    new Room(newRoomNumber, session.getEnd());

            rooms.add(newRoom);

            assignments.add(
                    new RoomAssignment(
                            session,
                            newRoomNumber
                    )
            );
        }

        return new Result(
                rooms.size(),
                assignments,
                operations[0]
        );
    }
}
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

        PriorityQueue<Room> rooms = new PriorityQueue<>(
                Comparator.comparingInt(Room::getAvailableAt)
                        .thenComparingInt(Room::getRoomNumber)
        );

        List<RoomAssignment> assignments = new ArrayList<>();

        long operations = 0;

        for (Session session : sessions) {

            operations++;

            if (!rooms.isEmpty()) {

                Room earliestRoom = rooms.peek();

                operations++;

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
                operations
        );
    }
}
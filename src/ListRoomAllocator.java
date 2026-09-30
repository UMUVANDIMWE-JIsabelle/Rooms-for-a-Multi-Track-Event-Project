
import java.util.ArrayList;
import java.util.List;

public class ListRoomAllocator {

    public static Result allocate(List<Session> input) {

        List<Session> sessions = input;

        List<Room> rooms = new ArrayList<>();
        List<RoomAssignment> assignments = new ArrayList<>();

        long operations = 0;

        for (Session session : sessions) {

            boolean assigned = false;

            for (Room room : rooms) {

                operations++;

                if (room.getAvailableAt() <= session.getStart()) {

                    room.setAvailableAt(session.getEnd());

                    assignments.add(
                            new RoomAssignment(
                                    session,
                                    room.getRoomNumber()
                            )
                    );

                    assigned = true;
                    break;
                }
            }

            if (!assigned) {

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
        }

        return new Result(
                rooms.size(),
                assignments,
                operations
        );
    }
}
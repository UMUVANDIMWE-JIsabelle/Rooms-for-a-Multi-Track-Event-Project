public class RoomAssignment {
    private final Session session;
    private final int roomNumber;

    public RoomAssignment(Session session, int roomNumber) {
        this.session = session;
        this.roomNumber = roomNumber;
    }

    public Session getSession() {
        return session;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return session.getId() + " -> Room " + roomNumber;
    }
}
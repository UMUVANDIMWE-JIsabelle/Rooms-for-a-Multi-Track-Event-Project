public class Room {
    private final int roomNumber;
    private int availableAt;

    public Room(int roomNumber, int availableAt) {
        this.roomNumber = roomNumber;
        this.availableAt = availableAt;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getAvailableAt() {
        return availableAt;
    }

    public void setAvailableAt(int availableAt) {
        this.availableAt = availableAt;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " available at " + availableAt;
    }
}

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

            /*
             * ==========================================
             * 1. SELF-CHECK
             * ==========================================
             */

            String selfCheckPath =
                    "data/Q1_sessions_selfcheck.csv";

            List<Session> selfCheck =
                    CsvSessionReader.read(selfCheckPath);

            Result selfCheckHeap =
                    HeapRoomAllocator.allocate(selfCheck);

            Result selfCheckList =
                    ListRoomAllocator.allocate(selfCheck);

            System.out.println("SELF-CHECK");
            System.out.println(
                    "Heap rooms: "
                            + selfCheckHeap.getRoomCount()
            );

            System.out.println(
                    "List rooms: "
                            + selfCheckList.getRoomCount()
            );

            /*
             * The brief says the self-check should need 3 rooms.
             */
            if (selfCheckHeap.getRoomCount() == 3) {
                System.out.println(
                        "Self-check passed."
                );
            } else {
                System.out.println(
                        "Self-check FAILED."
                );
            }

            /*
             * ==========================================
             * 2. MAIN DATA
             * ==========================================
             */

            String mainPath =
                    "data/Q1_sessions_main.csv";

            List<Session> mainSessions =
                    CsvSessionReader.read(mainPath);

            Result mainHeap =
                    HeapRoomAllocator.allocate(mainSessions);

            Result mainList =
                    ListRoomAllocator.allocate(mainSessions);

            int lowerBound =
                    OptimalityChecker.maximumOverlap(
                            mainSessions
                    );

            System.out.println();
            System.out.println("MAIN DATA");
            System.out.println(
                    "Sessions: "
                            + mainSessions.size()
            );

            System.out.println(
                    "Heap rooms: "
                            + mainHeap.getRoomCount()
            );

            System.out.println(
                    "List rooms: "
                            + mainList.getRoomCount()
            );

            System.out.println(
                    "Maximum overlap: "
                            + lowerBound
            );

            System.out.println(
                    "Heap is optimal: "
                            + (mainHeap.getRoomCount()
                            == lowerBound)
            );

            System.out.println(
                    "List is optimal: "
                            + (mainList.getRoomCount()
                            == lowerBound)
            );

            /*
             * ==========================================
             * 3. PRINT ROOM ASSIGNMENTS
             * ==========================================
             */

            System.out.println();
            System.out.println("HEAP ROOM ASSIGNMENTS");

            for (RoomAssignment assignment
                    : mainHeap.getAssignments()) {

                System.out.println(assignment);
            }

            /*
             * The two versions use the same number of rooms, but the
             * heap picks the room that becomes free EARLIEST while the
             * list picks the FIRST free room it meets. Count how many
             * sessions therefore end up in a different room.
             */
            List<RoomAssignment> heapAssignments =
                    mainHeap.getAssignments();

            List<RoomAssignment> listAssignments =
                    mainList.getAssignments();

            int differentRoom = 0;

            for (int i = 0; i < heapAssignments.size(); i++) {

                if (heapAssignments.get(i).getRoomNumber()
                        != listAssignments.get(i).getRoomNumber()) {

                    differentRoom++;
                }
            }

            System.out.println();
            System.out.println(
                    "Sessions given a different room by the list version: "
                            + differentRoom
                            + " of "
                            + heapAssignments.size()
            );

            /*
             * ==========================================
             * 4. GENERATED INPUTS
             * ==========================================
             */

            List<Session> growing =
                    DataGenerator.generateGrowingRooms(
                            5000
                    );

            List<Session> fixed =
                    DataGenerator.generateFixedRooms(
                            5000,
                            8
                    );

            /*
             * ==========================================
             * 5. BENCHMARKS
             * ==========================================
             */

            ExperimentRunner.runExperiment(
                    "Main dataset - 40 sessions",
                    mainSessions,
                    HeapRoomAllocator::allocate,
                    ListRoomAllocator::allocate
            );

            ExperimentRunner.runExperiment(
                    "Growing Rooms - 5000 sessions",
                    growing,
                    HeapRoomAllocator::allocate,
                    ListRoomAllocator::allocate
            );

            ExperimentRunner.runExperiment(
                    "Fixed Rooms - 5000 sessions",
                    fixed,
                    HeapRoomAllocator::allocate,
                    ListRoomAllocator::allocate
            );

            // Run scaling experiments for different input sizes
            ExperimentRunner.runScalingExperiment();

        } catch (IOException e) {

            System.out.println(
                    "Could not read CSV file: "
                            + e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                    "Program error: "
                            + e.getMessage()
            );
        }
    }
}
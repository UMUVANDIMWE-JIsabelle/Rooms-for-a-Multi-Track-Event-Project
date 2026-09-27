import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    /* Family 1:All sessions overlap.Therefore, n sessions require n rooms. */

    public static List<Session> generateGrowingRooms(int n) {

        List<Session> sessions = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            int start = 9 * 60;

            int end = start + 60;

            sessions.add(
                    new Session(
                            "G" + (i + 1),
                            start,
                            end
                    )
            );
        }

        return sessions;
    }

    /* Family 2: A fixed number of rooms is used repeatedly.Eight sessions run at the same time,
     * then the next group of eight runs later. Therefore, the number of rooms stays at eight while n increases. */

    public static List<Session> generateFixedRooms(
            int n,
            int roomCount
    ) {

        List<Session> sessions = new ArrayList<>();

        int sessionLength = 30;

        for (int i = 0; i < n; i++) {

            int group = i / roomCount;

            int roomPosition = i % roomCount;

            int start =
                    9 * 60
                            + group * sessionLength;

            int end =
                    start + sessionLength;

            sessions.add(
                    new Session(
                            "F" + (i + 1),
                            start,
                            end
                    )
            );
        }

        return sessions;
    }
}
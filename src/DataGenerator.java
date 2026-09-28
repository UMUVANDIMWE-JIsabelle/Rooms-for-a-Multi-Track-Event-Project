import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

   
     /* Family 1: the number of rooms grows with n. Every session runs from 09:00 to 10:00, so all n sessions overlap at the same moment. The maximum overlap is n, so n rooms are needed: r = n.
     */
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

    /*
     * Family 2: the number of rooms stays fixed however large n is. Sessions come in groups of roomCount. All sessions in a group start together and last 30 minutes; the next group starts at the
     * exact moment the previous group ends (which the problem treats as not overlapping). At most roomCount sessions ever run at once, so r = roomCount for every n >= roomCount.
     */
    public static List<Session> generateFixedRooms(
            int n,
            int roomCount
    ) {

        List<Session> sessions = new ArrayList<>();

        int sessionLength = 30;

        for (int i = 0; i < n; i++) {

            int group = i / roomCount;

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
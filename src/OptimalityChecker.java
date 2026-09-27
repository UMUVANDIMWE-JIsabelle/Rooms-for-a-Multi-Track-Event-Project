import java.util.ArrayList;
import java.util.List;

public class OptimalityChecker {

    public static int maximumOverlap(List<Session> sessions) {

        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();

        for (Session session : sessions) {
            starts.add(session.getStart());
            ends.add(session.getEnd());
        }

        starts.sort(Integer::compareTo);
        ends.sort(Integer::compareTo);

        int startPointer = 0;
        int endPointer = 0;

        int current = 0;
        int maximum = 0;

        while (startPointer < starts.size()) {

            if (ends.get(endPointer) <= starts.get(startPointer)) {
                current--;
                endPointer++;
            } else {
                current++;
                startPointer++;

                maximum = Math.max(maximum, current);
            }
        }

        return maximum;
    }

    public static boolean isOptimal(
            List<Session> sessions,
            Result result
    ) {
        return result.getRoomCount()
                == maximumOverlap(sessions);
    }
}
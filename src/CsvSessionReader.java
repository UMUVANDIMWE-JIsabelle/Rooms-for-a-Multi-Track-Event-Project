import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvSessionReader {

    public static List<Session> read(String filePath) throws IOException {
        List<Session> sessions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String headerLine = reader.readLine();

            if (headerLine == null) {
                throw new IOException("CSV file is empty.");
            }

            String[] headers = headerLine.toLowerCase().split(",");

            int idIndex = findColumn(headers, "id", "session", "sessionid", "session_id");
            int startIndex = findColumn(headers, "start", "starttime", "start_time");
            int endIndex = findColumn(headers, "end", "endtime", "end_time");

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] values = line.split(",");

                String id = values[idIndex].trim();
                int start = parseTime(values[startIndex].trim());
                int end = parseTime(values[endIndex].trim());

                sessions.add(new Session(id, start, end));
            }
        }

        return sessions;
    }

    private static int findColumn(
            String[] headers,
            String... possibleNames
    ) {
        for (int i = 0; i < headers.length; i++) {

            String header = headers[i]
                    .trim()
                    .replace("\"", "")
                    .replace(" ", "")
                    .toLowerCase();

            for (String name : possibleNames) {
                if (header.equals(name)) {
                    return i;
                }
            }
        }

        throw new IllegalArgumentException(
                "Required CSV column not found."
        );
    }

    private static int parseTime(String value) {

        value = value.replace("\"", "").trim();

        if (value.contains(":")) {
            String[] parts = value.split(":");

            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            return hours * 60 + minutes;
        }

        return Integer.parseInt(value);
    }
}
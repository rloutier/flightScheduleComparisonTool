import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InputSource {
    private static final int NB_FIELDS_TO_READ = 4;
    private final Path path;

    public InputSource(String strPath) {
        this.path = Path.of(strPath);
    }

    public List<FlightEntry> feedEntries() {
        try {
            return Files.lines(this.path)
                    .map(InputSource::parse)
                    .map(FlightEntry::of)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return null;
        }
    }

    private static String[] parse(String in) {
        // System.out.println("[parse] input String: "+ in);

        String[] arr = Arrays.stream(in.split(";", NB_FIELDS_TO_READ + 1))
                .limit(NB_FIELDS_TO_READ)
                .map(String::trim)
                .toArray(String[]::new);

        if (arr.length != NB_FIELDS_TO_READ)
            return null;

        boolean isValid = true;
        if (arr[0] == null || arr[0].length() < 5)
            isValid = false;
        if (arr[1] == null || arr[1].length() < 7)
            isValid = false;
        if (arr[2] == null || arr[2].length() < 7)
            isValid = false;
        if (arr[3] == null || arr[3].length() < 1)
            isValid = false;

        // System.out.println("[parse] output String: arr "+ Arrays.toString(arr));
        return isValid ? arr : null;
    }
}
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlightEntry {
    private static final int NB_FIELDS_TO_READ = 4;
    private final FlightId fId;
    private final FlightPeriod fPeriod;

    public FlightId getfId() {
        return fId;
    }

    public FlightPeriod getfPeriod() {
        return fPeriod;
    }

    public static FlightEntry of(String[] args) {
        // System.out.println("[FlightEntry::of] input String arr "+
        // Arrays.toString(args));
        if (args == null)
            return null;
        if (args.length != NB_FIELDS_TO_READ)
            return null;

        // args[0]: validation delegated to FlightId factory
        FlightId tmpId = FlightId.of(args[0]);
        // System.out.println("[FlightEntry::of] output Id "+ tmpId);
        if (tmpId == null)
            return null;

        // args[1-3]: validation delegated to FlightPeriod factory
        FlightPeriod tmpPeriod = FlightPeriod.of(Arrays.copyOfRange(args, 1, 4));
        // System.out.println("[FlightEntry::of] output Per "+ tmpPeriod);
        if (tmpPeriod == null)
            return null;

        return new FlightEntry(tmpId, tmpPeriod);
    }

    private FlightEntry(FlightId fid, FlightPeriod fp) {
        this.fId = fid;
        this.fPeriod = fp;
    }

    public String toString() {
        return this.fId + ": " + this.fPeriod;
    }
}

class FlightPeriod {
    private static final DateTimeFormatter inFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("ddMMMyy", Locale.US);
    private final LocalDate start, end;
    private final EnumSet<DayOfWeek> freq;

    public static FlightPeriod of(String[] args) {
        // System.out.println("[FlightPeriod::of] input String args "+
        // Arrays.toString(args));
        // args[0]: Dates input, stop instance creation if fail
        // args[1]:
        LocalDate sd;
        LocalDate ed;
        try {
            sd = LocalDate.parse(args[0], inFmt);
            ed = LocalDate.parse(args[1], inFmt);
        } catch (DateTimeParseException e) {
            return null;
        }
        if (sd.isAfter(ed))
            return null;

        // arg[2]: day freq input, stop instance creation if fail
        EnumSet<DayOfWeek> freq = EnumSet.noneOf(DayOfWeek.class);
        for (String digit : args[2].split("")) {
            switch (digit) {
                case "0":
                    break; // no-op; ignore
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                    freq.add(DayOfWeek.of(Integer.parseInt(digit)));
                    break;
                default:
                    return null;
            }
        }

        return new FlightPeriod(sd, ed, freq);
    }

    private FlightPeriod(LocalDate start, LocalDate end, EnumSet<DayOfWeek> freq) {
        this.start = start;
        this.end = end;
        this.freq = freq;
    }

    public List<LocalDate> inflate() {
        return start.datesUntil(end.plusDays(1))
                .filter(d -> freq.contains(d.getDayOfWeek()))
                .collect(Collectors.toList());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Stream.of(DayOfWeek.values())
                .map(d -> freq.contains(d) ? d.getValue() : "-")
                .forEach(sb::append);
        return start.format(outFmt).toUpperCase() + "-" + end.format(outFmt).toUpperCase() + "/" + sb;
    }
}

class FlightId {
    private final String value; // could be broken in few domain relevant parts (cie, number, suffix)

    public static FlightId of(String fid) {
        // Some validation, possibly failing and retunr null
        return new FlightId(fid);
    }

    private FlightId(String fid) {
        this.value = fid;
    }

    public String toString() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (o instanceof FlightId fid) {
            return this.value.equals(fid.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.value.hashCode();
    }
}

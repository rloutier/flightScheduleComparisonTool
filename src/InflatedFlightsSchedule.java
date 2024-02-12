import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

class InflatedFlightsSchedule {
    private final Map<FlightId, InflatedFlightSchedule> schedule = new HashMap<>();
    private final Map<FlightId, FlightScheduleDuplicateDates> duplicates = new HashMap<>();

    public void load(InputSource scheduleSrc) {
        scheduleSrc.feedEntries()
                .stream()
                .forEach(this::loadEntry);
    }

    private void loadEntry(FlightEntry fe) {
        FlightId currFlight = fe.getfId();
        FlightPeriod currPeriod = fe.getfPeriod();

        if (!schedule.containsKey(currFlight)) {
            schedule.put(currFlight, new InflatedFlightSchedule());
        }

        for (LocalDate currDate : currPeriod.inflate()) {
            FlightPeriod prevPeriod = schedule.get(currFlight).loadDate(currDate, currPeriod);
            if (prevPeriod != null) {
                if (!duplicates.containsKey(currFlight)) {
                    duplicates.put(currFlight, new FlightScheduleDuplicateDates());
                }
                duplicates.get(currFlight).loadDate(currDate, currPeriod, prevPeriod);
            }
        }
    }

    public void display(String flight) {
        FlightId fid = FlightId.of(flight);

        if (fid == null) {
            System.out.println("[disp:] Flight invalid -> '" + flight + "'");
        } else {
            if (!schedule.containsKey(fid)) {
                System.out.println("[disp:] Flight not in schedule -> '" + flight + "'");
            } else {
                schedule.get(fid).display(flight);
            }
        }
    }

    public void displayDuplicates(String flight) {
        FlightId fid = FlightId.of(flight);

        if (fid == null) {
            System.out.println("[disp:] Flight invalid -> '" + flight + "'");
        } else {
            if (!duplicates.containsKey(fid)) {
                System.out.println("[disp:] Flight not in schedule -> '" + flight + "'");
            } else {
                duplicates.get(fid).display(flight);
            }
        }
    }

    public void displayDuplicates() {
        duplicates.keySet()
                .stream()
                .forEach(fid -> displayDuplicates(fid.toString()));
    }

    public void displayNoneOf(InflatedFlightsSchedule otherSchedule) {
        schedule.keySet()
                .stream()
                .forEach(
                    fid -> {
                        if (!otherSchedule.schedule.containsKey(fid)) {
                            System.out.println("Flight not in OTHER flight");
                        } else {
                            schedule.get(fid).displayNoneOf(otherSchedule.schedule.get(fid), fid.toString());
                        }
                    }
                );
    }
}

class InflatedFlightSchedule {
    private static final DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("ddMMMyy", Locale.US);
    private final Map<LocalDate, FlightPeriod> flownDates = new HashMap<>();

    FlightPeriod loadDate(LocalDate date, FlightPeriod fp) {
        return flownDates.putIfAbsent(date, fp);
    }

    public void display() { display(""); }

    public void display(String prefix) {
        flownDates
            .forEach((k, v) -> System.out.println(
                    (prefix.isEmpty() ? "" : prefix + ";") +
                    k.format(outFmt).toUpperCase() + ";" + v
                )
            );
    }

    public void displayNoneOf(InflatedFlightSchedule otherSchedule) { displayNoneOf(otherSchedule,""); }

    public void displayNoneOf(InflatedFlightSchedule otherSchedule, String prefix) {
        flownDates.entrySet()
            .stream()
            .filter( entry -> !otherSchedule.flownDates.containsKey(entry.getKey()))
            .forEach( entry -> System.out.println(
                    (prefix.isEmpty() ? "" : prefix + ";") +
                    entry.getKey().format(outFmt).toUpperCase() + ";" + entry.getValue()
                )
            );
    }

    public String toString() {
        return flownDates.toString();
    }
}

class FlightScheduleDuplicateDates {
    private static final DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("ddMMMyy", Locale.US);
    private final Map<LocalDate, List<FlightPeriod>> dupsDates = new HashMap<>();

    void loadDate(LocalDate date, FlightPeriod current, FlightPeriod previous) {
        if (!dupsDates.containsKey(date)) {
            dupsDates.put(date, new ArrayList<>(List.of(previous)));
        }
        dupsDates.get(date).add(current);
    }

    public void display() {
        display("", "");
    }

    public void display(String prefix) {
        display(prefix, "");
    }

    public void display(String prefix, String suffix) {
        dupsDates.forEach((k, v) -> System.out.println(
                (prefix.isEmpty() ? "" : prefix + ";") +
                        k.format(outFmt).toUpperCase() + ";" +
                        v.stream()
                                .map(FlightPeriod::toString)
                                .collect(Collectors.joining(";"))
                        +
                        (suffix.isEmpty() ? "" : ";" + suffix)));
    }

    public String toString() {
        return dupsDates.toString();
    }
}
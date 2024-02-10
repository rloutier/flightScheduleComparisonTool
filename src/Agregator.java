import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

class Agregator {
    private final Map<FlightId, SimpleDatesAgregator> flights = new HashMap<>();
    private final Map<FlightId, DuplicateDatesAgregator> duplicates = new HashMap<>();

    public void loadSchedule(InputSource scheduleSrc) {
        scheduleSrc.feedEntries()
                .stream()
                .forEach(this::loadEntry);
    }

    private void loadEntry(FlightEntry fe) {
        FlightId currFlight = fe.getfId();
        FlightPeriod currPeriod = fe.getfPeriod();

        if (!flights.containsKey(currFlight)) {
            flights.put(currFlight, new SimpleDatesAgregator());
        }

        for (LocalDate currDate : currPeriod.inflate()) {
            FlightPeriod prevPeriod = flights.get(currFlight).loadDate(currDate, currPeriod);
            if (prevPeriod != null) {
                if (!duplicates.containsKey(currFlight)) {
                    duplicates.put(currFlight, new DuplicateDatesAgregator());
                }
                duplicates.get(currFlight).loadDate(currDate, currPeriod, prevPeriod);
            }
        }
    }

    public void displayScheduleForFlight(String inFlight) {
        display(inFlight, false);
    }

    public void displayDuplicatesForFlight(String inFlight) {
        display(inFlight, true);
    }

    public void displayAllDuplicates() {
        duplicates.keySet()
            .stream()
            .forEach(fid -> display(fid.toString(), true));
    }

    private void display(String inFlight, boolean forDuplicate) {
        FlightId fid = FlightId.of(inFlight);

        if (fid == null) {
            System.out.println("[disp:] Flight invalid -> '" + inFlight + "'");
        } else {
            if (forDuplicate) {
                if (!duplicates.containsKey(fid)) { 
                    System.out.println("[disp:] Flight without duplicates -> '" + inFlight + "'");
                } else  {
                    duplicates.get(fid).display(inFlight);
                }
            } else {
                if (!flights.containsKey(fid)) { 
                    System.out.println("[disp:] Flight not in schedule -> '" + inFlight + "'");
                } else  {
                    flights.get(fid).display(inFlight);
                }
            }
        }
    }
}

class SimpleDatesAgregator {
    private static final DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("ddMMMyy", Locale.US);
    private final Map<LocalDate, FlightPeriod> flownDates = new HashMap<>();

    FlightPeriod loadDate(LocalDate date, FlightPeriod fp) {
        return flownDates.putIfAbsent(date, fp);
    }

    public void display() {
        display("","");
    }
    public void display(String prefix) {
        display(prefix,"");
    }
    public void display(String prefix, String suffix) {
        flownDates.forEach((k,v) -> 
                            System.out.println(
                                (prefix.isEmpty() ? "" : prefix + ";" ) +
                                k.format(outFmt).toUpperCase() + ";" + v +
                                (suffix.isEmpty() ? "" : ";"+ suffix )
                                )
                            );
    }

    public String toString() {
        return flownDates.toString();
    }
}

class DuplicateDatesAgregator {
    private static final DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("ddMMMyy", Locale.US);
    private final Map<LocalDate, List<FlightPeriod>> dupsDates = new HashMap<>();

    void loadDate(LocalDate date, FlightPeriod current, FlightPeriod previous) {
        if (!dupsDates.containsKey(date)) {
            dupsDates.put(date, new ArrayList<>(List.of(previous)));
        }
        dupsDates.get(date).add(current);
    }

    public void display() {
        display("","");
    }
    public void display(String prefix) {
        display(prefix,"");
    }
    public void display(String prefix, String suffix) {
        dupsDates.forEach((k,v) -> 
                            System.out.println(
                                (prefix.isEmpty() ? "" : prefix + ";" ) +
                                k.format(outFmt).toUpperCase() + ";" +
                                    v.stream()
                                    .map(FlightPeriod::toString)
                                    .collect(Collectors.joining(";")) +
                                (suffix.isEmpty() ? "" : ";"+ suffix )
                                )
                            );
    }

    public String toString() {
        return dupsDates.toString();
    }
}
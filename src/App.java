
public class App {
    public static void main(String[] args) {
        // InputSource flightScheduleSrc = new InputSource("input/fdc_vols_formate_AF_DL_filtre_doublon_TimeBoxed.csv");
        // OutputSink sink = new OutputSink();
        InputSource flightScheduleSrc = new InputSource("input/fdc_short.csv");
        InflatedFlightsSchedule flightsSchedule = new InflatedFlightsSchedule();

        flightsSchedule.load(flightScheduleSrc);
        flightsSchedule.display("AF004");
        flightsSchedule.displayDuplicates("AF004");
        flightsSchedule.displayDuplicates();
    }
}
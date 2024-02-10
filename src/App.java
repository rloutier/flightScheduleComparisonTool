
public class App {
    public static void main(String[] args) {
        InputSource flightScheduleSrc = new InputSource("input/fdc_vols_formate_AF_DL_filtre_doublon_TimeBoxed.csv");
        // OutputSink sink = new OutputSink();
        // InputSource flightScheduleSrc = new InputSource("input/fdc_short.csv");
        Agregator flightScheduleAgregate = new Agregator();

        flightScheduleAgregate.loadSchedule(flightScheduleSrc);
        // flightScheduleAgregate.displayScheduleForFlight("AF002");
        // flightScheduleAgregate.displayDuplicatesForFlight("AF002");
        flightScheduleAgregate.displayAllDuplicates();
    }
}
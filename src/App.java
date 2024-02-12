
public class App {
    public static void main(String[] args) {
        // InputSource flightScheduleSrc = new InputSource("input/fdc_vols_formate_AF_DL_filtre_doublon_TimeBoxed.csv");
        // OutputSink sink = new OutputSink();
        InputSource sch1Src = new InputSource("input/icss_short.csv");
        InputSource sch2Src = new InputSource("input/icss_short_2.csv");
        InflatedFlightsSchedule fdc1 = new InflatedFlightsSchedule();
        InflatedFlightsSchedule fdc2 = new InflatedFlightsSchedule();

        fdc1.load(sch1Src);
        fdc2.load(sch2Src);

        fdc1.display("AF001");
        System.out.println();
        fdc2.display("AF001");

        fdc1.displayDuplicates("AF004");
        fdc1.displayDuplicates();

        fdc1.displayNoneOf(fdc2);
        fdc2.displayNoneOf(fdc1);
    }
}
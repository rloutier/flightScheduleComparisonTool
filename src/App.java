
public class App {
    public static void main(String[] args) {
        InputSource fdcFile = new InputSource("input/fdc-vol_S24_only.csv");
        InputSource dmsFile = new InputSource("input/dms-vol_S24_only_periodes_infinies.csv");
        // InputSource fdcFile = new InputSource("input/fdc_short.csv");
        // InputSource icssFile = new InputSource("input/icss_short.csv");

        InflatedFlightsSchedule fdc = new InflatedFlightsSchedule("FDC");
        InflatedFlightsSchedule dms = new InflatedFlightsSchedule("DMS");

        fdc.load(fdcFile);
        dms.load(dmsFile);

        // String ff;
        // ff = "AF7463";
        // System.out.println("FDC: " + ff);
        // fdc.display(ff);

        // ff = "AF7463";
        // System.out.println();
        // System.out.println("FDC " + ff +": duplicates");
        // fdc.displayDuplicates(ff);

        System.out.println();
        System.out.println("Dates exclusive to FDC:");
        fdc.displayNoneOf(dms);
        // System.out.println("Dates exclusive to DMS:");
        // dms.displayNoneOf(fdc);
    }
}
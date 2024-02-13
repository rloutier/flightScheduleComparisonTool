
public class App {
    public static void main(String[] args) {
        // InputSource fdcFile = new InputSource("input/fdc_vols_formate_AF_DL_filtre_doublon_TimeBoxed.csv");
        InputSource fdcFile = new InputSource("input/schedule_fdc_20240212_AF_DL_doublon_TimeBoxed.csv");
        InputSource icssFile = new InputSource("input/icss_vols_AF_DL_filtre_doublon_TimeBoxed.csv");
        // InputSource fdcFile = new InputSource("input/fdc_short.csv");
        // InputSource icssFile = new InputSource("input/icss_short.csv");

        InflatedFlightsSchedule fdc = new InflatedFlightsSchedule("FDC");
        InflatedFlightsSchedule icss = new InflatedFlightsSchedule("ICSS");

        fdc.load(fdcFile);
        icss.load(icssFile);

        // String ff = "AF002"; System.out.println("FDC: " + ff);
        // fdc.display(ff);
        // System.out.println();
        // ff = "AF002"; System.out.println("ICSS: " + ff);
        // icss.display(ff);

        // System.out.println();
        // System.out.println("FDC AF999B: duplicates");
        // fdc.displayDuplicates("AF999B");
        // System.out.println("ICSS: duplicates");
        // icss.displayDuplicates();

        System.out.println();
        // System.out.println("Dates exclusive to FDC:");
        // fdc.displayNoneOf(icss);
        System.out.println("Dates exclusive to ICSS:");
        icss.displayNoneOf(fdc);
    }
}
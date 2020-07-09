package govph.rsis.growapp;

public class DecVar {
    public static final String receiver() {

        String staging = "https://stagingdev.philrice.gov.ph/rsis/growApp";
        String production = "https://rsis.philrice.gov.ph/growApp";
        String training = "https://rsistraining.philrice.gov.ph/growApp";

        return production;
    }
}

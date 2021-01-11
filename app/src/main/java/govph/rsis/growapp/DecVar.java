package govph.rsis.growapp;

public class DecVar {
    public static final String receiver() {

        String staging = "https://stagingdev.philrice.gov.ph/rsis/growApp";
        String production = "https://rsis.philrice.gov.ph/growApp";
        String training = "https://rsistraining.philrice.gov.ph/growApp";

        String localhost = "http://192.168.43.5/growApp";
        return production;
    }
}

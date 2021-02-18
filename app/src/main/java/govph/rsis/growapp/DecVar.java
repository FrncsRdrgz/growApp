package govph.rsis.growapp;

public class DecVar {
    public static final String receiver() {

        String staging = "https://stagingdev.philrice.gov.ph/rsis/growApp";
        String production = "https://rsis.philrice.gov.ph/growApp";
        String training = "https://rsistraining.philrice.gov.ph/growApp";

        return production;
    }

    public static final String userLoginDetailsAPI(){
        String production = "https://rsis.philrice.gov.ph/seed_ordering/seed_ordering/getUserLoginDetails";

        return production;
    }
}

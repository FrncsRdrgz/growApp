package govph.rsis.growapp;

public class DecVar {
    public static final String receiver() {

        String staging = "https://stagingdev.philrice.gov.ph/rsis/growApp";
        String production = "https://rsis.philrice.gov.ph/growApp";
        String training = "https://rsistraining.philrice.gov.ph/growApp";
        String localhost = "http://192.168.0.101/growApp";
        return production;
    }

    public static final String userCredentialApiUrl(){
        String production = "https://rsis.philrice.gov.ph/seed_ordering/api/getUserCredentials";
        String localhost = "http://192.168.0.101/seed_ordering/api/getUserCredentials";
        return production;
    }

    public static final String getSeedBought(){
        String production = "https://rsis.philrice.gov.ph/seed_ordering/api/getSeedBought";
        String localhost = "http://192.168.0.101/seed_ordering/api/getSeedBought";

        return production;
    }
    public static boolean isNetworkConnected = false;
}

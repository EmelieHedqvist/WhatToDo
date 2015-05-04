package com.emehed.emeliehedqvist.whattodo;

/**
 * Created by henriktaljedal on 2015-04-29.
 */
public class SearcherDummy {
    public static Place search(String keyword, double latitude, double longitude, int radius) {

        //DUMMY-KLASS FÖR ATT VISA HUR DET KOMMER ATT FUNGERA, DÅ KEYWORD, TYP "bar", latitude, longitude OCH radius skickas
        //SOM INPARAMETRAR TILL search-metoden REURNERAS EN INSTANS AV PLACE SOM INNEHÅLLER ALL INFO OM FRAMSLUMPAD PLATS!

        Place dummyPlace = new Place();
        dummyPlace.name = "Golden-I";
        dummyPlace.address = String.format("Storgatan 47 \n411 38 Göteborg");
        dummyPlace.rating = "5.0";
        dummyPlace.phone = "031-960200";
        dummyPlace.isOpen = true;
        dummyPlace.reference = "DUMMY_REFERENCE_123456789";

        return dummyPlace;
    }
}

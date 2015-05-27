package com.emehed.emeliehedqvist.whattodo;

/**
 * Created by henriktaljedal on 2015-05-11.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by henriktaljedal on 2015-05-04.
 */
public class PlaceFinder{

    //This is the developer key for access to GooglePlaces, generated for developer usage with the WhatToDo App
    private static String googleWebServicePermission = "AIzaSyDtYpMpKbapO5YkwHO5h265jccWsiYUx58";
    //This is the instantiation for the String value for the value searched for, i.e. bar, restaurant, activity etc.
    String keyword;

    // search is the method which is called from DisplayActivity -
    // 'search' method builds the url (which is sent to google web services later on, in the 'downloadUrl' method)
    // 'search' calls DownloadWebpage with the url as parameter
    public DownloadWebpage search(String keyword, double latitude, double longitude, int radius){
        //A check is done on keyword, to create the right values to put in the URL
        this.keyword = keyword;
        if(keyword.equals("activity")){
            keyword = "amusement_park|aquarium|art_gallery|bowling_alley|casino|movie_rental|movie_theater|museum|spa|zoo";
        }
        //If 'mystery box' is clicked, random is sent as keyword. The keyword used in the URL is thereby chosen by random
        else if (keyword.equals("random")){
            Random rand = new Random();
            int  i = rand.nextInt(3);
            if (i==0)
                keyword = "bar";
            if (i==1)
                keyword = "restaurant";
            else
            keyword = "amusement_park|aquarium|art_gallery|bowling_alley|casino|movie_rental|movie_theater|museum|nightclub|spa|zoo";
        }
            //This is the creation of the URL, which is sent to Google Web Services in method 'downloadUrl'
            //The criteria opennow in the URL is to search only for places that are open when the search action is executed
            String findPlaceUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "&radius=" + radius + "&types=" + keyword + "&sensor=true&opennow&key=" + googleWebServicePermission;


        //This creates a new instance of the sub class DownloadWebpage, which is run away from the main UI thread
        DownloadWebpage dwt = new DownloadWebpage();
        //dwt.execute(...) starts the method doInBackground(...) in sub class DownloadWebpage, which is run away from the main UI thread
        dwt.execute(findPlaceUrl);
        //What is returned is a instance of the sub class DownloadWebpage, which has to be a sub class, this enables DisplayActivity to get access to
        //the DownloadWebpage sub class
        // in the sub class DownloadWebpage, which runs as a AsyncTask, away from the main UI thread
        return dwt;
    }




    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // used in the onPostExecute method, which takes the string and convertes it into a JSONObject
    // Then the information in the JSONObject is converted into instances of the class WPlace,
    // one instance of WPlace is created for each 'place' found on Google Web Services
    // These instances of WPlace are stored in an array list
    // Finally, by random one of the WPlace instances in this array list is chosen by random,
    // and then sent to the method processFinished via an 'AsyncResponce delegate' call, when this thread
    // (away from the main UI thread) is finished
    public class DownloadWebpage extends AsyncTask<String, Void, String> {
        public AsyncResponse delegate=null;
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays is run last of the AsyncTask.
        // This method takes the data from Google Web Services, converted as a String, and convertes it to a JSONObject
        // then it sends the JSONObject as a parameter to the method 'parse', which returns an instance of the class WPlace
        // then 'delegate' is executed, which is a call for the method 'processFinished(...)' in the class DisplayActivity
        // in this way, an instance of a WPlace object is sent to DisplayActivity when this thread (away from the UI thread) is finished
        // If this is unclear, contact us on henrik@taljedal.se for a better explanation! :)
        @Override
        protected void onPostExecute(String result) {
            JSONObject jObject;
            WPlace place;

            try{
                jObject = new JSONObject(result);

                /** Getting the parsed data as a List construct */
                //
                place = parse(jObject);

                delegate.processFinish(place);

            }catch(Exception e){
                Log.d("Exception", e.toString());
            }
        }
    }
    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("HTTP_Example", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader;
        StringBuilder jsonResults = new StringBuilder();
        int readSize;
        reader = new InputStreamReader(stream, "UTF-8");

        char[] buff = new char[1024];
        while ((readSize = reader.read(buff)) != -1) {
            jsonResults.append(buff, 0, readSize);
        }


        return jsonResults.toString();
    }
    /** Receives a JSONObject and returns an instance of a WPlace object */
    public WPlace parse(JSONObject jObject){
        //Creates an array list to store all instances of WPlace in, before a single WPlace instance is chosen by random and sent
        //to the method processFinished(...) in class DisplayActivity, by 'delegate' in the 'onPostExecute' method.
        ArrayList<WPlace> placesList = new ArrayList<WPlace>();
        WPlace place, chosenPlace;

        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int placesCount = jPlaces.length();

        /** Taking each place, parses and adds to list object */
        for(int i=0; i<placesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject)jPlaces.get(i));
                    placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        //Takes the arraylist and chooses one single WPlace instance by random, then returns this WPlace instance
        if (!placesList.isEmpty()) {
            Random rand = new Random();
            int i = rand.nextInt(placesList.size());
            chosenPlace = placesList.get(i);
            return chosenPlace;
        }
        else return null;

    }

    //Gets a JSONObject, reads it and stores the data in it in an instance of a WPlace object
    //Then this WPlace instance is returned
    private WPlace getPlace(JSONObject jPlace){

        WPlace place = new WPlace();

        try {
            // Extracting WPlace Reference, if available
            if(!jPlace.isNull("reference")){
                place.reference = jPlace.getString("reference");
            }
            // Extracting WPlace name, if available
            if(!jPlace.isNull("name")){
                place.name = jPlace.getString("name");
            }
            // Extracting WPlace latitude, if available
            if(!jPlace.isNull("geometry")){
                JSONObject point = jPlace.getJSONObject("geometry").getJSONObject("location");
                place.lat = point.getDouble("lat");
            }
            // Extracting WPlace longitude, if available
            if(!jPlace.isNull("geometry")){
                JSONObject point = jPlace.getJSONObject("geometry").getJSONObject("location");
                place.lng = point.getDouble("lng");
            }

            // Extracting WPlace Vicinity, if available
            if(!jPlace.isNull("vicinity")){
                place.address = jPlace.getString("vicinity");
            }

            // Extracting WPlace Rating, if available, if not available use '?' instead
            if(!jPlace.isNull("rating")){
                place.rating = jPlace.getString("rating");
            }
            else
                place.rating = "?";



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}

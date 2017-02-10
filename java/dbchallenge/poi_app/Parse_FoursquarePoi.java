package dbchallenge.poi_app;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class Parse_FoursquarePoi {

  public static void main(String[] args) {
  }

  public static void getPoi() {

    // foursquare user: huyraid@gmail.com, Michael Huy Truong-Ngoc
    final String CLIENT_ID = "UNKQJOHWTCLUUF1HWFFEUSUVH3IJ3BTN00OCR2JE1N0C4MQ4";
    final String CLIENT_SECRET = "JMRC1WFDFQZQAKPX4NPNF5JJC23OTDATL5L3BWVTY3GFTRAB";

    // latitude and the longitude from KASSEL-WILHELMSHÖHE
        /*String latitude = "51.313115";
        String longtitude = "9.446898"; */

    String latitude = Main.latitude;
    String longtitude = Main.longitude;

    // category ids
    String park = "4bf58dd8d48988d163941735";
    String bridge = "4bf58dd8d48988d1df941735";
    String castle = "50aaa49e4b90af0d42d5de11";
    String lake = "4bf58dd8d48988d161941735";
    String themepark = "4bf58dd8d48988d182941735";
    String waterpark = "4bf58dd8d48988d193941735";
    String palace = "52e81612bcbc57f1066b7a14";
    String church = "4bf58dd8d48988d132941735";


    String url = "https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&ll=" + latitude + "," + longtitude + "&limit=5&categoryId=" + park + "," + church + "," + bridge + "," + palace + "," + lake + "&v=20170111";
    try {
      String foursquare = IOUtils.toString(new URL(url));
      JSONObject JsonObject = (JSONObject) JSONValue.parseWithException(foursquare);

      // PARSE FOURSQUARE JSON
      JSONObject response = (JSONObject) JsonObject.get("response");
      JSONArray venuesArr = (JSONArray) response.get("venues");

      JSONObject venuesArr0 = (JSONObject) venuesArr.get(0);
      JSONObject venuesArr1 = (JSONObject) venuesArr.get(1);
      JSONObject venuesArr2 = (JSONObject) venuesArr.get(2);
      JSONObject venuesArr3 = (JSONObject) venuesArr.get(3);

      JSONObject location0 = (JSONObject) venuesArr0.get("location");
      JSONObject location1 = (JSONObject) venuesArr1.get("location");
      JSONObject location2 = (JSONObject) venuesArr2.get("location");
      JSONObject location3 = (JSONObject) venuesArr3.get("location");

      JSONArray locationformatted0 = (JSONArray) location0.get("formattedAddress");
      JSONArray locationformatted1 = (JSONArray) location1.get("formattedAddress");
      JSONArray locationformatted2 = (JSONArray) location2.get("formattedAddress");
      JSONArray locationformatted3 = (JSONArray) location3.get("formattedAddress");

      if(location0.get("city") != null) {
        System.out.println(venuesArr0.get("name") + ", " + locationformatted0.toString().replaceAll("[\\[\\]\"]",""));
        System.out.println("------------------");

        Main.finalPois.put(venuesArr0.get("name") + ", " + location0.get("city"));

        Main.Poi_name.put(venuesArr0.get("name"));
        Main.Poi_location.put(locationformatted0.toString().replaceAll("[\\[\\]\"]",""));
        if(venuesArr0.get("url") != null) {
          Main.Poi_tips.put(venuesArr0.get("url"));
        } else {
          Main.Poi_tips.put("keine URL vorhanden: https://www.google.de/#q=" + venuesArr0.get("name") + "," + location0.get("city"));
        }
      }
      if(location0.get("city") == null && location1.get("city") != null) {
        System.out.println(venuesArr1.get("name") + ", " + locationformatted1.toString().replaceAll("[\\[\\]\"]",""));
        System.out.println("------------------");

        Main.finalPois.put(venuesArr1.get("name") + ", " + location1.get("city"));

        Main.Poi_name.put(venuesArr1.get("name"));
        Main.Poi_location.put(locationformatted1.toString().replaceAll("[\\[\\]\"]",""));
        if(venuesArr1.get("url") != null) {
          Main.Poi_tips.put(venuesArr1.get("url"));
        } else {
          Main.Poi_tips.put("keine URL vorhanden: https://www.google.de/#q=" + venuesArr1.get("name") + "," + location1.get("city"));
        }
      }
      if(location0.get("city") == null && location1.get("city") == null && location2.get("city") != null) {
        System.out.println(venuesArr2.get("name") + ", " + locationformatted2.toString().replaceAll("[\\[\\]\"]",""));
        System.out.println("------------------");

        Main.finalPois.put(venuesArr2.get("name") + ", " + location2.get("city"));

        Main.Poi_name.put(venuesArr2.get("name"));
        Main.Poi_location.put(locationformatted2.toString().replaceAll("[\\[\\]\"]",""));
        if(venuesArr2.get("url") != null) {
          Main.Poi_tips.put(venuesArr2.get("url"));
        } else {
          Main.Poi_tips.put("keine URL vorhanden: https://www.google.de/#q=" + venuesArr2.get("name") + "," + location2.get("city"));
        }
      }
      if(location0.get("city") == null && location1.get("city") == null && location2.get("city") == null && location3.get("city") != null) {
        System.out.println(venuesArr3.get("name") + ", " + locationformatted3.toString().replaceAll("[\\[\\]\"]",""));
        System.out.println("------------------");

        Main.finalPois.put(venuesArr3.get("name") + ", " + location3.get("city"));

        Main.Poi_name.put(venuesArr3.get("name"));
        Main.Poi_location.put(locationformatted3.toString().replaceAll("[\\[\\]\"]",""));
        if(venuesArr3.get("url") != null) {
          Main.Poi_tips.put(venuesArr3.get("url"));
        } else {
          Main.Poi_tips.put("keine URL vorhanden: https://www.google.de/#q=" + venuesArr3.get("name") + "," + location3.get("city"));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}

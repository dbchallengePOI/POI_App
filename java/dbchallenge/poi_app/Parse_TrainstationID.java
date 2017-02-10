package dbchallenge.poi_app;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class Parse_TrainstationID {

  public static void getID() {

    String startName = Main.START_ID;

    String url = "https://open-api.bahn.de/bin/rest.exe/location.name?authKey=TestDemoAPI16&lang=de&input=" + startName + "&format=json";
    try {
      String LocationListUrl = IOUtils.toString(new URL(url));
      JSONObject JsonObject = (JSONObject) JSONValue.parseWithException(LocationListUrl);

      // PARSE ID JSON
      JSONObject LocationList = (JSONObject) JsonObject.get("LocationList");
      JSONArray StopLocation = (JSONArray) LocationList.get("StopLocation");
      JSONObject StopLocation0 = (JSONObject) StopLocation.get(0);

      System.out.println("ID FROM TRAINSTATION: " + StopLocation0.get("id"));
      Main.trainstationID = StopLocation0.get("id").toString();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}

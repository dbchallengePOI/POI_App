package dbchallenge.poi_app;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class GetImageURL {
  public static void getURL() {

    String POI = Main.notification_content;

    String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyDVzEqbpq0cYrCLbMc0Mf1345hnzMVN9Os&cx=012334199422596157288:s2_fhm_tndo&searchType=image&q=" + POI;
    // String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyC-khoSU1orRLifjcxLOiYY70X7Oxn7rTE&cx=003998160206604891176:9-vcfxnnfhw&searchType=image&q=" + POI;
    try {
      String foursquare = IOUtils.toString(new URL(url));
      JSONObject JsonObject = (JSONObject) JSONValue.parseWithException(foursquare);

      JSONArray items = (JSONArray) JsonObject.get("items");
      JSONObject items1 = (JSONObject) items.get(0);

      Main.Poi_imageurl_view = items1.get("link").toString();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}

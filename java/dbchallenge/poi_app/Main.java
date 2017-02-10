package dbchallenge.poi_app;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.os.StrictMode;

public class Main extends Activity {

  JSONArray journeyStops;

  public static JSONArray NotificationTimes = new JSONArray();
  public static JSONArray finalPois = new JSONArray();

  //for open View Information
  public static JSONArray Poi_name = new JSONArray();
  public static JSONArray Poi_location = new JSONArray();
  public static JSONArray Poi_tips = new JSONArray();
  public static String Poi_name_view;
  public static String Poi_location_view;
  public static String Poi_tips_view;
  public static String Poi_imageurl_view;

  //for Heads up notification
  public static String Poi_subinfo_view;
  public static String notification_content;

  // for getting POI foursquare
  public static String latitude;
  public static String longitude;

  //for AlarmManager
  public String minute;
  public String hour;

  //for Hotel feature
  public static String latitude_end;
  public static String longitude_end;


  //DB INFORMATION --> FRANKFURT - BERLIN
  /*
  final String START_ID = "008000105"; // Frankfurt Hbf ID
  final String ICE_NAME = "ICE 596";
  final String START_TIME = "16:09";
  final String START_DATE = "2017-04-02"; */
  final String authKey = "TestDemoAPI16";

  // UI input ---- START_ID for Parse_TrainstationID
  public static String START_ID = ""; // Frankfurt Hbf ID
  String ICE_NAME = "";
  String START_TIME = "";
  // 2017-04-02
  String START_DATE = "";

  String START_MONTH = "";
  String START_DAY = "";
  String START_YEAR = "";

  public static String trainstationID;

  String dbJourneyExtracted;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
  }

  // start the AsyncTask that makes the call for the venue search.
  public void getPoi(View view) {
    EditText start = (EditText)findViewById(R.id.start);
    EditText ice = (EditText)findViewById(R.id.ice);
    EditText date = (EditText)findViewById(R.id.date);
    EditText time = (EditText)findViewById(R.id.time);

    START_ID = start.getText().toString();
    ICE_NAME = ice.getText().toString();
    START_TIME = time.getText().toString();
    START_DATE = date.getText().toString();

    START_DAY = date.getText().toString().substring(0,2);
    START_MONTH = date.getText().toString().substring(3,5);
    START_YEAR = date.getText().toString().substring(6);

    new generateJourneyDetail().execute();
  }

  // start view of hotels and restaurants near destination
  public void getPoiForDestination(View view) {
    Intent intentView = new Intent(this, open_DestinationPoi.class);
    startActivity(intentView);
  }

  private void showAlert(String msg) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(msg)
      .setCancelable(false)
      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          //do things
        }
      });
    AlertDialog alert = builder.create();
    alert.show();
  }

  public class generateJourneyDetail extends AsyncTask<View, Void, String> {

    String dbJourney;
    String dbDeparture;

    @Override
    protected void onPreExecute() {
        Parse_TrainstationID.getID();
    }

    @Override
    protected String doInBackground(View... urls) {

      String START_DATE_CONV = START_YEAR + "-" + START_MONTH + "-" + START_DAY;

      dbDeparture = httpCall("https://open-api.bahn.de/bin/rest.exe/departureBoard?authKey=" + authKey + "&lang=de&id=" + trainstationID + "&date=" + START_DATE_CONV + "&time=" + URLEncoder.encode(START_TIME) + "&format=json");
      dbJourneyExtracted = parseDbDeparture(dbDeparture, ICE_NAME);
      dbJourney = httpCall(dbJourneyExtracted);

      return "";
    }

    @Override
    protected void onPostExecute(String result) {
      journeyStops = parseDbJourney(dbJourney);
      System.out.println("All stations (journey): " + journeyStops);


      // for Hotel and Restaurant
      Integer journeyStops_length = journeyStops.length() - 1;
      try {
        longitude_end = journeyStops.getJSONObject(journeyStops_length).getString("lon");
        latitude_end = journeyStops.getJSONObject(journeyStops_length).getString("lat");
      } catch (JSONException e) {
        e.printStackTrace();
      }


      if (journeyStops.length() > 0) {
        for (int i = 0; i < journeyStops.length(); i++) {
          try {
            latitude = journeyStops.getJSONObject(i).getString("lat");
            longitude = journeyStops.getJSONObject(i).getString("lon");

            JSONObject temp = new JSONObject();

            if (journeyStops.getJSONObject(i).has("arrTime")) {
              minute = journeyStops.getJSONObject(i).getString("arrTime").substring(3);
              hour = journeyStops.getJSONObject(i).getString("arrTime").substring(0, 2);
              temp.put("hour", hour);
              temp.put("min", minute);
              NotificationTimes.put(temp);
            } else {
              minute = journeyStops.getJSONObject(i).getString("depTime").substring(3);
              hour = journeyStops.getJSONObject(i).getString("depTime").substring(0, 2);
              temp.put("hour", hour);
              temp.put("min", minute);
              NotificationTimes.put(temp);
            }
            System.out.println(Integer.parseInt(hour) + ":" + Integer.parseInt(minute) + "Uhr:");
          } catch (Exception e) {
            e.printStackTrace();
          }
          Parse_FoursquarePoi.getPoi();
        }

        System.out.println("Final POIs: " + finalPois);
        System.out.println("Notification Times: " + NotificationTimes);

        for (int i = 0; i < finalPois.length(); i++) {

          try {
            notification_content = finalPois.getString(i);
            Poi_name_view = Poi_name.getString(i);
            Poi_location_view = Poi_location.getString(i);
            Poi_tips_view = Poi_tips.getString(i);
            Poi_subinfo_view = journeyStops.getJSONObject(i).getString("name");

            Integer start_hour = Integer.parseInt(START_TIME.substring(0, 2));
            Integer start_min = Integer.parseInt(START_TIME.substring(3));

            GetImageURL.getURL();

            //check, only give POI notifications,which comes after start station
            if ((Integer.parseInt(NotificationTimes.getJSONObject(i).getString("hour")) == start_hour && Integer.parseInt(NotificationTimes.getJSONObject(i).getString("min")) > start_min)
                    | Integer.parseInt(NotificationTimes.getJSONObject(i).getString("hour")) > start_hour) {
                startNotification(
                  Integer.parseInt(NotificationTimes.getJSONObject(i).getString("hour")),
                  Integer.parseInt(NotificationTimes.getJSONObject(i).getString("min")),
                  Integer.parseInt(START_DAY),
                  Integer.parseInt(START_MONTH) - 1,
                  Integer.parseInt(START_YEAR));
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        showAlert("POI notifications for your journey set up successfully!");
      } else {
        System.out.println("WRONG INPUT!");
        showAlert("NOT successfully, ticket input is wrong!");
      }
    }
  }

  // setting up the notification
  public void startNotification(Integer h, Integer min, Integer day, Integer month, Integer year) {
    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(this, NotificationReceiver.class);

    intent.putExtra("content", notification_content);
    intent.putExtra("name", Poi_name_view);
    intent.putExtra("location", Poi_location_view);
    intent.putExtra("tips", Poi_tips_view);
    intent.putExtra("subinfo", Poi_subinfo_view);
    intent.putExtra("imgurl", Poi_imageurl_view);

    //get unique ID
    long time = new Date().getTime();
    String tmpStr = String.valueOf(time);
    String last4Str = tmpStr.substring(tmpStr.length() - 5);
    int Id = Integer.valueOf(last4Str);

    PendingIntent alarmIntent = PendingIntent.getBroadcast(this, Id, intent, 0);

    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    cal.set(Calendar.MONTH,month); // month - 1
    cal.set(Calendar.YEAR,year);
    cal.set(Calendar.DAY_OF_MONTH,day);
    cal.set(Calendar.HOUR_OF_DAY,h);
    cal.set(Calendar.MINUTE,min);
    cal.set(Calendar.SECOND,0);
    alarmMgr.set(AlarmManager.RTC, cal.getTimeInMillis(), alarmIntent);
  }

  public static String httpCall(String url) {
    // string buffers the url
    StringBuffer buffer_string = new StringBuffer(url);
    String replyString = "";

    // instanciate an HttpClient
    HttpClient httpclient = new DefaultHttpClient();
    // instanciate an HttpGet
    HttpGet httpget = new HttpGet(buffer_string.toString());

  try {
    // get the responce of the httpclient execution of the url
    HttpResponse response = httpclient.execute(httpget);
    InputStream is = response.getEntity().getContent();

    // buffer input stream the result
    BufferedInputStream bis = new BufferedInputStream(is);
    ByteArrayBuffer baf = new ByteArrayBuffer(20);
    int current = 0;
    while ((current = bis.read()) != -1) {
      baf.append((byte) current);
    }
    // the result as a string is ready for parsing
    replyString = new String(baf.toByteArray());
    } catch (Exception e) {
    e.printStackTrace();
      }
    // trim the whitespaces
    return replyString.trim();
  }

  // GET JOURNEY DETAIL LINK JSON
  private static String parseDbDeparture(final String response, String ICE_parameter) {
    String dbDeparture = "";
    try {
      // make an jsonObject in order to parse the response
      JSONObject jsonObject = new JSONObject(response);

      if (jsonObject.has("DepartureBoard")) {
        if (jsonObject.getJSONObject("DepartureBoard").has("Departure")) {
          JSONArray departureArray = jsonObject.getJSONObject("DepartureBoard").getJSONArray("Departure");
          for (int i = 0; i < departureArray.length(); i++) {
            if (departureArray.getJSONObject(i).getString("name").equals(ICE_parameter)) {
              dbDeparture = departureArray.getJSONObject(i).getJSONObject("JourneyDetailRef").getString("ref");
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return dbDeparture;
    }
    return dbDeparture;
  }

  // GET JOURNEY
  public static JSONArray parseDbJourney(final String response) {
    JSONArray journeyStops = new JSONArray();
    try {
      // make an jsonObject in order to parse the response
      JSONObject jsonObject = new JSONObject(response);
      if (jsonObject.has("JourneyDetail")) {
        if (jsonObject.getJSONObject("JourneyDetail").has("Stops")) {
          journeyStops = jsonObject.getJSONObject("JourneyDetail").getJSONObject("Stops").getJSONArray("Stop");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return journeyStops;
    }
    return journeyStops;
  }
}

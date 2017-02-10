package dbchallenge.poi_app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class open_DestinationPoi extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_destination);

    String hotel1 = "http://www.kardorff.de/sites/www.kardorff.de/files/projectimages/intercity-hotel_kardorff-ingenieure_02.jpg?1372759965";
    String hotel2 = "http://ameron-hotel-abion-spreebogen-berlin.hotel-in-berlin.org/data/Photos/OriginalPhoto/2987/298745/298745904.JPEG";
    String restaurant = "http://www.noz-cdn.de/media/2016/02/04/hans-im-glueck-burgergrill-ambiente-2_201602042029_full.jpg";

    URL newurl = null;
    try {
      newurl = new URL(hotel1);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Bitmap mIcon_val;
    try {
      mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
      ImageView img= (ImageView) findViewById(R.id.firstHotel);
      img.setImageBitmap(mIcon_val);
    } catch (IOException e) {
      e.printStackTrace();
    }

    URL newurl2 = null;
    try {
      newurl2 = new URL(hotel2);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Bitmap mIcon_val2;
    try {
      mIcon_val2 = BitmapFactory.decodeStream(newurl2.openConnection().getInputStream());
      ImageView img= (ImageView) findViewById(R.id.secondHotel);
      img.setImageBitmap(mIcon_val2);
    } catch (IOException e) {
      e.printStackTrace();
    }
    URL newurl3 = null;
    try {
      newurl3 = new URL(restaurant);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Bitmap mIcon_val3;
    try {
      mIcon_val3 = BitmapFactory.decodeStream(newurl3.openConnection().getInputStream());
      ImageView img= (ImageView) findViewById(R.id.restaurant);
      img.setImageBitmap(mIcon_val3);
    } catch (IOException e) {
      e.printStackTrace();
    }

    final TextView tip = (TextView) findViewById(R.id.textView24);
    tip.setText("https://hansimglueck-burgergrill.de/standorte/berlin-hauptbahnhof/");
  }
}

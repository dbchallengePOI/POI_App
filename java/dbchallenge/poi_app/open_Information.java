package dbchallenge.poi_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class open_Information extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poi_information);

    Intent intent = getIntent();
    String poiName = intent.getStringExtra("name");
    String poiLocation = intent.getStringExtra("location");
    String poiTips = intent.getStringExtra("tips");
    String imgurl = intent.getStringExtra("imgurl");

    URL newurl = null;

    try {
      newurl = new URL(imgurl);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Bitmap mIcon_val;
    try {
      mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
      ImageView img= (ImageView) findViewById(R.id.imageView5);
      img.setImageBitmap(mIcon_val);
    } catch (IOException e) {
      e.printStackTrace();
    }

    final TextView name = (TextView) findViewById(R.id.POI_NAME);
    final TextView tip = (TextView) findViewById(R.id.POI_TIP);
    final TextView adress = (TextView) findViewById(R.id.POI_ADRESS);
    name.setText(poiName);
    tip.setText(poiTips);
    adress.setText(poiLocation);
  }
}

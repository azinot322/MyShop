package com.example.myshop.Activities.ItemsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.os.AsyncTask;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myshop.R;
import com.example.myshop.apiConnection.SendGridEmailSender;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class BoughtActivity extends AppCompatActivity {

    private MapView mapView;
    private String postalCode;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("2de28404-7141-4d9a-bced-9e1dc4751b03");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_bought);
        super.onCreate(savedInstanceState);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.getMap().move(new CameraPosition(new Point(0, 0), 14.0f, 0.0f, 0.0f));

        Intent intent = getIntent();
        postalCode = intent.getStringExtra("zip");


        new GetLocation().execute();


        button = findViewById(R.id.button_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String phone = intent.getStringExtra("phone");
                String email = intent.getStringExtra("email");
                String name = intent.getStringExtra("name");
                String price = intent.getStringExtra("price");
                String subject = "Purchase Confirmation";
                String message = "Здравствуйте вы купили " + name + ", за " + price + ". Оно придет вам на почту с индексом " + postalCode + ". Мы свяжемся с вами по указанному вами телефону " + phone;
                Intent intent1 = new Intent(BoughtActivity.this, SadActivity.class);
                intent1.putExtra("info",message);
                startActivity(intent1);
            }
        });
    }

    private class GetLocation extends AsyncTask<Void, Void, Point> {
        @Override
        protected Point doInBackground(Void... voids) {
            String apiKey = "4060c33a5ddf4ee5ac7fc67c51cc4984";
            String requestUrl = "https://api.opencagedata.com/geocode/v1/json?q=" + postalCode + "&key=" + apiKey;
            try {
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);
                JSONObject geometry = results.getJSONObject("geometry");

                double latitude = geometry.getDouble("lat");
                double longitude = geometry.getDouble("lng");

                return new Point(latitude, longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Point point) {
            super.onPostExecute(point);
            if (point != null) {
                mapView.getMap().move(new CameraPosition(point, 14.0f, 0.0f, 0.0f));
                MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
                Point textpoint = new Point(point.getLatitude()+0.0001,point.getLongitude()+0.0001);
                PlacemarkMapObject placemark = mapObjects.addPlacemark(point);
                PlacemarkMapObject placemark2 = mapObjects.addPlacemark(textpoint);
                placemark.setZIndex(100);
                placemark2.setText("Доставим сюда");
                placemark2.setOpacity(0.8f);

            }
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    private class SendEmailTask extends AsyncTask<Void, Void, Void> {
        String email;
        String subject;
        String message;

        public SendEmailTask(String email, String subject, String message) {
            this.email = email;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SendGridEmailSender sender = new SendGridEmailSender();
            sender.sendEmail(email, subject, message);
            return null;
        }
    }
}
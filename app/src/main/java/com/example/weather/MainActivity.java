package com.example.weather;

import static android.content.ContentValues.TAG;

import static androidx.test.InstrumentationRegistry.getContext;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.weather.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weather.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://weather-72c51-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference mDatabase = firebaseDatabase.getReference();

    TextView temperature;
    TextView city;
    TextView cloud;
    TextView maxTemp;
    TextView minTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);


        System.out.println(SHA1(this));

        HeConfig.init("HE2212040706301915", "6cfe845420684900b25cf5cae95388e2");
        HeConfig.switchToDevService();

        temperature = findViewById(R.id.temperature);
        cloud = findViewById(R.id.cloud);
        maxTemp = findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);
        city = findViewById(R.id.city);
        QWeather.getGeoCityLookup(this, "Horsens", new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "Weather Error: " + throwable.getMessage());
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                System.out.println(geoBean.getLocationBean().get(0).getId()+"klkkkkkkkkkkkkkkkkkkkkkkk");
                QWeather.getWeatherNow(getApplicationContext(), geoBean.getLocationBean().get(0).getId(), Lang.EN, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "Weather Error: " + e);
                    }

                    @SuppressLint("SetTextI18n")

                    @Override
                    public void onSuccess(WeatherNowBean weatherBean) {
                        Log.i(TAG, "Weather Success: " + new Gson().toJson(weatherBean.getNow()));
                        if (Code.OK == weatherBean.getCode()) {
                            WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                            temperature.setText(now.getTemp()+"\u2103");
                            cloud.setText(now.getText());

                        } else {

                            Code code = weatherBean.getCode();
                            Log.i(TAG, "failed code: " + code);
                        }
                    }
                });

                QWeather.getWeather3D(getApplicationContext(), geoBean.getLocationBean().get(0).getId(), Lang.EN,Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "Weather Error: " + throwable.getMessage());
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(WeatherDailyBean weatherDailyBean) {
                        if (Code.OK == weatherDailyBean.getCode()) {

                            maxTemp.setText("Highest: " + weatherDailyBean.getDaily().get(0).getTempMax()+"\u2103" + " °C");
                            minTemp.setText("Lowest: " + weatherDailyBean.getDaily().get(0).getTempMin()+"\u2103" + " °C");
                        } else {

                            Code code = weatherDailyBean.getCode();
                            Log.i(TAG, "failed code: " + code);
                        }
                    }
                });
            }
        });
    }

    public static String SHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
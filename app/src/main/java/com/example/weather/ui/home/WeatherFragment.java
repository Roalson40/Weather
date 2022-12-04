package com.example.weather.ui.home;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.weather.R;
import com.example.weather.databinding.FragmentHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

public class WeatherFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://weather-72c51-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference mDatabase = firebaseDatabase.getReference();

    TextView temperature;
    TextView city;
    TextView cloud;
    ProgressDialog load;
    TextView maxTemp;
    TextView minTemp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        temperature = root.findViewById(R.id.temperature);
        cloud = root.findViewById(R.id.cloud);
        maxTemp = root.findViewById(R.id.maxTemp);
        minTemp = root.findViewById(R.id.minTemp);
        city = root.findViewById(R.id.city);
        QWeather.getGeoCityLookup(getContext(), "Horsens", new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "Weather Error: " + throwable.getMessage());
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                System.out.println(geoBean.getLocationBean().get(0).getId()+"klkkkkkkkkkkkkkkkkkkkkkkk");
                QWeather.getWeatherNow(getContext(), geoBean.getLocationBean().get(0).getId(), Lang.EN, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
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

                QWeather.getWeather3D(getContext(), geoBean.getLocationBean().get(0).getId(), Lang.EN,Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        load.dismiss();
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


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.islandbuilder;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;


public class FragmentDisplay extends Fragment {

    private GameData gameData;
    private Settings settings;

    private TextView name, time, money, income, population, employmentRate, temperature;
    private Button demolishButton, increaseTime, details;

    private boolean demolish = false;
    private boolean detail = false;
    private int incomeVal, pop;
    private double empRate;
    private double temp;

    //api.openweathermap.org/data/2.5/weather?id={2063523}&appid={ceeca8720cd43098b47df094bbe97f2f};
    private final String tempUrl = Uri.parse("https://api.openweathermap.org/data/2.5/weather?id=2063523&appid=ceeca8720cd43098b47df094bbe97f2f&units=metric").toString();


    public FragmentDisplay() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        gameData = GameData.getInstance(getActivity());

        //For a new game, force any possible change to InitMoney to take effect.
        if (!gameData.isContinuing()) {
            gameData.resetMoney();
        }
        settings = gameData.getSettings();

        name = view.findViewById(R.id.island_name);
        time = view.findViewById(R.id.time);
        money = view.findViewById(R.id.money);
        income = view.findViewById(R.id.income);
        population = view.findViewById(R.id.population);
        employmentRate = view.findViewById(R.id.employment_rate);
        temperature = view.findViewById(R.id.temperature);
        demolishButton = view.findViewById(R.id.demolish);
        increaseTime = view.findViewById(R.id.next_time);
        details = view.findViewById(R.id.details);

        name.setText(settings.getName());

        //In case resuming game,
        if (gameData.getGameTime() > 0) {
            calculateValues();
            incomeVal = (int) ((pop * (empRate * settings.getSalary() * settings.getTaxRate() - settings.getServiceCost())));
            setValues();
        } else {
            updateView();
        }

        increaseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateValues();
            }
        });

        demolishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demolish = true;
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail = true;
            }
        });

        return view;
    }

    private void updateValues() {
        calculateValues();
        incomeVal = (int) ((pop * (empRate * settings.getSalary() * settings.getTaxRate() - settings.getServiceCost())));
         if (!gameData.transaction(incomeVal)) {
             //Set money to 0
             gameData.transaction(-gameData.getMoney());
             GameData.displayPopUp("You ran out of money, you lose!",getActivity());
         }
         gameData.nextDay();


        setValues();
    }

    private void calculateValues() {
        pop = gameData.getNumResidential() * settings.getFamilySize();
        if (pop == 0) {
            empRate = 0.0;
        } else {
            empRate = Math.min(1.0, gameData.getNumCommercial() * settings.getShopSize() / ((double) pop));
        }
        updateTemp();
    }

    private void setValues() {
        time.setText("Day " + gameData.getGameTime());
        money.setText("Cash: $" + gameData.getMoney());
        if (incomeVal < 0) {
            income.setText("Prev income: -$" + -incomeVal);
        } else {
            income.setText("Prev income: +$" + incomeVal);
        }
        population.setText("Population: " + pop);

        if (pop == 0) {
            employmentRate.setText("Employment rate: " + "undefined");
        } else {
            employmentRate.setText("Employment rate: " + String.format("%.0f", empRate * 100) + "%");
        }
    }

    //Can be called each time a new structure is built/demolished.
    public void updateView() {
        calculateValues();
        setValues();
    }

    public boolean isDemolish() {
        return demolish;
    }

    public void setDemolish(boolean demolish) {
        this.demolish = demolish;
    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }


    private void updateTemp() {
        try {
            new updateTemperature().execute(new URL(tempUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class updateTemperature extends AsyncTask<URL, Void, String>
    {

        @Override
        protected void onPostExecute(String result) {
            //String displayText = "";
            try {
                Log.i(TAG, "onPostExecute: JSON RESULT:\n\n" + result + "\n\n");
                JSONObject jBase = new JSONObject(result);

                JSONObject main = jBase.getJSONObject("main");
                temp = main.getDouble("temp");
                //Update here since its running at the same time, the temp display could be updated b4 this is finished. Therefore we just update it here when we've got the value.
                temperature.setText(temp + "\u00B0C");
                Log.i(TAG, "onPostExecute: TEMPERATURE UPDATED TO: " + temp);

            }catch (JSONException e) {
                Log.e(TAG, "onPostExecute: JSON COULDN'T BE PARSED");
            }

        }

        @Override
        protected String doInBackground(URL... urls) {
            String result = null;
            HttpsURLConnection conn = null;

            //set connection
            try {
                conn = (HttpsURLConnection)urls[0].openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    throw new IOException("connection response was not ok. Response was: " + conn.getResponseCode());
                }
                InputStream is= null;
                //get inputStream
                try {
                    is = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];

                int bytesRead= 0;
                try {
                    bytesRead = is.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while(bytesRead > 0)
                {
                    baos.write(buffer, 0,bytesRead);
                    try {
                        bytesRead=is.read(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = new String(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }

            return result;
        }

    }

}
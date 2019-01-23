package com.apdallahy3.marvelcharcters.Views.AsanycTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.Models.Item;
import com.apdallahy3.marvelcharcters.Utls.Constants;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnItemsLoaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class LoadItemsDataTask extends AsyncTask<String,Void,ArrayList<Item>> {
    private OnItemsLoaded onDataLoadedInstance;
    private Activity context;

    public LoadItemsDataTask(OnItemsLoaded onDataLoadedInstance, Activity context) {
        this.onDataLoadedInstance = onDataLoadedInstance;
        this.context=context;
     }

    @Override
    protected ArrayList<Item> doInBackground(String... params) {
            String URL = Constants.API_URL+"/"+params[0]+"/"+params[1]
                    +"?apikey="+ Constants.API_KEY_PUBLIC
                    +"&hash="  + Constants.API_HASH
                    +"&ts="    + Constants.API_TIMESTAMP;


            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String charactersJSonString = null;
            try {
                URL url = new URL(URL);
                //Connect to the api
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //open input stream and read Json data
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                charactersJSonString = buffer.toString();
                Log.i("REspose",charactersJSonString);
            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", URL);

                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e("ProtocolException", URL);

                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOECxption", URL);
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
                //convert json string to list of characters items if json string not empty
            if (charactersJSonString != null && !charactersJSonString.isEmpty())
                return Controller.getContollerInstance().getItemsDataFromJson(charactersJSonString);
            else
                return new ArrayList<Item>();


    }

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
              onDataLoadedInstance.onItemsLoaded(items);

        super.onPostExecute(items);
    }



}

package com.apdallahy3.marvelcharcters.Views.AsanycTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.Models.ChracterItem;
import com.apdallahy3.marvelcharcters.Utls.Constants;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnDataLoaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CharactersTask extends AsyncTask<String,Void,ArrayList<ChracterItem>> {
    private OnDataLoaded onDataLoadedInstance;
    private Activity context;
    ProgressDialog progressDialog;

    public CharactersTask(OnDataLoaded onDataLoadedInstance,Activity context) {
        this.onDataLoadedInstance = onDataLoadedInstance;
        this.context=context;
        progressDialog=new ProgressDialog(context);
    }

    @Override
    protected ArrayList<ChracterItem> doInBackground(String... params) {
        progressDialog.setMessage("Loading");
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();

            }
        });
            String URL = Constants.API_URL
                    +"?apikey="+ Constants.API_KEY_PUBLIC
                    +"&hash="  + Constants.API_HASH
                    +"&ts="    + Constants.API_TIMESTAMP;
                if(params[0]!=null&&!params[0].isEmpty()){
                    URL+="&offset="+params[0];
                }

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
                return Controller.getContollerInstance().getCharctersFromJson(charactersJSonString);
            else
                return new ArrayList<ChracterItem>();


    }

    @Override
    protected void onPostExecute(ArrayList<ChracterItem> chracterItems) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();

            }
        });
             onDataLoadedInstance.onDataLoaded(chracterItems,true);

        super.onPostExecute(chracterItems);
    }



}

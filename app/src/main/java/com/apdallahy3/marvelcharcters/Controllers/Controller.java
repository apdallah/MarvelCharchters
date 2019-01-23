package com.apdallahy3.marvelcharcters.Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.apdallahy3.marvelcharcters.DataBase.ChractersDbHelper;
import com.apdallahy3.marvelcharcters.Models.ChracterItem;
import com.apdallahy3.marvelcharcters.Models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Controller {
    private static Controller mController=null;
    //create singletone instance of controller class
    public static Controller getContollerInstance(){
        if(mController==null){
            mController=new Controller();
        }
        return mController;
    }
    public ArrayList<ChracterItem> getCharctersFromJson(String characterJsonString,Context context) {
        ArrayList<ChracterItem> charactersItems = new ArrayList<ChracterItem>();
        try {
            //convert string to json object
            JSONObject moiveJsonObj = new JSONObject(characterJsonString);
            //get array of results
            JSONArray moiveJsonArr = moiveJsonObj.getJSONObject("data").getJSONArray("results");
            //convert json array objects to characters array Items
            for (int i = 0; i < moiveJsonArr.length(); i++) {
                ChracterItem item = new ChracterItem();
                JSONObject jsonItem = moiveJsonArr.getJSONObject(i);
                item.setName(jsonItem.getString("name"));
                item.setDescription(jsonItem.getString("description"));
                item.setId(jsonItem.getInt("id"));
                item.setThumbnailUrl(jsonItem.getJSONObject("thumbnail").getString("path"));
                charactersItems.add(item);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return charactersItems;
    }

    public ArrayList<Item> getItemsDataFromJson(String jsonString) {
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            //convert string to json object
            JSONObject moiveJsonObj = new JSONObject(jsonString);
            //get array of results
            JSONArray moiveJsonArr = moiveJsonObj.getJSONObject("data").getJSONArray("results");
            //convert json array objects to characters array Items
            for (int i = 0; i < moiveJsonArr.length(); i++) {
                Item item = new Item();
                JSONObject jsonItem = moiveJsonArr.getJSONObject(i);
                item.setName(jsonItem.getString("title"));
                item.setId(jsonItem.getInt("id"));
                item.setThumbinalUrl(jsonItem.getJSONObject("thumbnail").getString("path"));
                 items.add(item);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    public boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public Bitmap downloadImageFromUrl(String Url){
        Bitmap downoadedBitmap=null;

        try {
            //initialize input stream from url
            final InputStream inputStream = new URL(Url).openStream();
            //decode bitmap from inputstream using bitmap factory
            downoadedBitmap=BitmapFactory.decodeStream(inputStream);
            Log.i("download",downoadedBitmap.getByteCount()+"");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return downoadedBitmap;
    }
    public String saveBitmapToStorage(Bitmap bitmap,long id ){
        String savedPath="";
        //get cache dirctory
        final String root=Environment.getExternalStorageDirectory().toString();
        //Create new file
        File dir=new File(root+"/marvel/");
        if(!dir.exists()){
            dir.mkdir();
        }
        Log.i("Dir",dir.toString());
        File saveFile=new File(dir,"/Image_"+id+".jpg");

        if(!saveFile.exists()){
            try {
                //create output stream
                FileOutputStream outputStream=new FileOutputStream(saveFile);
                //compress bitmap to outputstream
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                //flush outputstream and write bitmap
                outputStream.flush();
                outputStream.close();
                savedPath=saveFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return savedPath;
    }

    public String saveBitmapToStorage(Bitmap bitmap,int id ,String type){
        String savedPath="";
        //get Storage dirctory
        final String root=Environment.getExternalStorageDirectory().toString();
        //Create new Dir if not exist
        File dir=new File(root+"/marvel/"+type);
        if(!dir.exists()){
            dir.mkdir();
        }
        //create new file
         File saveFile=new File(dir,"/Image_"+id+".jpg");

        if(!saveFile.exists()){
            try {
                //create output stream
                FileOutputStream outputStream=new FileOutputStream(saveFile);
                //compress bitmap to outputstream
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                //flush outputstream and write bitmap
                outputStream.flush();
                outputStream.close();
                savedPath=saveFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            savedPath=saveFile.toString();
        }

        return savedPath;
    }

    public String loadPerfrance(String key,String defaultVal,Context context){
        String value="";
        SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(context);
        value = sharedPref.getString(key, defaultVal);
        return value;
    }
    public void savePrefrance(String key,String value,Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();

    }
}

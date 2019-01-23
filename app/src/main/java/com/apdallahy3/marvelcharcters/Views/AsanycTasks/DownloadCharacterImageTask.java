package com.apdallahy3.marvelcharcters.Views.AsanycTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnImageDownLoaded;

public class DownloadCharacterImageTask extends AsyncTask<String,Void,Bitmap> {
OnImageDownLoaded onImageDownLoaded;

    public DownloadCharacterImageTask(OnImageDownLoaded onImageDownLoaded) {
        this.onImageDownLoaded = onImageDownLoaded;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {



        return  Controller.getContollerInstance().downloadImageFromUrl(
                strings[0]+"/"+strings[1]+".jpg");
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        onImageDownLoaded.onImageDownloaded(bitmap);
        super.onPostExecute(bitmap);
    }

}

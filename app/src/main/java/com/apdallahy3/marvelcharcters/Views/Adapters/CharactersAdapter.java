package com.apdallahy3.marvelcharcters.Views.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.DataBase.ChractersDbHelper;
import com.apdallahy3.marvelcharcters.Models.ChracterItem;
import com.apdallahy3.marvelcharcters.R;
import com.apdallahy3.marvelcharcters.Views.AsanycTasks.DownloadCharacterImageTask;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnImageDownLoaded;

import java.io.File;
import java.util.ArrayList;

public class CharactersAdapter extends BaseAdapter {

    Activity context;
    ArrayList<ChracterItem> chracterItemArrayList=new ArrayList<ChracterItem>();

    public CharactersAdapter(Activity context) {
        this.context = context;
     }

    public void setChracterItemArrayList(ArrayList<ChracterItem> chracterItemArrayList) {

            this.chracterItemArrayList.addAll(chracterItemArrayList);

    }

    @Override
    public int getCount() {

        return chracterItemArrayList.size();
    }

    @Override
    public ChracterItem getItem(int position) {
        return chracterItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    static class ViewHolder{
            ImageView chrachterImage;
            TextView charcterName;
            FrameLayout layout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder = new ViewHolder();
        /*
            check if view created before if true set tag on it to use it again
            and prevent listview from creating new views that causes outOfMemoryExpection
        */

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.charcter_list_item, parent, false);
            viewHolder.chrachterImage = (ImageView) convertView.findViewById(R.id.character_image);
            viewHolder.charcterName = (TextView) convertView.findViewById(R.id.character_name);
            viewHolder.layout = (FrameLayout) convertView.findViewById(R.id.list_item);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.i("Adapter",getItem(position).getName()+"");

        //Setting list item  view data
        viewHolder.charcterName.setText(getItem(position).getName());
        //check if thumbinal  donwloaded before if true set it set it to image view
        File file=new File(Environment.getExternalStorageDirectory()+"/marvel/"
                 +"Image_"+getItem(position).getId()+".jpg");
        if(file.exists()){
            viewHolder.chrachterImage.setImageBitmap(BitmapFactory.decodeFile(file.toString()));
        }else if(Controller.getContollerInstance().isNetworkConnected(context)){

            viewHolder.chrachterImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
            final ViewHolder finalViewHolder = viewHolder;
            new DownloadCharacterImageTask(new OnImageDownLoaded() {
                @Override
                public void onImageDownloaded(final Bitmap bitmap) {
                    if(bitmap!=null) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finalViewHolder.chrachterImage.setImageBitmap(bitmap);
                            }
                        });
                        //save bitmap to application cache
                        if(Controller.getContollerInstance().loadPerfrance("write_storage","false",context).equals("true")){
                             Controller.getContollerInstance().saveBitmapToStorage(bitmap,getItem(position).getId());

                         }
                    }else{
                        Log.i("imagedownloaded","Probelm");
                    }
                }
            }).execute(getItem(position).getThumbnailUrl(),"landscape_incredible");
        }

        return convertView;
    }

}

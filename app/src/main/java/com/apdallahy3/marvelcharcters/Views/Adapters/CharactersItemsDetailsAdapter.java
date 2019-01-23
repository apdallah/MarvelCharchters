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
import android.widget.ImageView;
import android.widget.TextView;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.Models.Item;
import com.apdallahy3.marvelcharcters.R;
import com.apdallahy3.marvelcharcters.Views.AsanycTasks.DownloadCharacterImageTask;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnImageDownLoaded;

import java.io.File;
import java.util.ArrayList;

public class CharactersItemsDetailsAdapter extends BaseAdapter {

    Activity context;
    ArrayList<Item> items=new ArrayList<Item>();
    String type;
    public CharactersItemsDetailsAdapter(Activity context,String type) {

        this.type=type;
        this.context = context;
     }

    public void setItems(ArrayList<Item> itmes) {

            this.items.addAll(itmes);

    }

    @Override
    public int getCount() {
        Log.i("getCount",items.size()+"");
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    static class ViewHolder{
            ImageView photo;
            TextView name;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder = new ViewHolder();
        /*
            check if view created before if true set tag on it to use it again
            and prevent listview from creating new views that causes outOfMemoryExpection
        */

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.i("DetailsAdapter",getItem(position).getName()+"");

        //Setting list item  view data
        viewHolder.name.setText(getItem(position).getName());
        //check if thumbinal  donwloaded before if true set it set it to image view
        //check if file stored on localstorage
        File file=new File(Environment.getExternalStorageDirectory()+"/marvel/"
                +getItem(position).getType()
                +"/Image_"+getItem(position).getId()+".jpg");
        if(file.exists()){
            viewHolder.photo.setImageBitmap(BitmapFactory.decodeFile(file.toString()));
        }else if(Controller.getContollerInstance().isNetworkConnected(context)){
                    final ViewHolder finalViewHolder = viewHolder;
            viewHolder.photo.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
            new DownloadCharacterImageTask(new OnImageDownLoaded() {
                        @Override
                        public void onImageDownloaded(final Bitmap bitmap) {
                            if(bitmap!=null) {
                                //set bitmap to image view in viewholder
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finalViewHolder.photo.setImageBitmap(bitmap);
                                        }
                                        });
                                //save bitmap to application cache if we have permission to write_external Storage
                                if(Controller.getContollerInstance().loadPerfrance("write_storage","false",context).equals("true"))
                                    Controller.getContollerInstance().saveBitmapToStorage(bitmap,getItem(position).getId(),type);

                            }else{
                                Log.i("imagedownloaded","Probelm");
                            }
                        }
                    }).execute(getItem(position).getThumbinalUrl(),"standard_amazing");

        }
        return convertView;
    }

}

package com.apdallahy3.marvelcharcters.Views.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.DataBase.ChractersDbHelper;
import com.apdallahy3.marvelcharcters.Models.ChracterItem;
import com.apdallahy3.marvelcharcters.Models.Item;
import com.apdallahy3.marvelcharcters.R;
import com.apdallahy3.marvelcharcters.Views.Adapters.CharactersItemsDetailsAdapter;
import com.apdallahy3.marvelcharcters.Views.AsanycTasks.DownloadCharacterImageTask;
import com.apdallahy3.marvelcharcters.Views.AsanycTasks.LoadItemsDataTask;
import com.apdallahy3.marvelcharcters.Views.HorizontalListView;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnImageDownLoaded;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnItemsLoaded;

import java.io.File;
import java.util.ArrayList;

public class ActivityDetails extends AppCompatActivity {
    TextView name,description;
    ImageView photo;
    ChracterItem selectedItem;
    com.apdallahy3.marvelcharcters.Views.HorizontalListView eventsListview,comicsListView,seriesListView;
    CharactersItemsDetailsAdapter eventsAdapter,comicsAdapter,seriesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);
         name=(TextView)findViewById(R.id.character_name);
        description=(TextView)findViewById(R.id.character_description);
        photo=(ImageView) findViewById(R.id.cover_photo);

        //init listviews
        comicsListView=(HorizontalListView) findViewById(R.id.comics_list_view);
        eventsListview=(HorizontalListView) findViewById(R.id.events_list_view);
        seriesListView=(HorizontalListView) findViewById(R.id.series_list_view);
        //init adapters
        eventsAdapter=new CharactersItemsDetailsAdapter(this,"events");
        comicsAdapter=new CharactersItemsDetailsAdapter(this,"comics");
        seriesAdapter=new CharactersItemsDetailsAdapter(this,"series");
        //set adapters
        comicsListView.setAdapter(comicsAdapter);
        eventsListview.setAdapter(eventsAdapter);
        seriesListView.setAdapter(seriesAdapter);

        if(getIntent().getExtras()!=null){
            selectedItem=(ChracterItem)getIntent().getExtras().getSerializable("item");
            name.setText(selectedItem.getName());
            description.setText(selectedItem.getDescription());
            File file=new File(Environment.getExternalStorageDirectory()+"/marvel/Image_"+selectedItem.getId()+".jpg");
            if(file.exists()) {
                photo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }else{

                new DownloadCharacterImageTask(new OnImageDownLoaded() {
                    @Override
                    public void onImageDownloaded(final Bitmap bitmap) {

                        if(bitmap!=null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    photo.setImageBitmap(bitmap);
                                }
                            });

                            //save bitmap to application cache
                            if(Controller.getContollerInstance().loadPerfrance("write_storage","false",ActivityDetails.this).equals("true")){
                                  Controller.getContollerInstance().saveBitmapToStorage(bitmap,selectedItem.getId());
                            }
                        }else{
                            Log.i("imagedownloaded","Probelm");
                        }

                    }
                }).execute(selectedItem.getThumbnailUrl(),"landscape_incredible");
            }
            //Load Events Data
            if(Controller.getContollerInstance().isNetworkConnected(this)){
                            new LoadItemsDataTask(new OnItemsLoaded() {
                                @Override
                                public void onItemsLoaded(ArrayList<Item> loadedItems) {
                                    Log.i("Events",loadedItems.size()+"");
                                    //Cache Results to database
                                    for(int i=0;i<loadedItems.size();i++){
                                        if(!ChractersDbHelper.getChractersDbHelperInstance(ActivityDetails.this).isDetialsItemExist(loadedItems.get(i).getId(),"events")) {
                                            loadedItems.get(i).setType("events");
                                            loadedItems.get(i).setCharacter_id(selectedItem.getId());

                                            ChractersDbHelper.getChractersDbHelperInstance(ActivityDetails.this).addCharacterDetailsItem(loadedItems.get(i));

                                        }
                                    }
                                    eventsAdapter.setItems(loadedItems);
                                    eventsAdapter.notifyDataSetChanged();
                                }
                            }, ActivityDetails.this).execute(selectedItem.getId()+"","events");

                            //Load Comics Data
                            new LoadItemsDataTask(new OnItemsLoaded() {
                                @Override
                                public void onItemsLoaded(ArrayList<Item> loadedItems) {
                                     //Cache Results to database
                                    for(int i=0;i<loadedItems.size();i++){
                                        if(!ChractersDbHelper.getChractersDbHelperInstance(ActivityDetails.this).isDetialsItemExist(loadedItems.get(i).getId(),"comics")) {
                                            loadedItems.get(i).setType("comics");
                                            loadedItems.get(i).setCharacter_id(selectedItem.getId());
                                            ChractersDbHelper.getChractersDbHelperInstance(ActivityDetails.this).addCharacterDetailsItem(loadedItems.get(i));

                                        }
                                    }
                                    comicsAdapter.setItems(loadedItems);
                                    comicsAdapter.notifyDataSetChanged();
                                }
                            }, ActivityDetails.this).execute(selectedItem.getId()+"","comics");
                            //Load Series Data
                            new LoadItemsDataTask(new OnItemsLoaded() {
                                @Override
                                public void onItemsLoaded(ArrayList<Item> loadedItems) {
                                     //Cache Results to database
                                    for(int i=0;i<loadedItems.size();i++){
                                        if(!ChractersDbHelper.getChractersDbHelperInstance(ActivityDetails.this).isDetialsItemExist(loadedItems.get(i).getId(),"series")) {
                                            loadedItems.get(i).setType("series");
                                            loadedItems.get(i).setCharacter_id(selectedItem.getId());
                                            ChractersDbHelper.getChractersDbHelperInstance(ActivityDetails.this).addCharacterDetailsItem(loadedItems.get(i));

                                        }
                                    }
                                    seriesAdapter.setItems(loadedItems);
                                    seriesAdapter.notifyDataSetChanged();
                                }
                            }, ActivityDetails.this).execute(selectedItem.getId()+"","series");
            }else{
                //Load Character Events from Db
                eventsAdapter.setItems(ChractersDbHelper.getChractersDbHelperInstance(this).getCharacterDetailsItems(selectedItem.getId(),"events"));
                eventsAdapter.notifyDataSetChanged();

                //Load Character Comics from Db
                comicsAdapter.setItems(ChractersDbHelper.getChractersDbHelperInstance(this).getCharacterDetailsItems(selectedItem.getId(),"comics"));
                comicsAdapter.notifyDataSetChanged();

                //Load Character Series from Db
                seriesAdapter.setItems(ChractersDbHelper.getChractersDbHelperInstance(this).getCharacterDetailsItems(selectedItem.getId(),"series"));
                seriesAdapter.notifyDataSetChanged();
            }
        }
    }

}

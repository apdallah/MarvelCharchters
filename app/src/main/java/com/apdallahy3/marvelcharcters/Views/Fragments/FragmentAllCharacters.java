package com.apdallahy3.marvelcharcters.Views.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.DataBase.ChractersDbHelper;
import com.apdallahy3.marvelcharcters.Models.ChracterItem;
import com.apdallahy3.marvelcharcters.R;
import com.apdallahy3.marvelcharcters.Views.Activities.ActivityDetails;
import com.apdallahy3.marvelcharcters.Views.Activities.MainActivity;
import com.apdallahy3.marvelcharcters.Views.Adapters.CharactersAdapter;
import com.apdallahy3.marvelcharcters.Views.AsanycTasks.CharactersTask;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnDataLoaded;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAllCharacters extends Fragment implements OnDataLoaded {


    private ListView charchtersListView;
     private CharactersAdapter charactersAdapter;
    private int preLast;
    //to get supportd fragmnt manager use fragment activity
    private FragmentActivity fragmentActivity;
    //inialize fragmentactivity on attach
    @Override
    public void onAttach(Activity activity) {
        this.fragmentActivity=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all_charcters, container, false);

        //init UI
             charchtersListView=(ListView)view.findViewById(R.id.charcters_list_view);
             charactersAdapter=new CharactersAdapter(getActivity());
             charchtersListView.setAdapter(charactersAdapter);
             //set on scroll listener so when list scrolled to the end we load more data
            charchtersListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if(lastItem == totalItemCount)
                    {
                        if(preLast!=lastItem)
                        {
                            //to avoid multiple calls for last item
                             preLast = lastItem;
                             //check internet connection otherwise get from database
                            if(Controller.getContollerInstance().isNetworkConnected(getActivity())){
                                //calling async task with on dataloaded listener and context and offset paramter
                                new CharactersTask(FragmentAllCharacters.this,getActivity()).execute(""+charactersAdapter.getCount());
                            }else{
                                //getting data from database sending offset paramter
                                ArrayList<ChracterItem> dbItems=ChractersDbHelper.getChractersDbHelperInstance(getActivity()).getAllCharacters(charactersAdapter.getCount());
                                charactersAdapter.setChracterItemArrayList(dbItems);
                                charactersAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }
            });
            charchtersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getActivity(),ActivityDetails.class);
                    intent.putExtra("item",charactersAdapter.getItem(position));
                    getActivity().startActivity(intent);
                }
            });
            //check if there is internet connection
             if(Controller.getContollerInstance().isNetworkConnected(getActivity())){
                 //loading data for the first time in application using offeset 0
                 new CharactersTask(this,getActivity()).execute("0");
             }else{
                //if there is not internet connection get data from database
                 ArrayList<ChracterItem> dbItems=ChractersDbHelper.getChractersDbHelperInstance(getActivity()).getAllCharacters(0);
                  charactersAdapter.setChracterItemArrayList(dbItems);
                 charactersAdapter.notifyDataSetChanged();
             }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        SearchView searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        if(searchView!=null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // if (!query.isEmpty())
                    // searchNews(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("well", " Search for :"+newText);
                    return false;
                }
            });
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDataLoaded(ArrayList<ChracterItem> loadedItems) {
        if(loadedItems.size()>0) {
            //set list to adapter after loading data
            charactersAdapter.setChracterItemArrayList(loadedItems);
            charactersAdapter.notifyDataSetChanged();
            //caching items to database if they are not already exists
            for(int i=0;i<loadedItems.size();i++){
                if(!ChractersDbHelper.getChractersDbHelperInstance(getActivity()).isExist(loadedItems.get(i).getId()))
                     ChractersDbHelper.getChractersDbHelperInstance(getActivity()).addCharacter(loadedItems.get(i));
            }
        }
    }
}

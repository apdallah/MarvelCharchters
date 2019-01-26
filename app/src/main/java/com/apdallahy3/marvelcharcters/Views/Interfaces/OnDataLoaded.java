package com.apdallahy3.marvelcharcters.Views.Interfaces;

import com.apdallahy3.marvelcharcters.Models.ChracterItem;

import java.util.ArrayList;

public  interface OnDataLoaded {
    public void onDataLoaded(ArrayList<ChracterItem> loadedItems,boolean cacheFlag);
}

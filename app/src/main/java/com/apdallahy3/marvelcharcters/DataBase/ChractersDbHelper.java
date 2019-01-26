package com.apdallahy3.marvelcharcters.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apdallahy3.marvelcharcters.Models.ChracterItem;
import com.apdallahy3.marvelcharcters.Models.Item;
import com.apdallahy3.marvelcharcters.Views.Interfaces.OnDataLoaded;

import java.util.ArrayList;

public class ChractersDbHelper extends SQLiteOpenHelper{
    //database name
    private static final String DATABASE_NAME = "characters.db";
    //database version
    private static final int DATABASE_VERSION = 2;
    //singletone instance
    private static ChractersDbHelper mInstance = null;
    //context
    private Context mContext;

    //Constructor
    private ChractersDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext=context;
    }
    //create the singletone Instance
    public static ChractersDbHelper getChractersDbHelperInstance(Context mContext){
        if (mInstance == null) {
            mInstance = new ChractersDbHelper(mContext.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create characters table Statment
        String SQL_CREATE_CHARACTERS_TABLE = "CREATE TABLE " + ChractersContract.CharactersEntry.TABLE_NAME + " (" +
                ChractersContract.CharactersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChractersContract.CharactersEntry.COLUMN_ID + " INTEGER unique NOT NULL, " +
                 ChractersContract.CharactersEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ChractersContract.CharactersEntry.COLUMN_THUMBINAL_URL + " TEXT NOT NULL, " +
                ChractersContract.CharactersEntry.COLUMN_THUMBINAL_PATH+ " TEXT, " +
                 ChractersContract.CharactersEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL" +
                ");";

        //create characters Details Items table Statment
        String SQL_CREATE_CHARACTERS_Details_ITem_TABLE = "CREATE TABLE " + ChractersContract.CharactersItemDetailsEntry.TABLE_NAME + " (" +
                ChractersContract.CharactersItemDetailsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChractersContract.CharactersItemDetailsEntry.COLUMN_ID + " INTEGER unique NOT NULL, " +
                ChractersContract.CharactersItemDetailsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ChractersContract.CharactersItemDetailsEntry.COLUMN_PHOTO_URL+ " TEXT, " +
                ChractersContract.CharactersItemDetailsEntry.COLUMN_PHOTO_PATH+ " TEXT, " +
                ChractersContract.CharactersItemDetailsEntry.COLUMN_TYPE+ " TEXT, " +
                ChractersContract.CharactersItemDetailsEntry.COLUMN_CHARACTER_ID + " INTEGER " +
                ");";

        //excute creating table statment
        db.execSQL(SQL_CREATE_CHARACTERS_TABLE);
        db.execSQL(SQL_CREATE_CHARACTERS_Details_ITem_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ChractersContract.CharactersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ChractersContract.CharactersItemDetailsEntry.TABLE_NAME);
        onCreate(db);
    }


    public ArrayList<ChracterItem> getAllCharacters(int offset){
        ArrayList<ChracterItem> chracterItems=new ArrayList<ChracterItem>();
        //get writable database instance
        SQLiteDatabase db = getReadableDatabase();
        //Query String
        String sql ="SELECT * FROM "+ChractersContract.CharactersEntry.TABLE_NAME+" LIMIT 20 OFFSET "+offset;

        //query database for all characters
        Cursor charcersCursor = db.rawQuery(sql, null);
        //make cursor begin with the first row
        charcersCursor.moveToFirst();
        for (int i = 0; i < charcersCursor.getCount(); i++) {
            //create character item
            ChracterItem item = new ChracterItem();
            //set item values from database
            item.setId(charcersCursor.getInt(charcersCursor.getColumnIndex(ChractersContract.CharactersEntry.COLUMN_ID)));
            item.setName(charcersCursor.getString(charcersCursor.getColumnIndex(ChractersContract.CharactersEntry.COLUMN_NAME)));
            item.setThumbnailUrl(charcersCursor.getString(charcersCursor.getColumnIndex(ChractersContract.CharactersEntry.COLUMN_THUMBINAL_URL)));
             item.setDescription(charcersCursor.getString(charcersCursor.getColumnIndex(ChractersContract.CharactersEntry.COLUMN_DESCRIPTION)));
            //add item to characters list
            chracterItems.add(item);
            //move cursor to the next row
            charcersCursor.moveToNext();
        }
        //close cursor after reading data
        charcersCursor.close();
        return chracterItems;
    }
    public ArrayList<Item> getCharacterDetailsItems(long character_id,String type){
        ArrayList<Item> items=new ArrayList<Item>();
        //get writable database instance
        SQLiteDatabase db = getReadableDatabase();
        //Query String
        String sql ="SELECT * FROM "+ChractersContract.CharactersItemDetailsEntry.TABLE_NAME+" WHERE "
                +ChractersContract.CharactersItemDetailsEntry.COLUMN_CHARACTER_ID+"="+character_id
                +" and "+ChractersContract.CharactersItemDetailsEntry.COLUMN_TYPE+"='"+type+"'";

        //query database for all characters
        Cursor cursor = db.rawQuery(sql, null);
        //make cursor begin with the first row
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            //create character item
            Item item = new Item();
            //set item values from database
            item.setId(cursor.getInt(cursor.getColumnIndex(ChractersContract.CharactersItemDetailsEntry.COLUMN_ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(ChractersContract.CharactersItemDetailsEntry.COLUMN_NAME)));
            item.setThumbinalUrl(cursor.getString(cursor.getColumnIndex(ChractersContract.CharactersItemDetailsEntry.COLUMN_PHOTO_URL)));
            item.setThumbinalPath(cursor.getString(cursor.getColumnIndex(ChractersContract.CharactersItemDetailsEntry.COLUMN_PHOTO_PATH)));
            item.setCharacter_id(cursor.getInt(cursor.getColumnIndex(ChractersContract.CharactersItemDetailsEntry.COLUMN_CHARACTER_ID)));
            item.setType(cursor.getString(cursor.getColumnIndex(ChractersContract.CharactersItemDetailsEntry.COLUMN_TYPE)));
            //add item to characters list
            items.add(item);
            //move cursor to the next row
            cursor.moveToNext();
        }
        //close cursor after reading data
        cursor.close();
        return items;
    }
    //Add Chracter item to database
    public long addCharacter(ChracterItem chracterItem){
        long added=0;
        //get writable database instance
        SQLiteDatabase db = getWritableDatabase();
        //create content values to insert to table
        ContentValues charachterValues = new ContentValues();
        charachterValues.put(ChractersContract.CharactersEntry.COLUMN_ID, chracterItem.getId());
        charachterValues.put(ChractersContract.CharactersEntry.COLUMN_NAME, chracterItem.getName());
        charachterValues.put(ChractersContract.CharactersEntry.COLUMN_THUMBINAL_URL, chracterItem.getThumbnailUrl());
         charachterValues.put(ChractersContract.CharactersEntry.COLUMN_DESCRIPTION, chracterItem.getDescription());
        //excute insert  to db
        try{
            added = db.insert(ChractersContract.CharactersEntry.TABLE_NAME, null, charachterValues);

        }catch(SQLiteConstraintException sqlconstrainExption){

            added=0;
        }

        return added;
    }
    //Add character details item to database
    public long addCharacterDetailsItem(Item detailsItem){
        long added=0;
        //get writable database instance
        SQLiteDatabase db = getWritableDatabase();
        //create content values to insert to table
        ContentValues itemDetailsValues = new ContentValues();
        itemDetailsValues.put(ChractersContract.CharactersItemDetailsEntry.COLUMN_ID, detailsItem.getId());
        itemDetailsValues.put(ChractersContract.CharactersItemDetailsEntry.COLUMN_NAME, detailsItem.getName());
        itemDetailsValues.put(ChractersContract.CharactersItemDetailsEntry.COLUMN_PHOTO_URL, detailsItem.getThumbinalUrl());
        itemDetailsValues.put(ChractersContract.CharactersItemDetailsEntry.COLUMN_PHOTO_PATH, detailsItem.getThumbinalPath());
        itemDetailsValues.put(ChractersContract.CharactersItemDetailsEntry.COLUMN_TYPE, detailsItem.getType());
        itemDetailsValues.put(ChractersContract.CharactersItemDetailsEntry.COLUMN_CHARACTER_ID, detailsItem.getCharacter_id());
         //excute insert  to db
        try{
            added = db.insert(ChractersContract.CharactersItemDetailsEntry.TABLE_NAME, null, itemDetailsValues);

        }catch(SQLiteConstraintException sqlconstrainExption){

            added=0;
        }

        return added;
    }
    public boolean isExist(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql ="SELECT id FROM "+ChractersContract.CharactersEntry.TABLE_NAME+" WHERE "+ChractersContract.CharactersEntry.COLUMN_ID+"="+id;

        Cursor cursor = db.rawQuery(sql,null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public boolean isDetialsItemExist(long id,String type) {
        SQLiteDatabase db = getReadableDatabase();
        String sql ="SELECT id FROM "+ChractersContract.CharactersItemDetailsEntry.TABLE_NAME+" WHERE "
                +ChractersContract.CharactersItemDetailsEntry.COLUMN_ID+"="+id+" and "
                +ChractersContract.CharactersItemDetailsEntry.COLUMN_TYPE+" = '"+type+"'";

        Cursor cursor = db.rawQuery(sql,null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public void updateThumbinalUrl(long id,String path) {
        SQLiteDatabase db = getWritableDatabase();
        String sql ="update "+ChractersContract.CharactersEntry.TABLE_NAME+" set "+ChractersContract.CharactersEntry.COLUMN_THUMBINAL_PATH+"= '"+path+"' WHERE "+ChractersContract.CharactersEntry.COLUMN_ID+"="+id;
        if(!path.isEmpty())
            db.execSQL(sql);

    }

    public String getPath(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql ="SELECT "+ChractersContract.CharactersEntry.COLUMN_THUMBINAL_PATH+" FROM "+ChractersContract.CharactersEntry.TABLE_NAME+" WHERE "+ChractersContract.CharactersEntry.COLUMN_ID+"="+id;

        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        String path=cursor.getString(cursor.getColumnIndex(ChractersContract.CharactersEntry.COLUMN_THUMBINAL_PATH));
        return path ;
    }
}

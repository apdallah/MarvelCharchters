package com.apdallahy3.marvelcharcters.DataBase;

import android.provider.BaseColumns;

public class ChractersContract {
    public static class CharactersEntry implements BaseColumns {
        public static final String TABLE_NAME="characters";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_NAME ="name";
        public static final String COLUMN_THUMBINAL_URL="thumbinal_url";
        public static final String COLUMN_THUMBINAL_PATH="thumbinal_path";
        public static final String COLUMN_DESCRIPTION="description";

    }
    public static class CharactersItemDetailsEntry implements BaseColumns {
        public static final String TABLE_NAME="character_details";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_NAME ="name";
        public static final String COLUMN_PHOTO_URL ="photo_url";
        public static final String COLUMN_PHOTO_PATH ="photo_path";
        public static final String COLUMN_CHARACTER_ID ="character_id";
        public static final String COLUMN_TYPE ="type";


    }
}

package com.upasana.home.graphmaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;


public class databaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "My Graph";

    public static final String TABLE_MAIN = "GraphDetails";
    public static final String TABLE_SEC = "GraphData";

    public static final String MAIN_COL_1 = "ID";
    public static final String MAIN_COL_2 = "TITLE";
    public static final String MAIN_COL_3 = "DESCRIPTION";
    public static final String MAIN_COL_4 = "DATE_MODIFIED";
    public static final String MAIN_COL_5 = "TYPE";

    public static final String SEC_COL_1 = "ID";
    public static final String SEC_COL_2 = "DATA_NAMES";
    public static final String SEC_COL_3 = "DATA_VALUES";


    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MAIN + " (" + MAIN_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT , " + MAIN_COL_2 + " TEXT, " + MAIN_COL_3 + " TEXT, " + MAIN_COL_4 + " TEXT, " + MAIN_COL_5 + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_SEC + " (" + SEC_COL_1 + " INTEGER , " + SEC_COL_2 + " TEXT, " + SEC_COL_3 + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEC);
        onCreate(db);
    }

    public long insert_data(int type, String g_name, String g_details, String date, String[] dataNames, int[] datavalues) {
        SQLiteDatabase db = this.getWritableDatabase();
        long row_id;
        String d = DateFormat.getDateTimeInstance().format(new Date());

        ContentValues cotent_main = new ContentValues();
        cotent_main.put(MAIN_COL_2, g_name);
        cotent_main.put(MAIN_COL_3, g_details);
        cotent_main.put(MAIN_COL_4, d);
        cotent_main.put(MAIN_COL_5, type);
        //String s="INSERT INTO GRAPH_DETAILS [ TITLE , DESCRIPTION , DATE_MODIFIED , TYPE ] VALUES ( "+g_name+" , "+g_details+" , datetime('now','localtime') , "+type+" ) ;";
        //row_id= db.execSQL(s);

        row_id = db.insert(TABLE_MAIN, null, cotent_main);

        ContentValues cotent_sub = new ContentValues();
        cotent_sub.put(SEC_COL_1, row_id);
        for (int i = 0; i < dataNames.length; i++) {
            cotent_sub.put(SEC_COL_2, dataNames[i]);
            cotent_sub.put(SEC_COL_3, datavalues[i]);
            db.insert(TABLE_SEC, null, cotent_sub);
        }
        return row_id;
    }

    public void del(int type, String g_name, String g_details, String date)
    {
        int ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT ID FROM " + TABLE_MAIN +" WHERE "+MAIN_COL_5+" = "+type+" AND "+MAIN_COL_2+" = '"+g_name.toLowerCase()+"' AND "+MAIN_COL_3+" = '"+g_details.toLowerCase()+"'", null);
        cur.moveToFirst();
        if (cur.getCount()>0) {
            ID = cur.getInt(cur.getColumnIndex("ID"));
            db.execSQL("DELETE FROM " + TABLE_MAIN + " WHERE ID= '" + ID + "';");
            db.execSQL("DELETE FROM " + TABLE_SEC + " WHERE ID= '" + ID + "';");
        }

    }

    public Cursor get_data(int n) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_MAIN +" WHERE "+MAIN_COL_5+" = "+n, null);

        return cur;
    }

    public Cursor get_values(int pos, int type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT ID FROM " + TABLE_MAIN +" WHERE GraphDetails.TYPE = "+type, null);
        c.moveToPosition(pos);
        int id= c.getInt(c.getColumnIndex("ID"));
        c.close();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_SEC +" , "+ TABLE_MAIN +" WHERE GraphDetails.ID = "+id+" AND GraphData.ID = GraphDetails.ID", null);
        return cur;
    }
}

package com.janzelj.tim.shopmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.app.SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID;
import static android.app.SearchManager.SUGGEST_COLUMN_TEXT_1;

/**
 * Created by TJ on 10.10.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ShopMaps.db";
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String PRODUCTS_COLUMN_ID = "_id";
    public static final String PRODUCTS_COLUMN_NAME = SUGGEST_COLUMN_TEXT_1;
    public static final String PRODUCTS_COLUMN_SHOP = "shop_id";
    public static final String PRODUCTS_COLUMN_X = "x";
    public static final String PRODUCTS_COLUMN_Y = "y";

    public static final String SHOPS_TABLE_NAME = "shops";
    public static final String SHOPS_COLUMN_ID = "id";
    public static final String SHOPS_COLUMN_NAME = "name";
    public static final String SHOPS_COLUMN_ADDRESS = "address";

    public static final String MODELS_TABLE_NAME = "models";
    public static final String MODELS_COLUMN_ID = "id";
    public static final String MODELS_COLUMN_TYPE = "type_id";
    public static final String MODELS_COLUMN_X = "x";
    public static final String MODELS_COLUMN_Y = "y";
    public static final String MODELS_COLUMN_WIDTH = "width";
    public static final String MODELS_COLUMN_HEIGHT = "height";
    public static final String MODELS_COLUMN_SHOP = "shop_id";

    public DBHelper(Context context) {
        super(context, DB_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
            "CREATE table products " +
                    "(_id INTEGER PRIMARY KEY, " +
                    PRODUCTS_COLUMN_NAME + " TEXT NOT NULL, " +
                    PRODUCTS_COLUMN_SHOP + " INTEGER NOT NULL, " +
                    PRODUCTS_COLUMN_X + " REAL NOT NULL, " +
                    PRODUCTS_COLUMN_Y + " REAL NOT NULL, " +
                    SUGGEST_COLUMN_INTENT_DATA_ID + " INTEGER, " +
                    "FOREIGN KEY (" + PRODUCTS_COLUMN_SHOP + ") REFERENCES shops(_id) " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE)"
        );
        db.execSQL(
            "CREATE table shops " +
                    "(_id INTEGER PRIMARY KEY, " +
                    SHOPS_COLUMN_NAME + " TEXT NOT NULL, " +
                    SHOPS_COLUMN_ADDRESS + " TEXT NOT NULL)"
        );
        db.execSQL(
                "CREATE table models " +
                    "(_id INTEGER PRIMARY KEY, " +
                    MODELS_COLUMN_TYPE + " INTEGER NOT NULL, " +
                    MODELS_COLUMN_X + " REAL NOT NULL, " +
                    MODELS_COLUMN_Y + " REAL NOT NULL, " +
                    MODELS_COLUMN_WIDTH + " REAL NOT NULL, " +
                    MODELS_COLUMN_HEIGHT + " REAL NOT NULL, " +
                    MODELS_COLUMN_SHOP + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + MODELS_COLUMN_SHOP + ") REFERENCES shops(_id) " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE)"
        );
        db.execSQL(
                "CREATE table db_created " +
                    "(_id INTEGER PRIMARY KEY, " +
                    "created INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS shops");
        onCreate(db);
    }

    public int insertProduct (String name, int shop_id, float x, float y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCTS_COLUMN_NAME, name);
        contentValues.put(PRODUCTS_COLUMN_SHOP, shop_id);
        contentValues.put(PRODUCTS_COLUMN_X, x);
        contentValues.put(PRODUCTS_COLUMN_Y, y);
        db.insert("products", null, contentValues);
        Cursor cur = db.rawQuery("SELECT MAX(_id) FROM products", null);
        cur.moveToFirst();
        int id = cur.getInt(0);
        ContentValues cv = new ContentValues();
        cv.put(SUGGEST_COLUMN_INTENT_DATA_ID, id);
        db.update(PRODUCTS_TABLE_NAME, cv, "_id="+id, null);
        cur.close();
        return id;
    }

    public String getDataId (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM products WHERE _id = " + id, null);

        String dataId = "bla";
        if(cur.moveToFirst()) {
            dataId = cur.getString(5);
        }
        cur.close();
        return dataId;
    }

    public float[] getProductCoordinates (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT x, y FROM products WHERE _id = " + id, null);

        float x = 0;
        float y = 0;
        if(cur.moveToFirst()) {
            x = cur.getFloat(0);
            y = cur.getFloat(1);
        }
        cur.close();

        float[] coords = {x, y};
        return coords;
    }

    public int insertShop (String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHOPS_COLUMN_NAME, name);
        contentValues.put(SHOPS_COLUMN_ADDRESS, address);
        db.insert("shops", null, contentValues);
        Cursor cur = db.rawQuery("SELECT MAX(_id) FROM shops", null);
        cur.moveToFirst();
        int id = cur.getInt(0);
        cur.close();
        return id;
    }

    public int insertModel (int type_id, float x, float y, float width, float height, int shop_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MODELS_COLUMN_TYPE, type_id);
        contentValues.put(MODELS_COLUMN_X, x);
        contentValues.put(MODELS_COLUMN_Y, y);
        contentValues.put(MODELS_COLUMN_WIDTH, width);
        contentValues.put(MODELS_COLUMN_HEIGHT, height);
        contentValues.put(MODELS_COLUMN_SHOP, shop_id);
        db.insert("models", null, contentValues);
        Cursor cur = db.rawQuery("SELECT MAX(_id) FROM models", null);
        cur.moveToFirst();
        int id = cur.getInt(0);
        cur.close();
        return id;
    }

    public Cursor getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery( "select * from products where _id="+id+"", null );

        return cur;
    }

    public Cursor getProducts(String search) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery( "select * from products where " + PRODUCTS_COLUMN_NAME + " LIKE '%" + search + "%'", null );

        return cur;
    }

    public ArrayList<ArrayList<String>> getModelByShop(int shop_id) {
        ArrayList<ArrayList<String>> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery( "select * from models where shop_id = " + shop_id + "", null );
        cur.moveToFirst();

        if (cur != null && cur.getCount() > 0) {

            do {
                ArrayList<String> row = new ArrayList<>();

                row.add(cur.getString(1));
                row.add(cur.getString(2));
                row.add(cur.getString(3));
                row.add(cur.getString(4));
                row.add(cur.getString(5));

                array_list.add(row);
            }
            while (cur.moveToNext());
        }
        cur.close();

        return array_list;
    }

    public Cursor getShop(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery( "select * from shops where _id="+id+"", null );
        cur.close();
        return cur;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PRODUCTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateProduct (Integer id, String name, String shop, float x, float y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCTS_COLUMN_NAME, name);
        contentValues.put(PRODUCTS_COLUMN_SHOP, shop);
        contentValues.put(PRODUCTS_COLUMN_X, x);
        contentValues.put(PRODUCTS_COLUMN_Y, y);
        db.update("products", contentValues, "_id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteProduct (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products",
                "_id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteShop (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("shops",
                "_id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ArrayList<String>> getAll(String tableName, String[] columns) {
        ArrayList<ArrayList<String>> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery( "select * from " + tableName, null );
        cur.moveToFirst();

        while(!cur.isAfterLast()){
            ArrayList<String> row = new ArrayList<>();
            for (String column : columns) {
                row.add(cur.getString(cur.getColumnIndex(column)));
            }

            array_list.add(row);
            cur.moveToNext();
        }
        cur.close();

        return array_list;
    }

    public boolean dropAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS shops");
        return true;
    }

    public int dbCreated () {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("created", 1);
        db.insert("db_created", null, contentValues);
        return 1;
    }

    public int isDbCreated () {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT created FROM db_created WHERE _id = 1", null);

        int isCreated = 0;
        if(cur.moveToFirst()) {
            isCreated = cur.getInt(0);
        }
        cur.close();

        return isCreated;
    }
}

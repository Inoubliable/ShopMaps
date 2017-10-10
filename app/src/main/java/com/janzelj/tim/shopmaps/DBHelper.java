package com.janzelj.tim.shopmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by TJ on 10.10.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ShopMaps.db";
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String PRODUCTS_COLUMN_ID = "id";
    public static final String PRODUCTS_COLUMN_NAME = "name";
    public static final String PRODUCTS_COLUMN_SHOP = "shop_id";
    public static final String PRODUCTS_COLUMN_x = "x";
    public static final String PRODUCTS_COLUMN_y = "y";

    public static final String SHOPS_TABLE_NAME = "shops";
    public static final String SHOPS_COLUMN_ID = "id";
    public static final String SHOPS_COLUMN_NAME = "name";
    public static final String SHOPS_COLUMN_ADDRESS = "address";

    public DBHelper(Context context) {
        super(context, DB_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
            "CREATE table products " +
                    "(id INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "shop_id INTEGER NOT NULL, " +
                    "x REAL NOT NULL, " +
                    "y REAL NOT NULL, " +
                    "FOREIGN KEY (shop_id) REFERENCES shops(id) " +
                    "ON UPDATE CASCADE " +
                    "ON DELETE CASCADE)"
        );
        db.execSQL(
            "CREATE table shops " +
                    "(id INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS shops");
        onCreate(db);
    }

    public boolean insertProduct (String name, int shop_id, float x, float y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("shop_id", shop_id);
        contentValues.put("x", x);
        contentValues.put("y", y);
        db.insert("products", null, contentValues);
        return true;
    }

    public int insertShop (String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        db.insert("shops", null, contentValues);
        Cursor cur = db.rawQuery("SELECT MAX(id) FROM shops", null);
        cur.moveToFirst();
        int id = cur.getInt(0);
        cur.close();
        return id;
    }

    public Cursor getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur =  db.rawQuery( "select * from products where id="+id+"", null );
        cur.close();
        return cur;
    }

    public Cursor getShop(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur =  db.rawQuery( "select * from shops where id="+id+"", null );
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
        contentValues.put("name", name);
        contentValues.put("shop_id", shop);
        contentValues.put("x", x);
        contentValues.put("y", y);
        db.update("products", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteProduct (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteShop (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("shops",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String[]> getAll(String tableName) {
        ArrayList<String[]> array_list = new ArrayList<String[]>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery( "select * from " + tableName, null );
        cur.moveToFirst();

        while(!cur.isAfterLast()){
            String[] row = {cur.getString(cur.getColumnIndex(SHOPS_COLUMN_NAME)), cur.getString(cur.getColumnIndex(SHOPS_COLUMN_ADDRESS))};
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
}

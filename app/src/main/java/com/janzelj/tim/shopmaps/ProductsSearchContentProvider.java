package com.janzelj.tim.shopmaps;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by TJ on 12.10.2017.
 */

public class ProductsSearchContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.janzelj.tim.shopmaps.ProductsSearchContentProvider";

    private DBHelper mydb;

    @Override
    public boolean onCreate() {
        mydb = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cur = null;
        String query = uri.getLastPathSegment();
        //Log.d("timi", "" + query);
        cur = mydb.getProducts(query);

        return cur;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}

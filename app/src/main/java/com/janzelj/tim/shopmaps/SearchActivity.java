package com.janzelj.tim.shopmaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by TJ on 10.10.2017.
 */

public class SearchActivity extends Activity {
    private DBHelper mydb ;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);

        mydb = new DBHelper(this);
        //mydb.dropAll();
        mydb.insertShop("Rutar", "Rudnik");
        mydb.insertShop("Interspar", "BTC");
        mydb.insertShop("Mercator", "BTC");
        mydb.getAll(DBHelper.SHOPS_TABLE_NAME);
        ArrayList<String[]> shops_array = mydb.getAll(DBHelper.SHOPS_TABLE_NAME);

        listView = (ListView)findViewById(R.id.listView1);

        dataModels = new ArrayList<>();
        for (String[] shop: shops_array) {
            dataModels.add(new DataModel(shop[0], shop[1]));
        }

        adapter = new CustomSearchAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //DataModel dataModel = dataModels.get(position);

                Intent mainActivity = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }
}

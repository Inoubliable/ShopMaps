package com.janzelj.tim.shopmaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

        mydb.insertShop("Rutar", "Rudnik");
        mydb.insertShop("Mercator", "Dunajska cesta 218");
        mydb.insertShop("Interspar", "BTC");

        //1
        mydb.insertModel(MyGLRenderer.TYPE_PROSTOR, -10, -10, 20, 20, 0);

        mydb.insertModel(MyGLRenderer.TYPE_STENA, -11, -11, 1, 22, 0);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, -10, -11, 20, 1, 0);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, 10, -11, 1, 22, 0);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, -10, 10, 20, 1, 0);

        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -8, -4, 1, 5, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -5, -4, 1, 5, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -2, -4, 1, 5, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 1, -4, 1, 5, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -8, 2, 10, 1.5f, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 4, -4, 1.5f, 8, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 7, -4, 1.5f, 8, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 4, 6, 2, 3, 0);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 7, 6, 2, 3, 0);

        //2
        mydb.insertModel(MyGLRenderer.TYPE_PROSTOR, -10, -5, 20, 10, 1);

        mydb.insertModel(MyGLRenderer.TYPE_STENA, -11, -6, 1, 12, 1);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, -10, -6, 20, 1, 1);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, 10, -6, 1, 12, 1);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, -10, 5, 20, 1, 1);

        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -8, -4, 1, 5, 1);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -5, -4, 1, 5, 1);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -2, -4, 1, 5, 1);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 1, -4, 1, 5, 1);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -8, 2, 10, 1.5f, 1);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 4, -4, 1.5f, 8, 1);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 7, -4, 1.5f, 8, 1);

        //3
        mydb.insertModel(MyGLRenderer.TYPE_PROSTOR, -15f, -8f, 30, 16, 2);

        mydb.insertModel(MyGLRenderer.TYPE_STENA, -16, -9, 1, 18, 2);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, -15, -9, 30, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, 15, -9, 1, 18, 2);
        mydb.insertModel(MyGLRenderer.TYPE_STENA, -15, 8, 30, 1, 2);

        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -13, -6, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -13, -4, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -13, -2, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -13, 0, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -13, 2, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, -13, 4, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 3, -6, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 3, -4, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 3, -2, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 3, 0, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 3, 2, 10, 1, 2);
        mydb.insertModel(MyGLRenderer.TYPE_REGAL, 3, 4, 10, 1, 2);


        String[] columns = {"name", "address"};
        ArrayList<ArrayList<String>> shops_array = mydb.getAll(DBHelper.SHOPS_TABLE_NAME, columns);

        listView = (ListView)findViewById(R.id.listView1);

        dataModels = new ArrayList<>();
        for (ArrayList<String> shop: shops_array) {
            dataModels.add(new DataModel(shop.get(0), shop.get(1)));
        }

        adapter = new CustomSearchAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //DataModel dataModel = dataModels.get(position);

                Intent i = new Intent(SearchActivity.this, MainActivity.class);

                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }
}

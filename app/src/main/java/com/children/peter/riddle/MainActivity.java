package com.children.peter.riddle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.visible;


public class MainActivity extends AppCompatActivity {

    private final float textIncrementSize = 0.1f;
    private ArrayList<Riddle> riddles = new ArrayList<>();
    private int index = 0;

    public MainActivity() {
        Riddle riddle = new Riddle(
                "自家兄弟肩并肩，脱去黄袍味儿鲜；片片果肉色彩艳，冬天吃它来过年。（打一水果）", "—— 谜底:橘子");
        riddles.add(riddle);
        riddle = new Riddle(
                "壳儿硬，壳儿脆，四个姐妹隔床睡，从小到大背靠背，盖着一床疙瘩被。(打一植物)", "—— 谜底:核桃");
        riddles.add(riddle);
        riddle = new Riddle(
                "自家兄弟肩并肩，脱去黄袍味儿鲜；片片果肉色彩艳，冬天吃它来过年。（打一水果）", "—— 谜底:橘子");
        riddles.add(riddle);
        riddle = new Riddle(
                "壳儿硬，壳儿脆，四个姐妹隔床睡，从小到大背靠背，盖着一床疙瘩被。(打一植物)", "—— 谜底:核桃");
        riddles.add(riddle);
        riddle = new Riddle(
                "自家兄弟肩并肩，脱去黄袍味儿鲜；片片果肉色彩艳，冬天吃它来过年。（打一水果）", "—— 谜底:橘子");
        riddles.add(riddle);
        riddle = new Riddle(
                "壳儿硬，壳儿脆，四个姐妹隔床睡，从小到大背靠背，盖着一床疙瘩被。(打一植物)", "—— 谜底:核桃");
        riddles.add(riddle);
        riddle = new Riddle(
                "自家兄弟肩并肩，脱去黄袍味儿鲜；片片果肉色彩艳，冬天吃它来过年。（打一水果）", "—— 谜底:橘子");
        riddles.add(riddle);
        riddle = new Riddle(
                "壳儿硬，壳儿脆，四个姐妹隔床睡，从小到大背靠背，盖着一床疙瘩被。(打一植物)", "—— 谜底:核桃");
        riddles.add(riddle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate execute");

//        ActionBar actionBar =  getSupportActionBar();
//        if(actionBar != null) {
//            actionBar.hide();
//        }

        RiddleAdapter adapter = new RiddleAdapter(
                this, R.layout.riddle_item, riddles);
        ListView listView = (ListView) findViewById(R.id.riddle_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Log.d("result return", data.getStringExtra("data_return"));
                }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

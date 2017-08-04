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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final float textIncrementSize = 0.1f;
    private ArrayList<String> riddles = new ArrayList<String>();
    private int index = 0;

    public MainActivity() {
        riddles.add("自家兄弟肩并肩，脱去黄袍味儿鲜；片片果肉色彩艳，冬天吃它来过年。（打一水果）");
        riddles.add("比牛大，吃嫩草，头上长只弯弯角。老虎见它让三分，力大无穷脾气暴。（ 打一动物）");
        riddles.add("一腿长,一腿短,长腿脚尖尖,短腿画圈圈(打一文具)");
        riddles.add("两个小铁人,长着铁嘴唇,见了布和纸,张嘴两离分(打一日用品)");
        riddles.add("弯弯月亮薄皮子,黄衣裹着白胖子,肠胃消化就靠它 (打一水果)");
        riddles.add("细细钢筋搭房架，塑料薄膜当砖瓦。进进出出都是菜，送给社区千万家。（打一农业设施）");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate execute");

        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
//        Button btn1 = (Button) findViewById(R.id.button);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(".activitytest.ACTION_START");
//                intent.addCategory(".activitytest.MY_CATEGORY");
//                intent.putExtra("extra_data", "Hello");
//
////                startActivityForResult(intent, 1);
//                startActivity(intent);
//
////                Intent intent = new Intent(Intent.ACTION_DIAL);
////                intent.setData(Uri.parse("tel:10086"));
////                startActivity(intent);
//            }
//        });
//

        Button nextRiddle = (Button) findViewById(R.id.next);
        nextRiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textRiddle = (TextView)findViewById(R.id.riddleContent);
                textRiddle.setText(riddles.get(index++));
                index %= 6;
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

        TextView riddleContent = (TextView) findViewById(R.id.riddleContent);
        float textSize = riddleContent.getTextSize();
        float size = 0;

        switch (item.getItemId()) {
            case R.id.add_item:
                size = textSize + textIncrementSize;
                riddleContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
                Toast.makeText(this, "increment text size " + size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                size = textSize - textIncrementSize;
                riddleContent.setTextSize(size);
                Toast.makeText(this, "decrement text size " + size, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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

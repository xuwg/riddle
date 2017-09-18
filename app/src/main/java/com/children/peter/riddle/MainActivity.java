package com.children.peter.riddle;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.children.peter.riddle.db.Riddle;
import com.children.peter.riddle.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;
    NetworkChangeReceiver networkChangeReceiver;
    private ImageView photo;
    ProgressBar progressBar;
    private Uri imageUri;
    private MyPagerAdapter myPagerAdsapter;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CHOOSE_PHOTO = 3;
    private static final String TAG = "MainActivity";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPagerAdsapter = new MyPagerAdapter(getSupportFragmentManager());
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        photo = (ImageView) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {

            //使用摄像头拍照
            void ImageCapture() {
                File outputImage = new File(getExternalCacheDir(), "photo.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "com.children.peter.riddle.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }

            // 在相册选取
            void TakePhoto() {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    openAlbum();
                }
            }

            @Override
            public void onClick(View v) {
                TakePhoto();
            }
        });

        queryTitlesFromServer();

        //network change broadcast
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void queryTitlesFromServer() {
        final String address = "http://www.cmiyu.com";
        progressBar.setVisibility(View.VISIBLE);

        HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String html = new String(response.body().bytes(), "GBK");
                Document doc = Jsoup.parse(html);
                Elements elems = doc.getElementsByClass("miyuheader");
                for (Element elem : elems) {
                    Elements tabs = elem.getElementsByTag("a");
                    HashMap<String, String> titles = new LinkedHashMap<String, String>();
                    for (Element tab : tabs) {
                        String tabText = tab.text();
                        String linkHref = tab.attr("href");

                        titles.put(tabText, address + linkHref);
                    }
                    myPagerAdsapter.setTitles(titles);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewPager pager = (ViewPager) findViewById(R.id.pager);
                        pager.setAdapter(myPagerAdsapter);
                        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                        tabs.setViewPager(pager);

                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "query titles failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkChangeReceiver);
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
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        photo.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {

                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统
                        handleImageOnKitkat(data);
                    } else {
                        //4.4以下系统
                        handleImageBeforeKitKat(data);
                    }
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            photo.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
//                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private Map<String, String> titles = new LinkedHashMap<String, String>();
        private ArrayList<RiddlesFragment> views;
        RiddlesFragment curFragment;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setTitles(HashMap<String, String> map) {
            titles = map;
            if (views != null) {
                views.clear();
            } else {
                views = new ArrayList<RiddlesFragment>();
            }
            for (int i = 0; i < map.size(); ++i) {
                views.add(null);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (String) titles.keySet().toArray()[position];
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Fragment getItem(int position) {

            curFragment = views.get(position);
            if (curFragment == null) {
                String address = titles.get(titles.keySet().toArray()[position]);
                curFragment = new RiddlesFragment(address);
                views.set(position, curFragment);
            }

            return curFragment;
        }
    }
}

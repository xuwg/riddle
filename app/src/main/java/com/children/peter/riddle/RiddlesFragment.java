package com.children.peter.riddle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.children.peter.riddle.db.Riddle;
import com.children.peter.riddle.db.RiddleAdapter;
import com.children.peter.riddle.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-09-11.
 */

public class RiddlesFragment extends Fragment {

    private List<Riddle> riddles = new ArrayList<>();
    RiddleAdapter adapter;
    String address;
    private ProgressBar progressBar;
    MainActivity activity;
    RecyclerView recyclerView;

    public RiddlesFragment(String address) {
        super();
        this.address = address;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.riddles, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.riddle_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        getProgressBar().setVisibility(View.VISIBLE);
        riddles.clear();

        activity = (MainActivity) getActivity();

        HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {

            int type;
            int category;
            private static final String TAG = "RiddlesFragment";

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d(TAG, "onResponse: " + address);

                String html = new String(response.body().bytes(), "GBK");
                Document doc = Jsoup.parse(html);
                parseMiyuType(doc);
                parseMiyu(doc);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        if (adapter == null) {
                            adapter = new RiddleAdapter(riddles, RiddlesFragment.this);
                            recyclerView.setAdapter(adapter);
//                        } else {
//                            adapter.setRiddles(riddles);
//                            adapter.notifyDataSetChanged();
//                        }
                        getProgressBar().setVisibility(View.GONE);
                    }
                });

            }

            private void parseMiyuType(Document doc) {
                Elements contents = doc.getElementsByClass("content");
                for (Element content : contents) {
                    Elements lefts = content.getElementsByClass("left");
                    for (Element left : lefts) {
                        Elements pagess = left.getElementsByClass("pages");
                        for (Element pages : pagess) {
                            Elements hrefs = pages.getElementsByTag("a");
                            for (Element href : hrefs) {

                                String linkHref = href.attr("href");
                                Pattern p = Pattern.compile("\\d{3,}");
                                Matcher m = p.matcher(linkHref);
                                if (m.find()) {
                                    String name = m.group();

                                    Log.d(TAG, "parseMiyuType: " + name);
                                    type = Integer.parseInt(name.substring(0, 2));
                                    category = Integer.parseInt(name.substring(2));
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            private void parseMiyu(Document doc) {

                Elements contents = doc.getElementsByClass("content");
                for (Element content : contents) {
                    Elements lefts = content.getElementsByClass("left");
                    for (Element left : lefts) {
                        Elements lists = left.getElementsByClass("list");
                        for (Element list : lists) {
                            Elements miyus = list.getElementsByTag("a");
                            for (Element miyu : miyus) {
                                String linkText = miyu.text();
                                String linkHref = miyu.attr("href");

                                Riddle riddle = new Riddle(type, category, linkText, linkHref);
                                //riddle.save();
                                riddles.add(riddle);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

                Log.d(TAG, "onFailure: " + address);

                e.printStackTrace();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(activity, "query " + address + " failed", Toast.LENGTH_SHORT).show();
                        getProgressBar().setVisibility(View.GONE);
                    }
                });
            }
        });
        return view;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}

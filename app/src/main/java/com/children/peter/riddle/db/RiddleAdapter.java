package com.children.peter.riddle.db;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.children.peter.riddle.R;
import com.children.peter.riddle.RiddlesFragment;
import com.children.peter.riddle.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/4.
 */

public class RiddleAdapter extends RecyclerView.Adapter<RiddleAdapter.ViewHolder> {

    private List<Riddle> riddles;
    private static final String TAG = "RiddleAdapter";
    RiddlesFragment fragment;

    public List<Riddle> getRiddles() {
        return riddles;
    }

    public void setRiddles(List<Riddle> riddles) {
        this.riddles = riddles;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            this.riddleContent = (TextView) itemView.findViewById(R.id.riddle_content);
            this.riddleKey = (TextView) itemView.findViewById(R.id.riddle_key);
            this.answer = (TextView) itemView.findViewById(R.id.answer);
            this.collapse = (TextView) itemView.findViewById(R.id.collapse);
        }

        TextView riddleContent;
        TextView riddleKey;

        TextView answer;
        TextView collapse;
    }

    public RiddleAdapter(List<Riddle> objects, RiddlesFragment fragment) {

        setRiddles(objects);
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.riddle_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.answer.setVisibility(View.GONE);
                viewHolder.riddleKey.setVisibility(View.VISIBLE);
                viewHolder.collapse.setVisibility(View.VISIBLE);

                fragment.getProgressBar().setVisibility(View.VISIBLE);
                String address = new String("http://www.cmiyu.com");
                address += viewHolder.riddleKey.getText();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {

                    String riddleKey;

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        Log.d(TAG, "onResponse: ");

                        String html = new String(response.body().bytes(), "GBK");
                        Document doc = Jsoup.parse(html);
                        Elements contents = doc.getElementsByClass("content");
                        for (Element content : contents) {
                            Elements lefts = content.getElementsByClass("left");
                            for (Element left : lefts) {
                                Elements tops = left.getElementsByClass("top");
                                for (Element top : tops) {
                                    Elements mds = top.getElementsByClass("md");
                                    for (Element md : mds) {
                                        Elements miyus = md.getElementsByTag("h3");
                                        for (Element miyu : miyus) {
                                            String midi = miyu.text();
                                            Pattern p = Pattern.compile("^谜底：(.*)");
                                            Matcher m = p.matcher(midi);
                                            if (m.find()) {
                                                riddleKey = m.group(1);

                                                Log.d(TAG, "parseMiyuType: " + riddleKey);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.riddleKey.setText(riddleKey);
                                fragment.getProgressBar().setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {

                        e.printStackTrace();
                        Log.d(TAG, "onFailure: ");

                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragment.getProgressBar().setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });
        viewHolder.collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.answer.setVisibility(View.VISIBLE);
                viewHolder.riddleKey.setVisibility(View.GONE);
                viewHolder.collapse.setVisibility(View.GONE);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: " + position);
        Riddle riddle = getRiddles().get(position);
        holder.riddleContent.setText(riddle.getContent());
        holder.riddleKey.setText(riddle.getKey());
    }

    @Override
    public int getItemCount() {
        return getRiddles().size();
    }
}

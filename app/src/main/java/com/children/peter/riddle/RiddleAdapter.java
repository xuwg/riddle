package com.children.peter.riddle;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 */

public class RiddleAdapter extends RecyclerView.Adapter<RiddleAdapter.ViewHolder> {

    List<Riddle> riddles;

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

    public RiddleAdapter(List<Riddle> objects) {

        riddles = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.riddle_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.riddleKey.setVisibility(View.VISIBLE);
                viewHolder.collapse.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.riddleKey.setVisibility(View.GONE);
                viewHolder.collapse.setVisibility(View.GONE);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Riddle riddle = riddles.get(position);
        holder.riddleContent.setText(riddle.getContent());
        holder.riddleKey.setText(riddle.getKey());
    }

    @Override
    public int getItemCount() {
        return riddles.size();
    }
}

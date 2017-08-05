package com.children.peter.riddle;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class RiddleAdapter extends ArrayAdapter<Riddle> {
    private int resourceId;

    public RiddleAdapter(@NonNull Context context,
                         @IdRes int textViewResourceId, @NonNull List<Riddle> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View view;
        Riddle riddle = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.riddleContent = (TextView) view.findViewById(R.id.riddle_content);
            viewHolder.riddleKey = (TextView) view.findViewById(R.id.riddle_key);
            viewHolder.answer = (TextView) view.findViewById(R.id.answer);
            viewHolder.collapse = (TextView) view.findViewById(R.id.collapse);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.riddleContent.setText(riddle.getContent());
        viewHolder.riddleKey.setText(riddle.getKey());

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
        return view;
    }

    class ViewHolder {
        TextView riddleContent;
        TextView riddleKey;

        TextView answer;
        TextView collapse;
    }
}

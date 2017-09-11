package com.children.peter.riddle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.children.peter.riddle.db.Riddle;
import com.children.peter.riddle.db.RiddleAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-09-11.
 */

public class RiddlesFragment extends Fragment {

    private List<Riddle> riddles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.riddles, container, false);

        riddles = DataSupport.findAll(Riddle.class);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.riddle_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RiddleAdapter adapter = new RiddleAdapter(riddles);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

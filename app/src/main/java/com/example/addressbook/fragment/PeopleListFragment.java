package com.example.addressbook.fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;

import android.graphics.Color;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.activity.ModifyActivity;
import com.example.addressbook.R;
import com.example.addressbook.utils.SPUtils;
import com.example.addressbook.adapter.PeopleAdapter;
import com.example.addressbook.bean.People;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lzj.sidebar.SideBarLayout;

import org.litepal.LitePal;

import java.util.*;

public class PeopleListFragment extends Fragment {

    public static final int REQUEST_CODE_MODIFY = 1;

    public RecyclerView recyclerView;
    public SearchView mSearchView;
    public SideBarLayout sidebar;
    public PeopleAdapter peopleAdapter;
    List<People> peoples = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        peopleAdapter = new PeopleAdapter(getActivity(), peoples);
        mSearchView = view.findViewById(R.id.searchview);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                query(query);
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                query(newText);
                return false;
            }

        });

        sidebar = view.findViewById(R.id.sidebar);
        sidebar.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                for (int i = 0; i < peoples.size(); i++) {
                    if (peoples.get(i).getWord().equals(word)) {
                        recyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view_1);

        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_CODE_MODIFY);
            }
        });

        return view;
    }

    private void query(String newText) {
        List<People> peopleList;
        if (TextUtils.isEmpty(newText)) {
            onResume();
        } else {
            peopleList = LitePal.select().where("name like ?", "%" + newText + "%").order("word").find(People.class);
            peoples.clear();
            peoples.addAll(peopleList);
            peopleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(peopleAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int sort = SPUtils.getInt("sort", 0, getContext());
        if (sort == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        List<People> peopleList = LitePal.select().order("word").find(People.class);
        if (!peoples.equals(peopleList)) {
            peoples.clear();
            peoples.addAll(peopleList);
            peopleAdapter.notifyDataSetChanged();
        }
    }
}
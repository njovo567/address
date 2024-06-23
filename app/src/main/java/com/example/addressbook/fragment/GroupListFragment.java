package com.example.addressbook.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.addressbook.R;
import com.example.addressbook.adapter.GroupAdapter;
import com.example.addressbook.bean.People;
import com.example.addressbook.bean.PeopleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {

    private static final String DIALOG_TITLE = "添加分组";
    private static final String DIALOG_CONTENT = "请输入新的分组名称";
    private static final String DIALOG_INPUT_HINT = "分组名称";
    private static final String TOAST_ADD_SUCCESS = "添加成功";
    private static final String TOAST_GROUP_EXISTS = "分组已存在";

    public ExpandableListView expandableListView;
    public GroupAdapter groupAdapter;
    List<PeopleGroup> peopleGroupList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_2, container, false);
        groupAdapter = new GroupAdapter(getActivity(), peopleGroupList);
        expandableListView = view.findViewById(R.id.expand_list);
        expandableListView.setAdapter(groupAdapter);

        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asInputConfirm(DIALOG_TITLE, DIALOG_CONTENT, DIALOG_INPUT_HINT, new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                List<PeopleGroup> list = LitePal.where("name = ?", text).find(PeopleGroup.class);
                                if (list == null || list.isEmpty()) {
                                    PeopleGroup group = new PeopleGroup();
                                    group.setId(System.currentTimeMillis());
                                    group.setName(text);
                                    group.setPeopleList(new ArrayList<>());
                                    group.save();
                                    peopleGroupList.add(group);
                                    groupAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), TOAST_ADD_SUCCESS, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), TOAST_GROUP_EXISTS, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });

        return view;
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
        List<PeopleGroup> peopleGroupList = LitePal.findAll(PeopleGroup.class);
        List<People> peopleList = LitePal.findAll(People.class);

        for (PeopleGroup peopleGroup : peopleGroupList) {
            List<People> list = new ArrayList<>();
            String name = peopleGroup.getName();
            for (People people : peopleList) {
                if (!TextUtils.isEmpty(people.getG())) {
                    if (people.getG().equals(name)) {
                        list.add(people);
                    }
                }
            }
            peopleGroup.setPeopleList(list);
        }

        this.peopleGroupList.clear();
        this.peopleGroupList.addAll(peopleGroupList);
        groupAdapter.notifyDataSetChanged();
    }
}

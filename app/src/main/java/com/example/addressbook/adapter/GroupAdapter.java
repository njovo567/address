package com.example.addressbook.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.example.addressbook.R;
import com.example.addressbook.bean.PeopleGroup;
import com.example.addressbook.bean.People;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.litepal.LitePal;

import java.util.List;

public class GroupAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<PeopleGroup> peopleGroups;

    public GroupAdapter(Context c, List<PeopleGroup> peopleGroups) {
        context = c;        // getActivity()
        this.peopleGroups = peopleGroups;
    }

    @Override
    public int getGroupCount() {
        return peopleGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return peopleGroups.get(groupPosition).getPeopleList().size();
    }

    @Override
    public PeopleGroup getGroup(int groupPosition) {
        return peopleGroups.get(groupPosition);
    }

    @Override
    public People getChild(int groupPosition, int childPosition) {
        return peopleGroups.get(groupPosition).getPeopleList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return peopleGroups.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return peopleGroups.get(groupPosition).getPeopleList().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_view_1_item, parent, false);
        PeopleGroup peopleGroup = peopleGroups.get(groupPosition);
        TextView text_view_name = itemView.findViewById(R.id.text_view_name);
        text_view_name.setText(peopleGroup.getName());
        TextView text_view_address = itemView.findViewById(R.id.text_view_address);
        List<People> children = peopleGroup.getPeopleList();
        if (children == null || children.isEmpty()) {
            text_view_address.setVisibility(View.GONE);
        } else {
            text_view_address.setVisibility(View.VISIBLE);
            text_view_address.setText(peopleGroup.getPeopleList().size() + "");
        }

        return itemView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_view_2_item, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.circle_image_view);
        TextView textView = (TextView) itemView.findViewById(R.id.text_view);

        PeopleGroup peopleGroup = peopleGroups.get(groupPosition);

        List<People> children = peopleGroup.getPeopleList();
        if (children != null && !children.isEmpty()) {
            People p = children.get(childPosition);
            int i = Integer.valueOf(p.getPicturePath()).intValue();
            Glide.with(context).load(i).into(imageView);
            textView.setText(p.getName());
        }


        ImageView menu = (ImageView) itemView.findViewById(R.id.menu_image_view);
        menu.setVisibility(View.GONE);

        return itemView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
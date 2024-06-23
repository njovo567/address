package com.example.addressbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.addressbook.R;
import com.example.addressbook.bean.People;
import com.example.addressbook.bean.PeopleGroup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.litepal.LitePal;

import java.util.List;

// 适配器类，用于在 ListView 或 GridView 中显示 PeopleGroup 对象的列表
public class SettingGroupAdapter extends BaseAdapter {

    private Context context; // 上下文对象
    private List<PeopleGroup> peopleGroups; // PeopleGroup 对象的列表

    // 构造函数，初始化上下文和 PeopleGroup 列表
    public SettingGroupAdapter(Context c, List<PeopleGroup> peopleGroups) {
        context = c;
        this.peopleGroups = peopleGroups;
    }

    // 返回列表项的数量
    @Override
    public int getCount() {
        return peopleGroups.size();
    }

    // 返回指定位置的 PeopleGroup 对象
    @Override
    public PeopleGroup getItem(int position) {
        return peopleGroups.get(position);
    }

    // 返回指定位置项目的 ID，这里返回 0
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 获取视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_view_3_item, parent, false);
        PeopleGroup peopleGroup = peopleGroups.get(position);

        ImageView menu_image_view = itemView.findViewById(R.id.menu_image_view);
        menu_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首先检查这个组别下是否还有联系人
                List<People> groupMembers = LitePal.where("g = ?", String.valueOf(peopleGroup.getName())).find(People.class);
                if (!groupMembers.isEmpty()) {
                    // 如果组别下有联系人，显示一个警告对话框
                    new XPopup.Builder(context)
                            .asConfirm("操作不允许", "该组别下还有联系人，无法删除。",
                                    () -> {})
                            .show();
                } else {
                    // 如果组别下没有联系人，显示确认删除的对话框
                    new XPopup.Builder(context)
                            .asConfirm("确认删除", "您确定要删除这个组别吗？", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    LitePal.delete(PeopleGroup.class, peopleGroup.getId());
                                    peopleGroups.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .show();
                }
            }
        });

        TextView text_view_name = itemView.findViewById(R.id.text_view_name);
        text_view_name.setText(peopleGroup.getName());

        return itemView;
    }

}

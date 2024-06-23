package com.example.addressbook.activity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.Manifest;

import com.example.addressbook.R;
import com.example.addressbook.adapter.FragmentAdapter;
import com.example.addressbook.fragment.PeopleListFragment;
import com.example.addressbook.fragment.GroupListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.*;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_READ_CALL_LOG = 2;
    private static final int REQUEST_CODE_CALL_PHONE = 3;

    private static final String TAB_TITLE_CONTACT = "Contact";
    private static final String TAB_TITLE_GROUP = "Group";
    private static final String TOAST_PERMISSION_GRANTED = "权限已授予";
    private static final String ACTION_BAR_TITLE = "编辑联系人";

    private ViewPager viewPager; // ViewPager 实例
    private TabLayout tabLayout; // TabLayout 实例

    private List<String> titles = new ArrayList<>(); // 标题列表
    public List<Fragment> fragments = new ArrayList<>(); // Fragment 列表

    public FragmentAdapter fragmentAdapter; // FragmentAdapter 实例

    // 初始化方法，用于设置 ViewPager 和 TabLayout
    public void initializeViewPagerAndTabs() {
        tabLayout = findViewById(R.id.tab_layout); // 获取 TabLayout 实例
        viewPager = findViewById(R.id.view_pager); // 获取 ViewPager 实例

        titles.add(TAB_TITLE_CONTACT); // 添加标题 "Contact"
        titles.add(TAB_TITLE_GROUP); // 添加标题 "Group"

        // 根据标题列表动态添加 Tab
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }

        fragments.add(new PeopleListFragment()); // 添加 PeopleListFragment 实例到 Fragment 列表
        fragments.add(new GroupListFragment()); // 添加 GroupListFragment 实例到 Fragment 列表

        // 创建 FragmentAdapter 实例，用于管理 ViewPager 中的 Fragment
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(fragmentAdapter); // 设置 ViewPager 的适配器
        viewPager.setOffscreenPageLimit(1); // 设置 ViewPager 的预加载页数
        tabLayout.setupWithViewPager(viewPager); // 将 TabLayout 和 ViewPager 关联起来
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter); // 根据适配器设置 TabLayout 的标签

        // 设置点击设置图标（iv_setting）的监听器，启动设置活动
        findViewById(R.id.iv_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SettingActivity.class));
            }
        });
    }

    // 权限请求初始化方法，根据权限状态调用 initializeViewPagerAndTabs() 方法或请求权限
    public void checkPermissionsAndInitialize() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
        } else {
            initializeViewPagerAndTabs(); // 如果已有权限，则直接初始化界面
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); // 设置当前活动的布局
        setTitle(ACTION_BAR_TITLE); // 设置标题栏标题

        // 检查是否有读取通话记录的权限，如果没有则请求该权限，否则调用 checkPermissionsAndInitialize() 方法
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_CODE_READ_CALL_LOG);
        } else {
            checkPermissionsAndInitialize(); // 如果已有权限，则初始化界面
        }
    }

    // 处理从其他活动返回的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case PeopleListFragment.REQUEST_CODE_MODIFY: // 请求码为修改联系人
                fragmentAdapter.notifyDataSetChanged(); // 通知适配器数据已更改
                if (resultCode == RESULT_OK) {
                    fragmentAdapter.notifyDataSetChanged(); // 如果返回结果为 OK，再次通知适配器数据已更改
                }
                break;
            case REQUEST_CODE_CALL_PHONE: // 请求码为拨打电话权限
                fragmentAdapter.notifyDataSetChanged(); // 通知适配器数据已更改
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, TOAST_PERMISSION_GRANTED, Toast.LENGTH_SHORT).show(); // 弹出短暂提示消息
                    fragmentAdapter.notifyDataSetChanged(); // 如果返回结果为 OK，再次通知适配器数据已更改
                }
                break;
            default:
        }
    }

    // 处理权限请求的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PeopleListFragment.REQUEST_CODE_MODIFY: // 请求码为修改联系人
                break;
            case REQUEST_CODE_READ_CALL_LOG: // 请求码为读取通话记录权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndInitialize(); // 如果权限被授予，则初始化界面
                } else {
                    finish(); // 如果权限未被授予，则关闭活动
                }
                break;
            case REQUEST_CODE_CALL_PHONE: // 请求码为拨打电话权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeViewPagerAndTabs(); // 如果权限被授予，则初始化界面
                } else {
                    finish(); // 如果权限未被授予，则关闭活动
                }
                break;
        }
    }
}

package com.example.addressbook.activity;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.example.addressbook.R;
import com.example.addressbook.adapter.SelectAdapter;
import com.example.addressbook.bean.People;
import com.example.addressbook.bean.PeopleGroup;
import com.example.addressbook.utils.PinYinStringHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import android.app.AlertDialog;

import org.litepal.LitePal;

import java.util.*;

public class ModifyActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    public static final int[] PICTURES = {
            R.drawable.p21, R.drawable.p22, R.drawable.p23, R.drawable.p24, R.drawable.p25,
            R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10,
    };

    public static final String EXTRA_PEOPLE_DATA = "people_data";
    public static final String TITLE_EDIT_CONTACT = "编辑联系人";
    public static final String ERROR_PHONE_FORMAT = "电话号码格式不正确";
    public static final String ERROR_NAME_EMPTY = "用户名不能为空";
    public static final String SUCCESS_ADD_CONTACT = "添加联系人成功";
    public static final String SUCCESS_EDIT_CONTACT = "修改联系人成功";

    private People people; // 用于存储当前编辑的联系人信息
    private ImageView imageView; // 显示联系人头像的 ImageView
    private View infoGroupLayout; // 选择联系人分组的视图
    private String group; // 联系人分组
    private TextInputLayout nameTextInput; // 输入联系人姓名的 TextInputLayout
    private int pictureId = -1; // 联系人头像资源 ID，默认为 -1

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // 点击返回按钮时，结束当前活动
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_activity); // 设置当前活动的布局
        setTitle(TITLE_EDIT_CONTACT); // 设置标题栏标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回按钮

        TextView infoGroupTextView = findViewById(R.id.info_group); // 获取联系人分组显示的 TextView

        // 查询数据库，获取联系人分组列表
        List<PeopleGroup> groups = LitePal.findAll(PeopleGroup.class);
        String[] groupNames = new String[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            groupNames[i] = groups.get(i).getName();
        }

        infoGroupLayout = findViewById(R.id.info_group_rl); // 获取选择联系人分组的视图

        // 设置选择联系人分组视图的点击事件监听器
        infoGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 XPopup 显示联系人分组列表选择对话框
                new XPopup.Builder(ModifyActivity.this)
                        .asCenterList("", groupNames, new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                group = groupNames[position]; // 更新当前选择的联系人分组
                                infoGroupTextView.setText(group); // 更新显示的联系人分组名称
                            }
                        })
                        .show();
            }
        });

        imageView = findViewById(R.id.mod_img); // 获取显示联系人头像的 ImageView

        // 设置点击联系人头像 ImageView 的事件监听器
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = createDialog(); // 创建选择头像对话框
                dialog.show(); // 显示选择头像对话框
            }
        });

        // 获取联系人信息输入框的 TextInputLayout
        TextInputLayout telTextInput = findViewById(R.id.tel_text_input_layout);
        TextInputLayout infoTextInput = findViewById(R.id.info_tl);
        nameTextInput = findViewById(R.id.name_tl);

        // 获取从 MainActivity 传递过来的联系人数据
        Intent intent = getIntent();
        people = (People) intent.getSerializableExtra(EXTRA_PEOPLE_DATA);

        // 根据是否传递了联系人数据，初始化界面
        if (people == null) {
            infoGroupTextView.setText(""); // 如果没有传递联系人数据，则清空联系人分组显示
            pictureId = PICTURES[0]; // 默认设置第一个头像资源 ID
            Glide.with(this).load(pictureId).into(imageView); // 加载默认头像到 ImageView
        } else {
            group = people.getG(); // 获取联系人的分组
            pictureId = people.getPicturePath(); // 获取联系人的头像资源 ID
            infoGroupTextView.setText(group); // 显示联系人的分组名称
            Glide.with(this).load(people.getPicturePath()).into(imageView); // 加载联系人的头像到 ImageView
            nameTextInput.getEditText().setText(people.getName()); // 显示联系人的姓名
            telTextInput.getEditText().setText(people.getTelephone()); // 显示联系人的电话号码
            infoTextInput.getEditText().setText(people.getInformation()); // 显示联系人的额外信息
        }

        // 设置保存按钮的点击事件监听器
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的电话号码
                String tel = telTextInput.getEditText().getText().toString();
                if (tel.length() != 11) { // 如果电话号码不是11位
                    telTextInput.setErrorEnabled(true);
                    telTextInput.setError(ERROR_PHONE_FORMAT); // 显示错误提示
                    return;
                }
                telTextInput.setErrorEnabled(false); // 清除错误提示

                // 获取用户输入的联系人姓名
                String name = nameTextInput.getEditText().getText().toString();
                if (TextUtils.isEmpty(name)) { // 如果姓名为空
                    nameTextInput.setError(ERROR_NAME_EMPTY); // 显示错误提示
                    return;
                }

                String info = infoTextInput.getEditText().getText().toString(); // 获取用户输入的额外信息

                if (people == null) { // 如果没有传递联系人数据，表示添加新联系人
                    People newContact = new People();
                    newContact.setTelephone(tel);
                    newContact.setInformation(info);
                    newContact.setName(name);
                    newContact.setPicturePath(pictureId);
                    newContact.setId(System.currentTimeMillis());
                    newContact.setG(group);

                    String pinyin = PinYinStringHelper.getPingYin(name); // 获取姓名的全拼
                    newContact.setPinyin(pinyin);
                    String word = PinYinStringHelper.getAlpha(name); // 获取姓名的大写首字母或特殊字符
                    newContact.setWord(word);
                    String jianpin = PinYinStringHelper.getPinYinHeadChar(name); // 获取姓名的简拼
                    newContact.setJianpin(jianpin);

                    newContact.save(); // 将新联系人信息保存到数据库
                    Toast.makeText(ModifyActivity.this, SUCCESS_ADD_CONTACT, Toast.LENGTH_SHORT).show(); // 弹出添加成功提示
                } else { // 如果传递了联系人数据，表示编辑现有联系人
                    people.setTelephone(tel);
                    people.setName(name);
                    people.setG(group);
                    people.setPicturePath(pictureId);
                    people.setInformation(info);

                    String pinyin = PinYinStringHelper.getPingYin(name); // 获取姓名的全拼
                    people.setPinyin(pinyin);
                    String word = PinYinStringHelper.getAlpha(name); // 获取姓名的大写首字母或特殊字符
                    people.setWord(word);
                    String jianpin = PinYinStringHelper.getPinYinHeadChar(name); // 获取姓名的简拼
                    people.setJianpin(jianpin);

                    people.update(people.getId()); // 更新数据库中的联系人信息
                    Toast.makeText(ModifyActivity.this, SUCCESS_EDIT_CONTACT, Toast.LENGTH_SHORT).show(); // 弹出修改成功提示
                }
                finish();
            }
        });
    }

    // 创建选择联系人头像对话框
    public AlertDialog createDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
        dialog.setView(view);
        dialog.setOnDismissListener(this);
        AlertDialog alertDialog = dialog.create();
        RecyclerView recyclerView = view.findViewById(R.id.select_recycler_view);
        SelectAdapter selectAdapter = new SelectAdapter(this, alertDialog, imageView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectAdapter);

        return alertDialog;
    }

    // 选择联系人头像对话框关闭时的回调方法
    @Override
    public void onDismiss(DialogInterface dialog) {
        pictureId = SelectAdapter.getPictureId(); // 更新选择的联系人头像资源 ID
    }
}

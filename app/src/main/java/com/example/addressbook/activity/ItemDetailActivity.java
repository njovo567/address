package com.example.addressbook.activity;

import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.addressbook.R;
import com.example.addressbook.bean.People;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends BaseActivity {

    private String name = null;

    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.item_detail_activity);

        Intent intent = getIntent();

        People p = (People) intent.getSerializableExtra("people_data");
        name = p.getName();
        String tel = p.getTelephone();
        String info = p.getInformation();
        int picturePath = p.getPicturePath();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ImageView imageView = (ImageView) findViewById(R.id.title_img);
        Glide.with(this).load(picturePath).into(imageView);
        TextView t1 = (TextView) findViewById(R.id.text_view_item_name);
        TextView t2 = (TextView) findViewById(R.id.text_view_item_tel);
        TextView t3 = (TextView) findViewById(R.id.text_view_item_info);
        TextView t4 = (TextView) findViewById(R.id.text_view_item_group);
        t1.setText(name);
        t2.setText(tel);
        t3.setText(info);
        t4.setText(p.getG());
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent();
        intent.putExtra("people_name", name);
        setResult(RESULT_OK, intent);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("people_name", name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
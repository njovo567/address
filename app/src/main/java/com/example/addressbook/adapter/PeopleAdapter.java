package com.example.addressbook.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.addressbook.R;
import com.example.addressbook.activity.ItemDetailActivity;
import com.example.addressbook.activity.MainActivity;
import com.example.addressbook.activity.ModifyActivity;
import com.example.addressbook.bean.People;

import org.litepal.LitePal;

import java.util.*;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

	private Context context;
	private List<People> peoples;

	public PeopleAdapter(Context c, List<People> peoples){
		context = c;		// getActivity()
		this.peoples = peoples;
	}
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView imageView;
		TextView textView;
		ImageView menu;

		public ViewHolder(View v){
			super(v);
			imageView = (ImageView)v.findViewById(R.id.circle_image_view);
			textView = (TextView)v.findViewById(R.id.text_view);
			menu = (ImageView)v.findViewById(R.id.menu_image_view);
		}
	}

	private Context mContext;

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
		mContext = parent.getContext();
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_view_2_item,parent,false);

		final ViewHolder holder = new ViewHolder(view);

		view.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(context, ItemDetailActivity.class);	//go to detail
				int position = holder.getAdapterPosition();
				People p = peoples.get(position);
				intent.putExtra("tag","people");
				intent.putExtra("people_data",p);
				((MainActivity)context).startActivityForResult(intent,2);
			}
		});
		holder.menu.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				final int position = holder.getAdapterPosition();
				final People p = peoples.get(position);

				PopupMenu pm = new PopupMenu(context,v);
				pm.getMenuInflater().inflate(R.menu.popup_menu,pm.getMenu());
				pm.show();
				pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
					@Override
					public boolean onMenuItemClick(MenuItem item){
						int itemId = item.getItemId();
						if (itemId == R.id.call_item) {
							String telNumber = p.getTelephone();
							Intent i = new Intent(Intent.ACTION_CALL);
							i.setData(Uri.parse("tel:" + telNumber));
							context.startActivity(i);

						}   else if (itemId == R.id.detail_item) {
							Intent intent = new Intent(context, ItemDetailActivity.class);
							intent.putExtra("tag", "people");
							intent.putExtra("people_data", peoples.get(position));
							((MainActivity) context).startActivityForResult(intent, 2);
						} else if (itemId == R.id.modify_item) {
							Intent intent = new Intent(context, ModifyActivity.class);
							intent.putExtra("people_data", peoples.get(position));
							((MainActivity) context).startActivityForResult(intent, 1);
						} else if (itemId == R.id.delete_item) {
							delete(position);
						}
						return true;
					}
				});
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder,int position){
		People p = peoples.get(position);
		int i = Integer.valueOf(p.getPicturePath()).intValue();
		Glide.with(mContext).load(i).into(holder.imageView);
		holder.textView.setText(p.getName());
	}

	@Override
	public int getItemCount(){
		return peoples.size();
	}

	public void delete(int position){
		LitePal.deleteAll(People.class,"name = ?",peoples.get(position).getName());		//database
		peoples.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position,peoples.size()-position);
	}

}
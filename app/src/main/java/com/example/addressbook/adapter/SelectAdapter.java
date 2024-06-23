package com.example.addressbook.adapter;

import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.bumptech.glide.Glide;
import com.example.addressbook.R;

import android.content.Context;
import android.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
	
	public static int [] pictures ={
		R.drawable.p21,R.drawable.p22,R.drawable.p23,R.drawable.p24,R.drawable.p25,
		R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10,
	};
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView imageView;
		
		ViewHolder(View view){
			super(view);
			imageView = (ImageView)view.findViewById(R.id.select_image_view);
		}
	}
	
	private Context context;
	private AlertDialog dialog;
	private ImageView imageView;
	
	public SelectAdapter(Context c, AlertDialog d, ImageView image){
		context = c;
		dialog = d;
		imageView = image;
	}
	public static int pictureId = -1;
	public static int getPictureId(){
		return pictureId;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.recycler_view_select_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
		holder.imageView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				int position = holder.getAdapterPosition();
				
				Glide.with(context).load(pictures[position]).into(imageView);
				//((ModifyActivity)context).pictureId = pictures[position];
				pictureId = pictures[position];
				dialog.dismiss();
			}
		});
		return holder;
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder,int position){
		
		Glide.with(context).load(pictures[position]).into(holder.imageView);
	}
	
	@Override
	public int getItemCount(){
		return pictures.length;
	}
}
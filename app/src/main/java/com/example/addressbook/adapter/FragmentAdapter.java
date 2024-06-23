package com.example.addressbook.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.*;

public class FragmentAdapter extends FragmentStatePagerAdapter {
	
	private List<Fragment> fragments;
	
	private List<String> titles;
	
	public FragmentAdapter(FragmentManager fm, List<Fragment> fs, List<String> ts){
		super(fm);
		fragments = fs;
		titles = ts;
	}
	
	@Override
	public Fragment getItem(int position){
		return fragments.get(position);
	}
	
	@Override
	public CharSequence getPageTitle(int position){
		return titles.get(position);
	}
	
	@Override
	public int getCount(){
		return titles.size();
	}
	
	@Override
	public int getItemPosition(Object object){
		return PagerAdapter.POSITION_NONE;
	}
}
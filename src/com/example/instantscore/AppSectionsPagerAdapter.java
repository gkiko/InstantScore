package com.example.instantscore;

import java.util.ArrayList;

import com.example.instantscore.database.DBManager;
import com.example.instantscore.R;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragmentList;
	private MainSectionFragment fragment1;
	private SelectedSectionFragment fragment2;
	private transient Context c;
	
	public AppSectionsPagerAdapter(FragmentManager fm, Context c) {
		super(fm);
		this.c = c;
		
		fragment2 = new SelectedSectionFragment();
		fragment1 = new MainSectionFragment();
		
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		
		DBManager.init(c);
	}
	
	@Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	String[] titles = c.getResources().getStringArray(R.array.titles);
        return titles[position];
    }
    
    public void onRefresh(){
    	MainSectionFragment fragment = (MainSectionFragment)fragmentList.get(0);
    	fragment.fetchList();
    }
    
    public void onSubmit(){
    	MainSectionFragment fragment = (MainSectionFragment)fragmentList.get(0);
    	fragment.submitGames();
    }
    
}

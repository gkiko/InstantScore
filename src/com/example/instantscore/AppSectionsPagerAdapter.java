package com.example.instantscore;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	private transient Context c;
	
	public AppSectionsPagerAdapter(FragmentManager fm, Context c) {
		super(fm);
		this.c = c;
	}
	
	@Override
    public Fragment getItem(int i) {
		Fragment asd;
		Bundle b;
		if(i==0){
			b = new Bundle();
			b.putString("url", c.getString(R.string.url_get_submit));
			b.putString("live", "true");
			asd = new MainSectionFragment();
			asd.setArguments(b);
		}else{
			if(i==1){
				b = new Bundle();
				b.putString("url", c.getString(R.string.url_get_coming));
				b.putString("live", "false");
				asd = new MainSectionFragment();
				asd.setArguments(b);
			}else asd = new SelectedSectionFragment();
		}
        return asd;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	String[] titles = c.getResources().getStringArray(R.array.titles);
        return titles[position];
    }
    
    public int getLastFragmentIndex(){
    	return 2;
    }
    
}

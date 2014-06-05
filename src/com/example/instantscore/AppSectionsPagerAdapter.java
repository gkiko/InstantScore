package com.example.instantscore;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	private transient FragmentActivity c;
	
	public AppSectionsPagerAdapter(FragmentManager fm, FragmentActivity c) {
		super(fm);
		this.c = c;
	}
	
	@Override
    public Fragment getItem(int i) {
		Fragment asd;
        asd = (c.getSupportFragmentManager().findFragmentByTag(makeFragmentName(R.id.pager,getItemId(i))));
        Bundle b;
        if(asd == null){
            b = new Bundle();
            if(i==0){
                b.putString("live", "true");
                asd = new MainSectionFragment();
                asd.setArguments(b);
            }else{
                if(i==1){
                    b.putString("live", "false");
                    asd = new MainSectionFragment();
                    asd.setArguments(b);
                }else{
                    asd = new SelectedSectionFragment();
                }
            }
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

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
    
}

package com.example.projet_app_mobile.pageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter{
    private int nbTabs;
    private Channels_Fragment_Activity channels;
    private Messages_Fragment_Activity messages;
    private Profile_Fragment_Activity profile;

    public PageAdapter(FragmentManager fm, int nbTabs){
        super(fm);
        this.nbTabs = nbTabs;
        channels = new Channels_Fragment_Activity();
        messages = new Messages_Fragment_Activity();
        profile = new Profile_Fragment_Activity();
    }

    public Messages_Fragment_Activity getMessagesTab() {
        return messages;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return channels;
            case 1 :
                return  messages;
            case 2 :
                return profile;
            default :
                return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return nbTabs;
    }
}

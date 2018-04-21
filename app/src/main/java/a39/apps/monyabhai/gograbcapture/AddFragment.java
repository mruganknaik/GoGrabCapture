package a39.apps.monyabhai.gograbcapture;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add, container, false);
        TabLayout tabs=view.findViewById(R.id.add_tabs);
        ViewPager pager=view.findViewById(R.id.add_pager);
        pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager())
        {
            public int getCount()
            {
                return 2;
            }

            public Fragment getItem(int position)
            {
                switch (position)
                {
                    case 0:
                        return new AddProductFragment();
                    case 1:
                        return new AddServiceFragment();
                    default:
                        return null;
                }

            }

            @Nullable
            public CharSequence getPageTitle(int paramAnonymousInt)
            {
                switch (paramAnonymousInt)
                {

                    case 0:
                        return "Product ";
                    case 1:
                        return "Service";
                    default:
                        return null;
                }
            }
        });
        tabs.setupWithViewPager(pager);
        return view;
    }
}

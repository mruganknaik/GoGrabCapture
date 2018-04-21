package a39.apps.monyabhai.gograbcapture;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DiscoverFragment extends Fragment {

    LinearLayout gearlayout;
    LinearLayout servicelayout;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView welcomemsg;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_discover, container, false);
        welcomemsg=view.findViewById(R.id.textView4);
        String welcome_text="Welcome " + user.getDisplayName() + ", What are you looking for..?";
        welcomemsg.setText(welcome_text);
        user.getDisplayName();
        gearlayout=view.findViewById(R.id.linear_gear);
        servicelayout=view.findViewById(R.id.linear_services);
        gearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DiscoverGearTypeActivity.class);
                DiscoverFragment.this.startActivity(intent);
            }
        });
        servicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DiscoverServiceTypeActivity.class);
                DiscoverFragment.this.startActivity(intent);
            }
        });
        return view;
    }
}

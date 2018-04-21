
package a39.apps.monyabhai.gograbcapture;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Beans.User;


public class ProfileFragment extends Fragment {

    FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;
    User value;
    TextView name,phone_no,city,products,services;
    public ProfileFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_profile, container, false);

        name=view.findViewById(R.id.name_txt);
        phone_no=view.findViewById(R.id.mobile_txt_profile);
        city=view.findViewById(R.id.txt_city_profile);
        products=view.findViewById(R.id.txt_your_products_profile);
        services=view.findViewById(R.id.txt_your_services_profile);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/");
        reference.keepSynced(true);
        storageReference= FirebaseStorage.getInstance().getReference("ProfilePics/");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value= dataSnapshot.getValue(User.class);
                Glide.with(view.getContext()).load(value.getUrl()).into((ImageView) view.findViewById(R.id.profile_img));
                name.setText(value.getName().toUpperCase());
                phone_no.setText(value.getPhone_no());
                city.setText(value.getCity());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=user.getUid();
                Intent yourproductIntent=new Intent(getContext(),YourProductActivity.class);
                yourproductIntent.putExtra("reference",uid);
                startActivity(yourproductIntent);
            }
        });
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=user.getUid();
                Intent yourserviceIntent=new Intent(getContext(),YourServicesActivity.class);
                yourserviceIntent.putExtra("reference",uid);
                startActivity(yourserviceIntent);
            }
        });
        return view;
    }

}

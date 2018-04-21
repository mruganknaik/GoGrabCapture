package a39.apps.monyabhai.gograbcapture;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import Beans.Service;

import static android.app.Activity.RESULT_OK;

public class AddServiceFragment extends Fragment {

    ProgressBar progress;
    EditText title,description,price_day,years_used;
    Spinner type,city;
    ImageView i1,i2,i3,i4;
    static int imgcnt=0;
    Uri[]imguri=new Uri[4];
    Button submit;
    FirebaseUser user;
    DatabaseReference serref;
    StorageReference serstrref;
    View view;
    Service s1=new Service();
    public static final int CHOOSE_IMAGE =129;
    int incomingimg;

    public AddServiceFragment() {
        // Required empty public constructor
    }

    private void imageIntent(){
        Intent imeageIntent=new Intent();
        imeageIntent.setType("image/*");
        imeageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imeageIntent,"Choose Image"),CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK){
            CropImage.activity(data.getData()).setAspectRatio(4,3).start(getContext(), this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK){
            imguri[incomingimg]=CropImage.getActivityResult(data).getUri();


            switch (incomingimg){
                case 0:
                    i1.setImageURI(imguri[incomingimg]);
                    i2.setEnabled(true);
                    if(imgcnt==incomingimg){
                        imgcnt++;
                    }
                    break;
                case 1:
                    i2.setImageURI(imguri[incomingimg]);
                    i3.setEnabled(true);
                    if(imgcnt==incomingimg){
                        imgcnt++;
                    }
                    break;
                case 2:
                    i3.setImageURI(imguri[incomingimg]);
                    i4.setEnabled(true);
                    if(imgcnt==incomingimg){
                        imgcnt++;
                    }
                    break;
                case 3:
                    i4.setImageURI(imguri[incomingimg]);
                    if(imgcnt==incomingimg){
                        imgcnt++;
                    }
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_add_service, container, false);
        //cl=view.findViewById(R.id.constraint_layout_add_service);

        user= FirebaseAuth.getInstance().getCurrentUser();
        serref = FirebaseDatabase.getInstance().getReference("Services/");
        title=view.findViewById(R.id.title_add_service);
        description=view.findViewById(R.id.desc_add_service);
        price_day=view.findViewById(R.id.price_add_service);
        years_used=view.findViewById(R.id.experience_add_services);

        type=view.findViewById(R.id.type_aspn_add_service);
        city=view.findViewById(R.id.city_spn_add_service);


        i1=view.findViewById(R.id.img1_add_service);
        i2=view.findViewById(R.id.img2_add_service);
        i3=view.findViewById(R.id.img3_add_service);
        i4=view.findViewById(R.id.img4_add_service);

        progress=view.findViewById(R.id.add_service_progress);
        submit=view.findViewById(R.id.add_btn_add_aervices);

        i2.setEnabled(false);
        i3.setEnabled(false);
        i4.setEnabled(false);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingimg=0;
                imageIntent();
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingimg=1;
                imageIntent();
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingimg=2;
                imageIntent();
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingimg=3;
                imageIntent();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(title.getText().toString().isEmpty()){
                    title.setError("Title is Required");
                    title.requestFocus();
                    return;
                }
                if(years_used.getText().toString().isEmpty()){
                    years_used.setError("Experience is Required");
                    years_used.requestFocus();
                    return;
                }

                if(price_day.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(),"Price Must be Specified",Toast.LENGTH_LONG).show();
                    return;
                }

                if(imgcnt==0) {
                    Toast.makeText(view.getContext(),"At least one image Must be Specified",Toast.LENGTH_LONG).show();
                    return;
                }
                s1.setPrice(price_day.getText().toString());
                submit.setEnabled(false);
                submit.setBackgroundColor(Color.GRAY);
                progress.setVisibility(View.VISIBLE);
                s1.setUid(user.getUid());
                s1.setTitle(title.getText().toString());
                s1.setDescription(description.getText().toString());
                s1.setCity(city.getSelectedItem().toString());
                s1.setType(type.getSelectedItem().toString());
                s1.setExperiance(years_used.getText().toString());
                final String sid= serref.push().getKey();
                s1.setSid(sid);
                int t=0;
                for(;t<imgcnt;t++) {
                    serstrref = FirebaseStorage.getInstance().getReference("Services/" + sid + "/i"+t+1+".jpg");
                    serstrref.putFile(imguri[t]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (s1.getI1()==null) {
                                s1.setI1(taskSnapshot.getDownloadUrl().toString());
                                serref.child(sid).setValue(s1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(imgcnt==1){
                                            reload();
                                        }
                                    }
                                });
                                return;
                            }
                            else if (s1.getI2()==null){
                                s1.setI2(taskSnapshot.getDownloadUrl().toString());
                                serref.child(sid).setValue(s1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(imgcnt==2){
                                            reload();
                                        }
                                    }
                                });
                                return;
                            }
                            else if (s1.getI3()==null){
                                s1.setI3(taskSnapshot.getDownloadUrl().toString());
                                serref.child(sid).setValue(s1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(imgcnt==3){
                                            reload();
                                        }
                                    }
                                });
                                return;
                            }
                            else {
                                s1.setI4(taskSnapshot.getDownloadUrl().toString());
                                serref.child(sid).setValue(s1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(imgcnt==4){
                                            reload();
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
    void reload(){
        Toast.makeText(view.getContext(),"Service Added",Toast.LENGTH_SHORT).show();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}


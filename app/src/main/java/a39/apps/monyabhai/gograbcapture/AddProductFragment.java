package a39.apps.monyabhai.gograbcapture;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import Beans.Product;

import static android.app.Activity.RESULT_OK;


public class AddProductFragment extends Fragment {



    int incomingimg;
    ConstraintLayout cl;
    ProgressBar pbar;
    EditText title,description,price_day,price_sell,years_used;
    Spinner type,brand,model,city;
    CheckBox sell,rent;
    ImageView i1,i2,i3,i4;
    static int imgcnt=0;
    Uri []imguri=new Uri[4];
    Button submit;
    FirebaseUser user;
    DatabaseReference pdtref;
    StorageReference pdtstrref;
    View view;
    static Product p1=new Product();

    public static final int CHOOSE_IMAGE =123 ;


    public AddProductFragment() {

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
            CropImage.activity(data.getData()).setAspectRatio(1,1).start(getContext(), this);
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
        view= inflater.inflate(R.layout.fragment_add_product, container, false);
        cl=view.findViewById(R.id.mainlayout);

        user= FirebaseAuth.getInstance().getCurrentUser();
        pdtref= FirebaseDatabase.getInstance().getReference("Products/");
        title=view.findViewById(R.id.txt_title_add_product);
        description=view.findViewById(R.id.txt_desc_add_product);
        price_day=view.findViewById(R.id.txt_priceperday_add_product);
        price_sell=view.findViewById(R.id.txt_pricesell_add_product);
        years_used=view.findViewById(R.id.txt_year_add_product);

        type=view.findViewById(R.id.spn_type_add_product);
        brand=view.findViewById(R.id.spn_brand_add_product);
        model=view.findViewById(R.id.spn_model_add_product);
        city=view.findViewById(R.id.spn_city_add_product);

        sell=view.findViewById(R.id.cb_sell_add_product);
        rent=view.findViewById(R.id.cb_rent_add_product);

        i1=view.findViewById(R.id.img_1_add_product);
        i2=view.findViewById(R.id.img_2_add_product);
        i3=view.findViewById(R.id.img_3_add_product);
        i4=view.findViewById(R.id.img_4_add_product);

        pbar=view.findViewById(R.id.add_product_progress);
        pbar.setVisibility(View.INVISIBLE);

        submit=view.findViewById(R.id.btn_add_product_add);

        i2.setEnabled(false);
        i3.setEnabled(false);
        i4.setEnabled(false);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        brand.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.brands_cam)));
                        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch (position){
                                    case 0:
                                        model.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.canon_models)));
                                        break;
                                    case 1:
                                        model.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.nikon_brands)));
                                        break;
                                    case 2:
                                        model.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.pana_brands)));
                                        break;
                                    case 3:
                                        model.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sony_brands)));
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 1:
                        brand.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.brands_lens)));
                        break;
                    case 2:
                        brand.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.brands_storage)));
                        break;
                    case 3:
                        brand.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.brands_flash)));
                        break;
                    case 4:
                        brand.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.brands_audio)));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        price_sell.setEnabled(false);

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


        sell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sell.isChecked()){
                    price_sell.setEnabled(true);
                }
                else {
                    price_sell.setEnabled(false);
                }
            }
        });

        rent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rent.isChecked()){
                    price_day.setEnabled(true);
                }
                else {
                    price_day.setEnabled(false);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //p1=new Product();
                if(!(sell.isChecked()|rent.isChecked())){
                    Toast.makeText(view.getContext(),"At least One Category Must be Selected",Toast.LENGTH_LONG).show();
                    return;
                }
                if(title.getText().toString().isEmpty()){
                    title.setError("Title is Required");
                    title.requestFocus();
                    return;
                }
                if(years_used.getText().toString().isEmpty()){
                    years_used.setError("Purchased Year is Required");
                    years_used.requestFocus();
                    return;
                }

                if(sell.isChecked()){
                    if(price_sell.getText().toString().isEmpty()){
                        Toast.makeText(view.getContext(),"Price Must be Specified",Toast.LENGTH_LONG).show();
                        return;
                    }
                    p1.setPrice_sell(price_sell.getText().toString());
                }
                if(rent.isChecked()){
                    if(price_day.getText().toString().isEmpty()){
                        Toast.makeText(view.getContext(),"Price Must be Specified",Toast.LENGTH_LONG).show();
                        return;
                    }
                    p1.setPrice_day(price_day.getText().toString());
                }

                if(imgcnt==0)
                {
                    Toast.makeText(view.getContext(),"At least one image Must be Specified",Toast.LENGTH_LONG).show();
                    return;
                }
                pbar.setVisibility(View.VISIBLE);
                submit.setEnabled(false);
                submit.setBackgroundColor(Color.GRAY);
                p1.setUid(user.getUid());
                p1.setTitle(title.getText().toString());
                p1.setDescription(description.getText().toString());
                p1.setCity(city.getSelectedItem().toString());
                p1.setBran(brand.getSelectedItem().toString());
                p1.setModel(model.getSelectedItem().toString());
                p1.setType(type.getSelectedItem().toString());
                p1.setYear(years_used.getText().toString());
                final String pid=pdtref.push().getKey();
                p1.setPid(pid);
                int t=0;
                for(;t<imgcnt;t++) {
                    pdtstrref = FirebaseStorage.getInstance().getReference("Products/" + pid + "/i"+t+1+".jpg");
                    pdtstrref.putFile(imguri[t]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (p1.getI1()==null) {
                                p1.setI1(taskSnapshot.getDownloadUrl().toString());
                                pdtref.child(pid).setValue(p1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(imgcnt==1){
                                            reload();

                                        }
                                    }
                                });
                                return;
                            }
                            else if (p1.getI2()==null){
                                p1.setI2(taskSnapshot.getDownloadUrl().toString());
                                pdtref.child(pid).setValue(p1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(imgcnt==2){
                                            reload();
                                        }
                                    }
                                });
                                return;
                            }
                            else if (p1.getI3()==null){
                                p1.setI3(taskSnapshot.getDownloadUrl().toString());
                                pdtref.child(pid).setValue(p1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                p1.setI4(taskSnapshot.getDownloadUrl().toString());
                                pdtref.child(pid).setValue(p1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        Toast.makeText(view.getContext(),"Product Added",Toast.LENGTH_SHORT).show();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}



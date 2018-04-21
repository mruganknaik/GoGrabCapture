package a39.apps.monyabhai.gograbcapture;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;

import Beans.User;

public class DisplayNameActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE =101 ;
    ImageView profile_img;
    EditText txt_name;
    TextView txt_dob;
    Button submit_name_btn;
    ProgressBar progressbar;
    Toolbar mtool;
    int state= 0;
    String name;
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/");
    User user1=new User();
    Spinner city_txt;
    UserProfileChangeRequest profileChangeRequest;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    private Uri uriProfileImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_name);

        Intent i=getIntent();
        String reference=i.getStringExtra("reference");


        profile_img=findViewById(R.id.profile_img);
        txt_dob=findViewById(R.id.date_txt);
        txt_name = findViewById(R.id.name_txt);
        mtool=findViewById(R.id.reg_appbar);
        submit_name_btn = findViewById(R.id.submit_name_btn);
        city_txt=findViewById(R.id.city_txt);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.cities));
        city_txt.setAdapter(adapter);


        if(!(reference == null))
        {
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user1=dataSnapshot.getValue(User.class);
                    state=1;
                    Glide.with(getApplicationContext()).load(user1.getUrl()).into(profile_img);
                    txt_dob.setText(user1.getDob());
                    name=user1.getName();
                    txt_name.setText(user1.getName());
                    String[]cities=getResources().getStringArray(R.array.cities);
                    int l=0;
                    for(int i=0;i<cities.length;i++){
                        String c=cities[i];
                        if (c.equals(user1.getCity())){
                            l=i;
                            break;
                        }
                    }
                    city_txt.setSelection(l);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        setSupportActionBar(mtool);
        getSupportActionBar().setTitle("Update Information");

        myRef = FirebaseDatabase.getInstance().getReference("Users");
        txt_name.requestFocus();

        txt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(DisplayNameActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,onDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date=day+"/"+month+"/"+year;
                txt_dob.setText(date);
            }
        };

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imeageIntent=new Intent();
                imeageIntent.setType("image/*");
                imeageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imeageIntent,"Choose Image"),CHOOSE_IMAGE);
            }
        });

        submit_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_name.getText().toString().isEmpty()){
                    txt_name.setError("Name is Required");
                    txt_name.requestFocus();
                    return;
                }
                if (txt_dob.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Date of Birth must Be Specified",Toast.LENGTH_LONG).show();
                    return;
                }
                if(uriProfileImg==null&&state==0) {
                    Toast.makeText(getApplicationContext(),"Click on Picture and Upload Profile Image",Toast.LENGTH_LONG).show();
                    return;
                }


                user1.setUid(user.getUid());
                user1.setName(txt_name.getText().toString());
                user1.setDob(txt_dob.getText().toString());
                user1.setCity(city_txt.getSelectedItem().toString());
                user1.setPhone_no(user.getPhoneNumber());

                progressbar=findViewById(R.id.progress_name);
                progressbar.setVisibility(View.VISIBLE);

                txt_name.setEnabled(false);
                submit_name_btn.setEnabled(false);
                if(state==2){
                    uploadImageToFirebase();
                }
                else {
                    if(name.equals(user1.getName())){
                        saveToDatabase();
                    }
                    else{
                        changeName();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK){
            CropImage.activity(data.getData()).setAspectRatio(1,1).start(DisplayNameActivity.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK){
            uriProfileImg=CropImage.getActivityResult(data).getUri();
            state=2;
            profile_img.setImageURI(uriProfileImg);
        }
    }

    private void uploadImageToFirebase() {
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("ProfilePics/"+user.getUid()+".jpg");
        storageReference.putFile(uriProfileImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                user1.setUrl( taskSnapshot.getDownloadUrl().toString());
                profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(user1.getName()).setPhotoUri(taskSnapshot.getDownloadUrl()).build();
                changeName();
            }
        });
    }

    private void onComplete(){
        Toast.makeText(getApplicationContext(),"Information Updated",Toast.LENGTH_LONG).show();
        Intent homeIntet=new Intent(DisplayNameActivity.this,HomeActivity.class);
        startActivity(homeIntet);
        finish();
    }
    private void changeName(){
        profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(user1.getName()).build();
        user.updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                saveToDatabase();
            }
        });
    }
    private void saveToDatabase(){
        myRef.child(user.getUid()).setValue(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onComplete();
            }
        });
    }
}

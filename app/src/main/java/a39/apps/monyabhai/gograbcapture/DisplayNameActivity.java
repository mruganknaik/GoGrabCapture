package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Beans.User;

public class DisplayNameActivity extends AppCompatActivity {

    EditText txt_name,txt_dob;
    Button submit_name_btn;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    Toolbar mtool;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    String name,dob,city;
    String [] cities;
    AutoCompleteTextView city_txt;
    UserProfileChangeRequest profileChangeRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_name);

        txt_dob=findViewById(R.id.date_txt);
        txt_name = findViewById(R.id.name_txt);
        mtool=findViewById(R.id.reg_appbar);
        submit_name_btn = findViewById(R.id.submit_name_btn);
        city_txt=findViewById(R.id.city_txt);

        setSupportActionBar(mtool);
        getSupportActionBar().setTitle("Update Info");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        user=mAuth.getCurrentUser();

        cities=getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,cities);
        city_txt.setAdapter(adapter);
        txt_name.requestFocus();

        submit_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=txt_name.getText().toString();
                dob=txt_dob.getText().toString();
                city=city_txt.toString();
                User user1=new User(name,user.getPhoneNumber(),dob,city);
                myRef.child(user.getUid()).setValue(user1);

                progressbar=findViewById(R.id.progress_name);
                progressbar.setVisibility(View.VISIBLE);
                profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                txt_name.setEnabled(false);
                submit_name_btn.setEnabled(false);

                user.updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Name Updated",Toast.LENGTH_LONG).show();
                        Intent homeIntet=new Intent(DisplayNameActivity.this,HomeActivity.class);
                        startActivity(homeIntet);
                        finish();
                    }
                });
            }
        });

    }
    }

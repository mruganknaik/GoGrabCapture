package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class DisplayNameActivity extends AppCompatActivity {

    EditText txt_name;
    Button submit_name_btn;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    Toolbar mtool;
    //UserProfileChangeRequest profileChangeRequest
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_name);
        txt_name = findViewById(R.id.name_txt);
        mtool=findViewById(R.id.reg_appbar);
        setSupportActionBar(mtool);
        getSupportActionBar().setTitle("Update Info");
        mAuth = FirebaseAuth.getInstance();
        submit_name_btn = findViewById(R.id.submit_name_btn);
        submit_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar=findViewById(R.id.progress_name);
                progressbar.setVisibility(View.VISIBLE);
                FirebaseUser user = mAuth.getCurrentUser();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txt_name.getText().toString()).build();
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

package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity {
        Toolbar mtool;
        EditText verify_txt;
        EditText phone_txt;
        TextInputLayout phone_txt_layout;
        TextInputLayout verify_txt_layout;
        FirebaseAuth mAuth;
        ProgressBar progress;
        Button verify_btn;
        int btnType=0;
        TextView errtxt;
        String mVerificationId;
        PhoneAuthProvider.ForceResendingToken mResendToken;
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Registering Views

        mAuth=FirebaseAuth.getInstance();
        phone_txt_layout=findViewById(R.id.txt_phone_layout);
        verify_txt_layout=findViewById(R.id.txt_verify_layout);
        phone_txt=findViewById(R.id.txt_phone);
        verify_txt=findViewById(R.id.txt_verify);
        progress=findViewById(R.id.progress_verify);
        verify_btn=findViewById(R.id.btn_signup);
        mtool=findViewById(R.id.signup_appbar);
        errtxt=findViewById(R.id.err_signup_txt);
        setSupportActionBar(mtool);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Callbacks
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {

            }
            @Override
            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                btnType=1;
                verify_btn.setText("GET VERIFIED");
                verify_btn.setEnabled(true);
                verify_txt.setEnabled(true);
                progress.setVisibility(View.INVISIBLE);
                verify_txt_layout.setVisibility(View.VISIBLE);
            }
        };

        //Listener
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnType==0) {
                    String phoneNo = phone_txt.getText().toString();
                    if (phoneNo.isEmpty()) {
                        phone_txt.setError("Phone No is Required");
                        phone_txt.requestFocus();
                        return;
                    }
                    else if (phoneNo.length()<10){
                        phone_txt.setError("Enter Valid Phone No");
                        phone_txt.requestFocus();
                        return;
                    }
                    phone_txt.setEnabled(false);
                    verify_btn.setEnabled(false);
                    verify_txt.setEnabled(false);
                    progress.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60, TimeUnit.SECONDS, SignUpActivity.this, mCallbacks);
                }
                else{
                    progress.setVisibility(View.VISIBLE);
                    verify_btn.setEnabled(false);
                    String veri_code=verify_txt.getText().toString();
                    if(veri_code.length()<6){
                        verify_txt.setError("Enter Valid Verification Code");
                        verify_txt.requestFocus();
                        return;
                    }
                    PhoneAuthCredential credential= new PhoneAuthCredential(mVerificationId,veri_code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    if (user.getDisplayName()==null) {
                        Intent dispIntent=new Intent(SignUpActivity.this,DisplayNameActivity.class);
                        startActivity(dispIntent);
                        finish();
                    }
                    else {
                        Intent homeIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                    }
                }
                else {
                    verify_btn.setEnabled(true);
                    verify_txt.setEnabled(true);
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        verify_txt.setError("Invalid Verification Code");
                    }
                    else{
                        errtxt.setVisibility(View.VISIBLE);
                        errtxt.setText("Unknown Error Odoured. Please Try Again...");
                    }
                }
            }
        });
    }
}
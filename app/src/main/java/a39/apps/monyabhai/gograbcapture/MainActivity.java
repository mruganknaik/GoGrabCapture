package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button signup_btn;
    Intent signup_intent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup_btn = findViewById(R.id.btn_signup);
        signup_intent = new Intent(this, SignUpActivity.class);
        signup_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.this.signup_intent);
            }
        });
    }
}

package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    Toolbar mtool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if (user==null){
            Intent AuthIntent=new Intent(this,MainActivity.class);
            startActivity(AuthIntent);
            finish();
        }
        mtool=findViewById(R.id.home_appbar);
        setSupportActionBar(mtool);
        getSupportActionBar().setTitle("Go Grab n Capture");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.home_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signoutoption:
                mAuth.signOut();
                Intent LoginIntent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(LoginIntent);
                finish();
        }
        return true;
    }
}

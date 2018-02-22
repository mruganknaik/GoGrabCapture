package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    Toolbar mtool;
    FrameLayout main_frame;
    BottomNavigationView main_nav;
    DiscoverFragment discoverFragment;
    AddFragment addFragment;
    ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        discoverFragment= new DiscoverFragment();
        addFragment= new AddFragment();
        profileFragment= new ProfileFragment();
        if (user==null){
            Intent AuthIntent=new Intent(this,MainActivity.class);
            startActivity(AuthIntent);
            finish();
        }
        mtool=findViewById(R.id.home_appbar);
        setSupportActionBar(mtool);
        getSupportActionBar().setTitle("Go Grab n Capture");
        main_frame=findViewById(R.id.main_frame);
        main_nav=findViewById(R.id.main_nav_bar);
       main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_search :
                        setFragment(discoverFragment);
                        break;
                    case R.id.nav_add :
                        setFragment(addFragment);
                        break;
                    case R.id.nav_profile :
                        setFragment(profileFragment);
                        break;
                }
                return true;

            }
        });
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
    public void setFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
}

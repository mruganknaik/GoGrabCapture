package a39.apps.monyabhai.gograbcapture;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {

    Fragment addFragment;
    int state = 0;
    Fragment discoverFragment;
    FirebaseAuth mAuth;
    FrameLayout main_frame;
    BottomNavigationView main_nav;
    Toolbar mtool;
    Fragment notificationFragment;
    Fragment profileFragment;
    FirebaseUser user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (state == 0) {
            super.onCreate(savedInstanceState);
            state = 1;
        }
        if (((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            setContentView(R.layout.activity_home);
            discoverFragment = new DiscoverFragment();
            addFragment = new AddFragment();
            profileFragment = new ProfileFragment();
            notificationFragment = new NotificationFragment();
            if (this.user == null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            mtool = findViewById(R.id.discover_appbar);
            setSupportActionBar(this.mtool);
            getSupportActionBar().setTitle("Go Grab n Capture");
            main_frame =  findViewById(R.id.main_frame);
            main_nav =  findViewById(R.id.main_nav_bar);
            setFragment(discoverFragment);
            BottomNavigationViewHelper.removeShiftMode(this.main_nav);
            this.main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_add:
                            setFragment(addFragment);
                            break;
                        case R.id.nav_notif:
                            setFragment(notificationFragment);
                            break;
                        case R.id.nav_profile:
                            setFragment(profileFragment);
                            break;
                        case R.id.nav_search:
                            setFragment(discoverFragment);
                            break;
                    }
                    return true;
                }
            });
            return;
        }
        else {
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onCreate(savedInstanceState);
                }
            });
            ad.show();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editprofile_option:
                Intent EditProfileIntent = new Intent(this, DisplayNameActivity.class);
                EditProfileIntent.putExtra("reference", "Edit This Profile");
                startActivity(EditProfileIntent);
                finish();
                break;
            case R.id.signoutoption:
                this.mAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return true;
    }
    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).commit();
    }
}

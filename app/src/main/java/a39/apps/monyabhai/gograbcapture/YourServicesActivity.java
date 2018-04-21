package a39.apps.monyabhai.gograbcapture;
import Beans.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class YourServicesActivity extends AppCompatActivity {
    String reference;
    DatabaseReference serref;
    ListView service_list;
    ArrayList<Service> services = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_services);
        reference = getIntent().getStringExtra("reference");
        service_list =  findViewById(R.id.your_service_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_your_service));
        getSupportActionBar().setTitle("Your Services");
        serref = FirebaseDatabase.getInstance().getReference("Services/");
        serref.keepSynced(true);
        serref.orderByChild("uid").equalTo(reference).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                services.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    services.add(ds.getValue(Service.class));
                }
                service_list.setAdapter(new ServiceAdapter(YourServicesActivity.this, services));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.service_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sid = ((services.get(position)).getSid());
                Intent serviceIntent = new Intent(YourServicesActivity.this, ServiceActivity.class);
                serviceIntent.putExtra("sid", sid);
                startActivity(serviceIntent);
            }
        });
    }
}

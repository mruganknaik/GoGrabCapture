package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Beans.Service;

public class DiscoverServiceListActivity extends AppCompatActivity {

    List<Service> services=new ArrayList<>();
    DatabaseReference serref;
    ListView service_list;
    String reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_service_list);
        Intent intent=getIntent();
        reference=intent.getStringExtra("reference");
        Toolbar mtool = findViewById(R.id.discover_service_appbar);
        service_list=findViewById(R.id.list_service_list);
        setSupportActionBar(mtool);
        getSupportActionBar().setTitle(reference);
        serref= FirebaseDatabase.getInstance().getReference("Services/");
        serref.keepSynced(true);
        Query q=serref.orderByChild("type").equalTo(reference);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                services.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Service s=ds.getValue(Service.class);
                    services.add(s);
                }
                ServiceAdapter adapter=new ServiceAdapter (DiscoverServiceListActivity.this,services);
                service_list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        service_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sid= services.get(position).getSid();
                Intent serviceIntent=new Intent(DiscoverServiceListActivity.this,ServiceActivity.class);
                serviceIntent.putExtra("sid",sid);
                startActivity(serviceIntent);
            }
        });
    }
}

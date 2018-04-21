package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DiscoverServiceTypeActivity extends AppCompatActivity {
    Intent intent;
    String[] items;
    ListView listView_service_type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_service_type);
        this.intent = new Intent(this, DiscoverServiceListActivity.class);
        this.listView_service_type =  findViewById(R.id.service_list);
        this.items = getResources().getStringArray(R.array.service_type);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_service_type));
        getSupportActionBar().setTitle("Choose Category");
        this.listView_service_type.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiscoverServiceTypeActivity.this.intent.putExtra("reference", DiscoverServiceTypeActivity.this.items[position]);
                DiscoverServiceTypeActivity.this.startActivity(DiscoverServiceTypeActivity.this.intent);
            }
        });
    }
}

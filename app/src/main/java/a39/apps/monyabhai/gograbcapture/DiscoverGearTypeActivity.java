package a39.apps.monyabhai.gograbcapture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DiscoverGearTypeActivity extends AppCompatActivity {
    int[] imgs = new int[]{R.drawable.camera, R.drawable.lens, R.drawable.memory_card, R.drawable.flash, R.drawable.microphone};
    Intent intent;
    String[] items;
    ListView listView_geartype;

    class CustomAdopter extends BaseAdapter {
        CustomAdopter() {
        }

        public int getCount() {
            return DiscoverGearTypeActivity.this.imgs.length;
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View v1 = DiscoverGearTypeActivity.this.getLayoutInflater().inflate(R.layout.list_gear_type, null);
            TextView t1 = v1.findViewById(R.id.txt_gear_type_list);
            ((ImageView) v1.findViewById(R.id.img_gear_type_list)).setImageResource(DiscoverGearTypeActivity.this.imgs[i]);
            t1.setText(DiscoverGearTypeActivity.this.items[i]);
            return v1;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_gear_type);
        this.intent = new Intent(this, DiscoverGearListActivity.class);
        this.listView_geartype = findViewById(R.id.geartypelist);
        this.items = getResources().getStringArray(R.array.brands);
        setSupportActionBar((Toolbar) findViewById(R.id.discover_type_appbar));
        getSupportActionBar().setTitle("Choose Category");
        this.listView_geartype.setAdapter(new CustomAdopter());
        this.listView_geartype.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiscoverGearTypeActivity.this.intent.putExtra("reference", DiscoverGearTypeActivity.this.items[position]);
                DiscoverGearTypeActivity.this.startActivity(DiscoverGearTypeActivity.this.intent);
            }
        });
    }
}

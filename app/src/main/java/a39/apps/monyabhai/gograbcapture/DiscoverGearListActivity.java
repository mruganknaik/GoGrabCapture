package a39.apps.monyabhai.gograbcapture;

import Beans.Product;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class DiscoverGearListActivity extends AppCompatActivity {
    Spinner model_spn;
    DatabaseReference pdtref;
    ListView product_list;
    ArrayList<Product> products = new ArrayList<>();
    String reference;
    ImageView search_icon;
    EditText search_txt;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_gear_list);
        model_spn = findViewById(R.id.spn_models_gearlst);
        reference = getIntent().getStringExtra("reference");
        search_txt =  findViewById(R.id.txt_search);
        search_icon = findViewById(R.id.search_icon);
        product_list =  findViewById(R.id.product_list_list);
        findViewById(R.id.focous_layout).requestFocus();
        setSupportActionBar((Toolbar) findViewById(R.id.discover_appbar));
        getSupportActionBar().setTitle(this.reference);
        String str = this.reference;
        switch (str) {
            case "Camera":
                this.model_spn.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.brands_cam_sort)));
                break;
            case "Lens":
                this.model_spn.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.brands_lens_sort)));
                break;
            case "Storage":
                this.model_spn.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.brands_storage_sort)));
                break;
            case "Audio":
                this.model_spn.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.brands_audio_sort)));
                break;
            case "Flash":
                this.model_spn.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.brands_flash_sort)));
                break;
        }
        pdtref = FirebaseDatabase.getInstance().getReference("Products/");
        pdtref.keepSynced(true);
        pdtref.orderByChild("type").equalTo(this.reference).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DiscoverGearListActivity.this.products.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    products.add(ds.getValue(Product.class));
                }
                DiscoverGearListActivity.this.product_list.setAdapter(new ProductAdapter(DiscoverGearListActivity.this, DiscoverGearListActivity.this.products));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        product_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = (DiscoverGearListActivity.this.products.get(position)).getPid();
                Intent productIntent = new Intent(DiscoverGearListActivity.this, ProductActivity.class);
                productIntent.putExtra("sid", pid);
                DiscoverGearListActivity.this.startActivity(productIntent);
            }
        });
        search_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yourproductIntent = new Intent(DiscoverGearListActivity.this, SearchResultActivity.class);
                yourproductIntent.putExtra("reference", DiscoverGearListActivity.this.search_txt.getText().toString());
                DiscoverGearListActivity.this.startActivity(yourproductIntent);
            }
        });
    }
}

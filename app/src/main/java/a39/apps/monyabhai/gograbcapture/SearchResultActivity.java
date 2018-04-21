package a39.apps.monyabhai.gograbcapture;

import Beans.Product;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    DatabaseReference pdtref;
    ListView product_list;
    ArrayList<Product> products = new ArrayList<>();
    String reference;
    TextView search_count;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.reference = getIntent().getStringExtra("reference").toUpperCase();
        this.product_list =  findViewById(R.id.search_result_view);
        this.search_count =  findViewById(R.id.search_count);
        setSupportActionBar((Toolbar) findViewById(R.id.search_appbar));
        getSupportActionBar().setTitle("Search Results");
        this.pdtref = FirebaseDatabase.getInstance().getReference("Products/");
        this.pdtref.keepSynced(true);
        this.pdtref.orderByChild("model").startAt(this.reference).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String search_cnt="Total " + dataSnapshot.getChildrenCount() + " Results Found";
                SearchResultActivity.this.search_count.setText(search_cnt);
                SearchResultActivity.this.products.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SearchResultActivity.this.products.add(ds.getValue(Product.class));
                }
                SearchResultActivity.this.product_list.setAdapter(new ProductAdapter(SearchResultActivity.this, SearchResultActivity.this.products));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.product_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = (SearchResultActivity.this.products.get(position)).getPid();
                Intent productIntent = new Intent(SearchResultActivity.this, ProductActivity.class);
                productIntent.putExtra("sid", pid);
                SearchResultActivity.this.startActivity(productIntent);
            }
        });
    }
}

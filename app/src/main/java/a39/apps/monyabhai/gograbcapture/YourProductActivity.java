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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class YourProductActivity extends AppCompatActivity {
    DatabaseReference pdtref;
    ListView product_list;
    ArrayList<Product> products = new ArrayList<>();
    String reference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_product);
        reference = getIntent().getStringExtra("reference");
        product_list =  findViewById(R.id.your_product_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_your_product));
        getSupportActionBar().setTitle("Your Products");
        pdtref = FirebaseDatabase.getInstance().getReference("Products/");
        pdtref.keepSynced(true);
        pdtref.orderByChild("uid").equalTo(reference).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    products.add( ds.getValue(Product.class));
                }
                product_list.setAdapter(new ProductAdapter(YourProductActivity.this, products));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        product_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = (products.get(position)).getPid();
                Intent productIntent = new Intent(YourProductActivity.this, ProductActivity.class);
                productIntent.putExtra("sid", pid);
                startActivity(productIntent);
            }
        });
    }
}

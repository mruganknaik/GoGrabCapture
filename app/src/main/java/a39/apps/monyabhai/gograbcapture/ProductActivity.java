package a39.apps.monyabhai.gograbcapture;

import Beans.GenericNotification;
import Beans.Product;
import Beans.Request;
import Beans.User;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductActivity extends AppCompatActivity {

    TextView brand,city,decription,model,price_rent,price_sell,type,username;
    ImageView i1,i2,i3,i4,i_main,user_image;
    Toolbar mtool;
    User myu;
    DatabaseReference myuref,pref,uref;
    Product pdt_obj;
    String pid;
    StorageReference pstrref;
    Button req_btn;
    User user1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        i1 =  findViewById(R.id.i1_product_layout);
        i2 =  findViewById(R.id.i2_product_layout);
        i3 =  findViewById(R.id.i3_product_layout);
        i4 =  findViewById(R.id.i4_product_layout);

        i2.setVisibility(View.INVISIBLE);
        i3.setVisibility(View.INVISIBLE);
        i4.setVisibility(View.INVISIBLE);

        i_main =  findViewById(R.id.img_main_product_layout);
        user_image =  findViewById(R.id.pdt_image_user);

        type =  findViewById(R.id.pdt_txt_type);
        brand =  findViewById(R.id.pdt_txt_brand);
        model =  findViewById(R.id.pdt_text_model);
        city =  findViewById(R.id.pdt_txt_city);
        price_rent =  findViewById(R.id.pdt_txt_pric_rent);
        price_sell =  findViewById(R.id.pdt_txt_pric_buy);
        decription =  findViewById(R.id.pdt_txt_description);
        username =  findViewById(R.id.pdt_txt_user_name);

        req_btn =  findViewById(R.id.product_req_button);

        mtool =  findViewById(R.id.product_appbar);
        setSupportActionBar(mtool);

        pid = getIntent().getStringExtra("sid");
        pstrref = FirebaseStorage.getInstance().getReference("Products/" + pid + "/");
        pref = FirebaseDatabase.getInstance().getReference("Products/" + pid + "/");
        pref.keepSynced(true);

        pref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pdt_obj =  dataSnapshot.getValue(Product.class);
                getSupportActionBar().setTitle(pdt_obj.getTitle());

                if (pdt_obj.getUid().equals(user.getUid())) {

                    req_btn.setText(R.string.delete_pdt);
                    req_btn.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                }
                type.setText(pdt_obj.getType());
                brand.setText(pdt_obj.getBran());
                model.setText(pdt_obj.getModel());
                city.setText(pdt_obj.getCity());
                String p_day = "₹" + pdt_obj.getPrice_day();
                if (!(p_day.isEmpty())) {

                    price_rent.setText(p_day);
                } else {
                    price_rent.setText("-");
                }
                String p_sell ="₹" + pdt_obj.getPrice_sell();
                if (p_sell.isEmpty()) {
                    price_sell.setText("-");
                } else {
                    price_sell.setText( p_sell);
                }
                decription.setText(pdt_obj.getDescription());

                uref = FirebaseDatabase.getInstance().getReference("Users/" + pdt_obj.getUid() + "/");
                uref.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user1 =  dataSnapshot.getValue(User.class);
                        username.setText(user1.getName());
                        Glide.with(ProductActivity.this).load(user1.getUrl()).into(user_image);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Glide.with(ProductActivity.this).load(pdt_obj.getI1()).into(i_main);
                Glide.with(ProductActivity.this).load(pdt_obj.getI1()).into(i1);
                if (pdt_obj.getI2() != null) {
                    i2.setVisibility(View.VISIBLE);
                    Glide.with(ProductActivity.this).load(pdt_obj.getI2()).into(i2);
                }
                if (pdt_obj.getI3() != null) {
                    i3.setVisibility(View.VISIBLE);
                    Glide.with(ProductActivity.this).load(pdt_obj.getI3()).into(i3);
                }
                if (pdt_obj.getI4() != null) {
                    i4.setVisibility(View.VISIBLE);
                    Glide.with(ProductActivity.this).load(pdt_obj.getI4()).into(i4);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        i1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ProductActivity.this).load(pdt_obj.getI1()).into(i_main);
            }
        });

        i2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i2.getVisibility() == View.VISIBLE) {
                    Glide.with(ProductActivity.this).load(pdt_obj.getI2()).into(i_main);
                }
            }
        });

        i3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i3.getVisibility() == View.VISIBLE) {
                    Glide.with(ProductActivity.this).load(pdt_obj.getI3()).into(i_main);
                }
            }
        });

        i4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i4.getVisibility() == View.VISIBLE) {
                    Glide.with(ProductActivity.this).load(pdt_obj.getI4()).into(i_main);
                }
            }
        });

        req_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                req_btn.setEnabled(false);
                if (req_btn.getText().toString().toUpperCase().equals("REQUEST")) {

                    final DatabaseReference req_ref = FirebaseDatabase.getInstance().getReference("Requests/" + pid + "/" + user.getUid() + "/");
                    req_ref.keepSynced(true);

                    req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Request[] r = new Request[]{ dataSnapshot.getValue(Request.class)};

                            myuref = FirebaseDatabase.getInstance().getReference("Users/" + user.getUid() + "/");
                            myuref.addListenerForSingleValueEvent(new ValueEventListener() {

                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    myu = dataSnapshot.getValue(User.class);
                                    if (r[0] == null) {

                                        Builder ad = new Builder(ProductActivity.this);
                                        ad.setTitle("Request Confirmation");
                                        ad.setMessage("Do you really want to send Request?");
                                        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                r[0] = new Request();
                                                GenericNotification n = new GenericNotification();
                                                n.setFuid(user.getUid());
                                                n.setPid(pid);
                                                n.setType(1);
                                                n.setStatus("Pending");
                                                DatabaseReference nref = FirebaseDatabase.getInstance().getReference("Notifications/" + pdt_obj.getUid() + "/");
                                                String nid = nref.push().getKey();
                                                n.setNid(nid);
                                                nref.child(nid).setValue(n).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        r[0].setStatus("Pending");
                                                        req_ref.setValue(r[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Toast.makeText(ProductActivity.this, "Request Sent Successfully", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });

                                        ad.setNegativeButton("No", null);
                                        ad.show();
                                    } else if (r[0].getStatus().equals("Pending")) {

                                        Toast.makeText(ProductActivity.this, "Your Request is Still Pending", Toast.LENGTH_LONG).show();

                                    } else if (r[0].getStatus().equals("Accepted")) {

                                        Toast.makeText(ProductActivity.this, "Your Request is already Accepted \n You Can Find Status in Notification Section", Toast.LENGTH_LONG).show();
                                    }
                                }

                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }

                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else if (req_btn.getText().toString().toUpperCase().equals("DELETE PRODUCT")) {

                    Builder ad = new Builder(ProductActivity.this);
                    ad.setTitle("Delete Confirmation");
                    ad.setMessage("Do you really want to Delete Your Product?");
                    ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pref.removeValue();
                            Toast.makeText(ProductActivity.this, "Product Deleted Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });

                    ad.setNegativeButton("No", null).show();
                }

                req_btn.setEnabled(true);
            }
        });
    }

}

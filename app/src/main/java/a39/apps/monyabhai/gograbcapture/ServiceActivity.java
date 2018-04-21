package a39.apps.monyabhai.gograbcapture;

import Beans.GenericNotification;
import Beans.Request;
import Beans.Service;
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

public class ServiceActivity extends AppCompatActivity {

    TextView city,decription,type,username;
    ImageView i1,i2,i3,i4,i_main,user_image;
    Toolbar mtool;
    User myu;
    DatabaseReference myuref;
    Service ser_obj;
    String sid;
    DatabaseReference sref;
    TextView price;
    StorageReference sstrref;
    Button req_btn;
    User user1;
    DatabaseReference uref;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        i1 =  findViewById(R.id.i1_service_layout);
        i2 =  findViewById(R.id.i2_service_layout);
        i3 =  findViewById(R.id.i3_service_layout);
        i4 =  findViewById(R.id.i4_service_layout);

        i2.setVisibility(View.INVISIBLE);
        i3.setVisibility(View.INVISIBLE);
        i4.setVisibility(View.INVISIBLE);

        i_main =  findViewById(R.id.img_main_serivice_layout);
        user_image =  findViewById(R.id.img_user_service);

        type =  findViewById(R.id.txt_ser_type_service);
        city =  findViewById(R.id.txt_ser_city_service);
        price =  findViewById(R.id.txt_ser_price_service);
        decription =  findViewById(R.id.txt_ser_desc_service);
        username =  findViewById(R.id.txt_ser_uname_service);

        req_btn =  findViewById(R.id.service_req_button);

        mtool =  findViewById(R.id.service_appbar);
        setSupportActionBar(mtool);

        sid = getIntent().getStringExtra("sid");
        sstrref = FirebaseStorage.getInstance().getReference("Services/" + sid + "/");
        sref = FirebaseDatabase.getInstance().getReference("Services/" + sid + "/");
        sref.keepSynced(true);
        sref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ser_obj =  dataSnapshot.getValue(Service.class);
                getSupportActionBar().setTitle(ser_obj.getTitle());
                if (ser_obj.getUid().equals(user.getUid())) {
                    req_btn.setText(R.string.delete_ser);
                    req_btn.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                }
                type.setText(ser_obj.getType());
                city.setText(ser_obj.getCity());
                String price_str="₹" + ser_obj.getPrice();
                price.setText(price_str);
                decription.setText(ser_obj.getDescription());
                uref = FirebaseDatabase.getInstance().getReference("Users/" + ser_obj.getUid() + "/");
                uref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user1 =  dataSnapshot.getValue(User.class);
                        username.setText(user1.getName());
                        Glide.with(ServiceActivity.this).load(user1.getUrl()).into(user_image);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Glide.with(ServiceActivity.this).load(ser_obj.getI1()).into(i_main);
                Glide.with(ServiceActivity.this).load(ser_obj.getI1()).into(i1);
                if (ser_obj.getI2() != null) {
                    i2.setVisibility(View.VISIBLE);
                    Glide.with(ServiceActivity.this).load(ser_obj.getI2()).into(i2);
                }
                if (ser_obj.getI3() != null) {
                    i3.setVisibility(View.VISIBLE);
                    Glide.with(ServiceActivity.this).load(ser_obj.getI3()).into(i3);
                }
                if (ser_obj.getI4() != null) {
                    i4.setVisibility(View.VISIBLE);
                    Glide.with(ServiceActivity.this).load(ser_obj.getI4()).into(i4);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        i1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ServiceActivity.this).load(ser_obj.getI1()).into(i_main);
            }
        });
        i2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ServiceActivity.this).load(ser_obj.getI2()).into(i_main);
            }
        });
        i3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ServiceActivity.this).load(ser_obj.getI3()).into(i_main);
            }
        });
        i4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ServiceActivity.this).load(ser_obj.getI4()).into(i_main);
            }
        });
        req_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                req_btn.setEnabled(false);
                if (req_btn.getText().toString().toUpperCase().equals("REQUEST")) {
                    final DatabaseReference req_ref = FirebaseDatabase.getInstance().getReference("Requests/" + sid + "/" + user.getUid() + "/");
                    req_ref.keepSynced(true);
                    req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Request[] r = new Request[]{ dataSnapshot.getValue(Request.class)};
                            myuref = FirebaseDatabase.getInstance().getReference("Users/" + user.getUid() + "/");
                            myuref.addListenerForSingleValueEvent(new ValueEventListener() {
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    myu =  dataSnapshot.getValue(User.class);
                                    if (r[0] == null) {
                                        Builder ad = new Builder(ServiceActivity.this);
                                        ad.setTitle("Request Confirmation");
                                        ad.setMessage("Do you really want to send Request?");
                                        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                r[0] = new Request();
                                                GenericNotification n = new GenericNotification();
                                                n.setFuid(user.getUid());
                                                n.setPid(sid);
                                                n.setType(2);
                                                n.setStatus("Pending");
                                                DatabaseReference nref = FirebaseDatabase.getInstance().getReference("Notifications/" + ser_obj.getUid() + "/");
                                                String nid = nref.push().getKey();
                                                n.setNid(nid);
                                                nref.child(nid).setValue(n).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        r[0].setStatus("Pending");
                                                        req_ref.setValue(r[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ServiceActivity.this, "Request Sent Successfully", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                        ad.setNegativeButton("No", null).show();
                                    } else if (r[0].getStatus().equals("Pending")) {
                                        Toast.makeText(ServiceActivity.this, "Your Request is Still Pending", Toast.LENGTH_LONG).show();
                                    } else if (r[0].getStatus().equals("Accepted")) {
                                        Toast.makeText(ServiceActivity.this, "Your Request is already Accepted \n You Can Find Status in Notification Section", Toast.LENGTH_LONG).show();
                                    }
                                }

                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }

                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else if (req_btn.getText().toString().toUpperCase().equals("DELETE SERVICE")) {
                    Builder ad = new Builder(ServiceActivity.this);
                    ad.setTitle("Delete Confirmation");
                    ad.setMessage("Do you really want to Delete Your Service?");
                    ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sref.removeValue();
                            Toast.makeText(ServiceActivity.this, "Service Deleted Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    ad.setNegativeButton("No", null);
                    ad.show();
                }
                req_btn.setEnabled(true);

            }
        });
    }
}

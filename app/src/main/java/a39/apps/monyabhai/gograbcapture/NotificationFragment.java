
package a39.apps.monyabhai.gograbcapture;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Beans.GenericNotification;
import Beans.Notifications;
import Beans.Product;
import Beans.Service;
import Beans.User;


public class NotificationFragment extends Fragment {

    ListView noif;
    User u;
    List <Notifications> notificationsList=new  ArrayList<>();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference notif_ref;
    DatabaseReference u_ref;
    public NotificationFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        noif=view.findViewById(R.id.notif_list);
        notif_ref= FirebaseDatabase.getInstance().getReference("Notifications/"+user.getUid()+"/");
        notif_ref.keepSynced(true);
        notif_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationsList.clear();
                for (DataSnapshot notisnapshot:dataSnapshot.getChildren()){
                    final Notifications n1=new Notifications();
                    final GenericNotification n=notisnapshot.getValue(GenericNotification.class);
                    if(n.getType()==1|n.getType()==3|n.getType()==4){
                        FirebaseDatabase.getInstance().getReference("Products/"+n.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Product p=dataSnapshot.getValue(Product.class);
                                    n1.setType(n.getType());
                                    n1.setPid(n.getPid());
                                    n1.setStatus(n.getStatus());
                                    n1.setNid(n.getNid());
                                    n1.setFuid(n.getFuid());
                                    n1.setOn(p.getTitle());
                                    FirebaseDatabase.getInstance().getReference("Users/"+n.getFuid()+"/").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User u=dataSnapshot.getValue(User.class);
                                            n1.setName(u.getName());
                                            n1.setPhone_no(u.getPhone_no());
                                            n1.setCity(u.getCity());
                                            n1.setImg_url(u.getUrl());
                                            notificationsList.add(n1);
                                            NotifacationAdapter adapter=new NotifacationAdapter(getContext(),R.layout.notif_layout,notificationsList);
                                            noif.setAdapter(adapter);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else
                                    notif_ref.child(n.getNid()).removeValue();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else if (n.getType()==2||n.getType()==5||n.getType()==6){
                        FirebaseDatabase.getInstance().getReference("Services/"+n.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Service s=dataSnapshot.getValue(Service.class);
                                    n1.setType(n.getType());
                                    n1.setPid(n.getPid());
                                    n1.setStatus(n.getStatus());
                                    n1.setNid(n.getNid());
                                    n1.setFuid(n.getFuid());
                                    if(s!=null){
                                        n1.setOn(s.getTitle());
                                        FirebaseDatabase.getInstance().getReference("Users/"+n.getFuid()+"/").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User u=dataSnapshot.getValue(User.class);
                                                if(u!=null) {
                                                    n1.setName(u.getName());
                                                    n1.setPhone_no(u.getPhone_no());
                                                    n1.setCity(u.getCity());
                                                    n1.setImg_url(u.getUrl());
                                                    notificationsList.add(n1);
                                                    NotifacationAdapter adapter=new NotifacationAdapter(getContext(),R.layout.notif_layout,notificationsList);
                                                    noif.setAdapter(adapter);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                                else
                                    notif_ref.child(n.getNid()).removeValue();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        u_ref=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
        u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u=dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        noif.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Notifications n = notificationsList.get(position);
                final GenericNotification gn=new GenericNotification();
                gn.setNid(n.getNid());
                gn.setFuid(n.getFuid());
                gn.setPid(n.getPid());
                gn.setStatus("Completed");
                gn.setType(n.getType());
                if(n.getType()==1|n.getType()==2){
                    if(n.getStatus().equals("Pending")){
                        AlertDialog.Builder ad=new AlertDialog.Builder(getContext());
                        ad.setTitle("Accept/Decline Confirmation");
                        ad.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                notif_ref.child(n.getNid()).setValue(gn).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference("Requests/"+n.getPid()+"/"+n.getFuid()+"/").child("status").setValue("Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                GenericNotification n1= new GenericNotification();
                                                n1.setStatus("Completed");
                                                n1.setFuid(u.getUid());
                                                n1.setPid(n.getPid());
                                                if(n.getType()==1) {
                                                    n1.setType(4);
                                                }
                                                else {
                                                    n1.setType(6);
                                                }
                                                DatabaseReference nref=FirebaseDatabase.getInstance().getReference("Notifications/"+n.getFuid()+"/");
                                                String nid=nref.push().getKey();
                                                n1.setNid(nid);
                                                nref.child(nid).setValue(n1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(),"Request Accepted",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }});
                        ad.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notif_ref.child(n.getNid()).setValue(gn).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference("Requests/"+n.getPid()+"/"+n.getFuid()+"/").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                GenericNotification n1= new GenericNotification();
                                                n1.setStatus("Completed");
                                                n1.setFuid(u.getUid());
                                                n1.setPid(n.getPid());
                                                if(n.getType()==1) {
                                                    n1.setType(3);
                                                }
                                                else {
                                                    n1.setType(5);
                                                }
                                                DatabaseReference nref=FirebaseDatabase.getInstance().getReference("Notifications/"+n.getFuid()+"/");
                                                String nid=nref.push().getKey();
                                                n1.setNid(nid);
                                                nref.child(nid).setValue(n1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(),"Request Declined",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        ad.show();
                    }
                    else {
                        Toast.makeText(getContext(),"You have Already Responded",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return view;
    }
}

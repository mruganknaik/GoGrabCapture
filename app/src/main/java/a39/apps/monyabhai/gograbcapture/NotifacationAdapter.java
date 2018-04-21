
package a39.apps.monyabhai.gograbcapture;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

import Beans.Notifications;



public class NotifacationAdapter extends ArrayAdapter<Notifications> {
    private Activity context;
    private List<Notifications> notificationsList;

    NotifacationAdapter(@NonNull Context context, int resource, @NonNull List<Notifications> objects) {
        super(context, resource, objects);
        this.context= (Activity) context;
        notificationsList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.notif_layout,null,true);
        final ImageView notif_img=view.findViewById(R.id.notif_img);
        final TextView noti_text=view.findViewById(R.id.txt_notif);
        final Notifications n=notificationsList.get(position);
        final int type=n.getType();
        Glide.with(context).load(n.getImg_url()).into(notif_img);
        String txtData;
        switch (type){
            case 1:
                txtData=n.getName().toUpperCase()+" ("+n.getCity()+","+n.getPhone_no()+")"+" Sent Request for your Product "+n.getOn();
                noti_text.setText(txtData);
                break;
            case 2:
                txtData=n.getName().toUpperCase()+" ("+n.getCity()+","+n.getPhone_no()+")"+" Sent Request for your Service "+n.getOn();
                noti_text.setText(txtData);
                break;
            case 3:
                txtData=n.getName().toUpperCase()+" has Declined your Request for Product "+n.getOn();
                noti_text.setText(txtData);
                break;
            case 4:
                txtData=n.getName().toUpperCase()+" ("+n.getPhone_no()+")"+" has Accepted your Request for Product "+n.getOn();
                noti_text.setText(txtData);
                break;
            case 5:
                txtData=n.getName().toUpperCase()+" has Declined your Request for Service "+n.getOn();
                noti_text.setText(txtData);
                break;
            case 6:
                txtData=n.getName().toUpperCase()+" ("+n.getPhone_no()+")"+" has Accepted your Request for Service "+n.getOn();
                noti_text.setText(txtData);
                break;
        }

        return view;
    }
}

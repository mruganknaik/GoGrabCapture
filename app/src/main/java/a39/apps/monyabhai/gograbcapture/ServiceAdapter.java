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

import Beans.Service;


public class ServiceAdapter extends ArrayAdapter<Service> {
    private Activity context;
    private List<Service> servicelist;
    ServiceAdapter(@NonNull Context context, List<Service> resource) {
        super(context, R.layout.product_list_layout,resource);
        this.context= (Activity) context;
        this.servicelist=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.product_list_layout,null,true);
        final ImageView img_pdt=listViewItem.findViewById(R.id.layout_product_img);
        TextView title_text=listViewItem.findViewById(R.id.layout_product_titile);
        TextView city_text=listViewItem.findViewById(R.id.layout_product_city);
        TextView price_text=listViewItem.findViewById(R.id.layout_product_price);
        Service s = servicelist.get(position);
        Glide.with(context).load(s.getI1()).into(img_pdt);
        title_text.setText(s.getTitle());
        city_text.setText(s.getCity());
        String price="₹"+s.getPrice();
        price_text.setText(price);
        return listViewItem;
    }
}

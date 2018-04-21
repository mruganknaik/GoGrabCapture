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

import Beans.Product;


public class ProductAdapter extends ArrayAdapter<Product> {
    private Activity context;
    private List<Product> productlist;

    ProductAdapter(@NonNull Context context, List<Product> resource) {
        super(context,R.layout.product_list_layout, resource);
        this.context= (Activity) context;
        this.productlist=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.product_list_layout, null, true);
        ImageView img_pdt = listViewItem.findViewById(R.id.layout_product_img);
        TextView title_text = listViewItem.findViewById(R.id.layout_product_titile);
        TextView city_text = listViewItem.findViewById(R.id.layout_product_city);
        TextView price_text = listViewItem.findViewById(R.id.layout_product_price);
        Product p = productlist.get(position);
        Glide.with(context).load(p.getI1()).into(img_pdt);
        title_text.setText(p.getTitle());
        city_text.setText(p.getCity());
        if (p.getPrice_day() == null) {
            String price="₹"+p.getPrice_sell();
            price_text.setText(price);
        } else {
            String price="₹"+p.getPrice_day();
            price_text.setText(price);
        }
        return listViewItem;

    }
}

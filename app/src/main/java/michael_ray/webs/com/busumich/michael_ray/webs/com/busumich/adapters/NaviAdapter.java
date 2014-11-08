package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import michael_ray.webs.com.busumich.R;

public class NaviAdapter extends ArrayAdapter<String> {
    Context context;
    int layoutResourceId;
    String[] data = null;

    public NaviAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NaviHolder holder = null;

        if (row==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new NaviHolder();
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.icon = (ImageView)row.findViewById(R.id.icon);
            row.setTag(holder);
        } else {
            holder = (NaviHolder)row.getTag();
        }
        holder.title.setText(data[position]);
        if (data[position].equals("Uber")) holder.icon.setImageResource(R.drawable.img_uber);
        else if (data[position].equals("ZipCar")) holder.icon.setImageResource(R.drawable.img_zipcar);
        else if (data[position].equals("TheRide")) holder.icon.setImageResource(R.drawable.img_theride);
        else if (data[position].equals("Blue Bus")) holder.icon.setImageResource(R.drawable.ic_launcher);
        else if (data[position].equals("Safe Ride")) holder.icon.setImageResource(R.drawable.img_saferide);
        return row;
    }

    static class NaviHolder {
        TextView title;
        ImageView icon;
    }
}
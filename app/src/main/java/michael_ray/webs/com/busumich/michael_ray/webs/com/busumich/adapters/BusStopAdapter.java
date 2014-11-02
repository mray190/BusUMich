package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import michael_ray.webs.com.busumich.R;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.BusStop;

public class BusStopAdapter extends ArrayAdapter<BusStop> {
    Context context;
    int layoutResourceId;
    ArrayList<BusStop> data = null;
    DecimalFormat df = new DecimalFormat("00.0000");

    public BusStopAdapter(Context context, int layoutResourceId, ArrayList<BusStop> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BusStopHolder holder = null;

        if (row==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new BusStopHolder();
            holder.nameTxt = (TextView)row.findViewById(R.id.nameTxt);
            holder.descriptionTxt = (TextView)row.findViewById(R.id.descTxt);
            holder.checkbox = (CheckBox)row.findViewById(R.id.favorite);
            //holder.latTxt = (TextView)row.findViewById(R.id.latTxt);
            //holder.lonTxt = (TextView)row.findViewById(R.id.lonTxt);
            row.setTag(holder);
        } else {
            holder = (BusStopHolder)row.getTag();
        }
        BusStop stop = data.get(position);
        holder.nameTxt.setText(stop.getName());
        holder.descriptionTxt.setText(stop.getDescription());
        holder.checkbox.setChecked(false);
        //holder.latTxt.setText(df.format(stop.getLat()));
        //holder.lonTxt.setText(df.format(stop.getLon()));
        return row;
    }

    static class BusStopHolder {
        TextView nameTxt, descriptionTxt, latTxt, lonTxt;
        CheckBox checkbox;
    }
}
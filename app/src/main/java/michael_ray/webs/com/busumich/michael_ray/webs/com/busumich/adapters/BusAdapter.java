package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import michael_ray.webs.com.busumich.R;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.Bus;

/**
 * Created by mray on 31/10/14.
 */
public class BusAdapter extends ArrayAdapter<Bus> {
    Context context;
    int layoutResourceId;
    ArrayList<Bus> data = null;
    DecimalFormat df = new DecimalFormat("00.0000");

    public BusAdapter(Context context, int layoutResourceId, ArrayList<Bus> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BusHolder holder = null;

        if (row==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new BusHolder();
            //holder.idTxt = (TextView)row.findViewById(R.id.idTxt);
            //holder.busNameTxt = (TextView)row.findViewById(R.id.busNameTxt);
            //holder.headingTxt = (TextView)row.findViewById(R.id.descTxt);
            holder.routeTxt = (TextView)row.findViewById(R.id.routeTxt);
            holder.lastStopTxt = (TextView)row.findViewById(R.id.lastStopTxt);
            holder.etaTxt = (TextView)row.findViewById(R.id.etaTxt);
            //holder.lastTimeTxt = (TextView)row.findViewById(R.id.descTxt);
            //holder.latTxt = (TextView)row.findViewById(R.id.descTxt);
            //holder.lonTxt = (TextView)row.findViewById(R.id.descTxt);
            row.setTag(holder);
        } else {
            holder = (BusHolder)row.getTag();
        }
        Bus bus = data.get(position);
        //holder.idTxt.setText(bus.getId());
        //holder.busNameTxt.setText(bus.getName());
        //holder.headingTxt.setText(bus.getHeading());
        holder.routeTxt.setText("Route: " + bus.getBusRoute().getName());
        holder.lastStopTxt.setText("Last stop: " + bus.getLastBusStop().getName());
        holder.etaTxt.setText("Eta (min): " + Integer.toString(bus.getEta()));
        //holder.lastTimeTxt.setText(bus.getLastTime());
        //holder.latTxt.setText(df.format(bus.getLat()));
        //holder.lonTxt.setText(df.format(bus.getLon()));
        return row;
    }

    static class BusHolder {
        TextView idTxt, busNameTxt, headingTxt, routeTxt, lastStopTxt, lastTimeTxt, latTxt, lonTxt, etaTxt;
    }
}
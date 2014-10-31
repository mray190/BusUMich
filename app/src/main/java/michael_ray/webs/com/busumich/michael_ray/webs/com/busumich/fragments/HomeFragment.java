package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import michael_ray.webs.com.busumich.R;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters.BusAdapter;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters.BusStopAdapter;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.Bus;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.BusStop;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.Parser;

public class HomeFragment extends ListFragment {

    private int layer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layer = 0;
        BusStopAdapter adapter = new BusStopAdapter(getActivity(), R.layout.row_busstop,new ArrayList<BusStop>());
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
    }

    public void setBusStopData(ArrayList<BusStop> stops) {
        BusStopAdapter adapter = new BusStopAdapter(getActivity(), R.layout.row_busstop, stops);
        setListAdapter(adapter);
        layer = 1;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (layer==1) {
            Refresh refresh = new Refresh();
            BusStop stop = (BusStop)getListAdapter().getItem(position);
            refresh.execute(stop);
        }
    }

    class Refresh extends AsyncTask<BusStop, Void, ArrayList<Bus>> {
        @Override
        protected ArrayList<Bus> doInBackground(BusStop...params) {
            Parser parser = new Parser();
            return parser.getClosestBuses(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Bus> buses) {
            BusAdapter adapter = new BusAdapter(getActivity(), R.layout.row_bus, buses);
            setListAdapter(adapter);
            layer = 2;
        }
    }
}
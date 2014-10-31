package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import michael_ray.webs.com.busumich.R;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters.BusStopAdapter;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.BusStop;

public class HomeFragment extends ListFragment {
    private BusStopAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BusStopAdapter(getActivity(), R.layout.row_busstop,new ArrayList<BusStop>());
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
    }

    public void setData(ArrayList<BusStop> stops) {
        adapter = new BusStopAdapter(getActivity(), R.layout.row_busstop, stops);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}
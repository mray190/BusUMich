package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters;

import java.io.Serializable;
import java.util.ArrayList;

import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.Bus;

public class BusWrapper implements Serializable {
    private ArrayList<Bus> buses;
    public BusWrapper(ArrayList<Bus> data) {
            this.buses = data;
        }
    public ArrayList<Bus> getBuses() {
            return this.buses;
        }
}

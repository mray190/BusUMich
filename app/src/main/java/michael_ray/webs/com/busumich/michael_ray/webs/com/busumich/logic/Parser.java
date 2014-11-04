package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Parser.java
 * Parses information from online
 * @author Michael Ray
 * @version 1
 * @since 10-02-14
 */
public class Parser {

    private Context context;

    public Parser(Context context) {
        this.context = context;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Logic for parser methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private ArrayList<Integer> readData() {
        ArrayList<Integer> data = new ArrayList<Integer>();
        try {
            File file = context.getFileStreamPath("favorites");
            if (!file.exists())
                file.createNewFile();
            FileInputStream fis = context.openFileInput("favorites");
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BusUMich","Error reading data from the favorites file");
        }
        return data;
    }

    private ArrayList<BusStop> syncFavoriteData(ArrayList<BusStop> stops, ArrayList<Integer> favorites) {
        for (int i=0; i<stops.size(); i++) {
            for (int j=0; j<favorites.size(); j++) {
                if (stops.get(i).getId()==favorites.get(j)) {
                    stops.get(i).setFavorite(true);
                    break;
                }
            }
        }
        return stops;
    }

    private void writeData(ArrayList<Integer> output) {
        try {
            FileOutputStream fos = context.openFileOutput("favorites",Context.MODE_PRIVATE);
            for (int i=0; i<output.size(); i++)
                fos.write((Integer.toString(output.get(i))+"\n").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BusUMich","Error writing to the favorites file");
        }
    }

    public void removeFavorite(BusStop stop) {
        ArrayList<Integer> data = readData();
        for (int i=0; i<data.size(); i++) {
            if (data.get(i) == stop.getId()) {
                data.remove(i);
                writeData(data);
                return;
            }
        }
    }

    public void addFavorite(BusStop stop) {
        ArrayList<Integer> favorites = readData();
        favorites.add(stop.getId());
        writeData(favorites);
    }

    /**
     * Connects to a url and returns the data on that url page
     * @param url representing the site url (string)
     * @return string representing the site data
     */
    private String pullData(String url) {
        String outputLine = "";
        try {
            URL ur = new URL(url);
            HttpURLConnection yc = (HttpURLConnection) ur.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                outputLine += inputLine;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BusUMich", "Error connecting and reading URL. Method: pullData");
        }
        return outputLine;
    }

    /**
     * Gets all of the Buses from the DoubleMap API
     * @return list of buses
     */
    private ArrayList<Bus> calcBuses() {
        ArrayList<Bus> buses = new ArrayList<Bus>();
        try {
            String url = "http://mbus.doublemap.com/map/v2/buses";
            JSONArray json = new JSONArray(pullData(url));
            for (int i=0; i<json.length(); i++) {
                int id = json.getJSONObject(i).getInt("id");
                String name = json.getJSONObject(i).getString("name");
                int heading = json.getJSONObject(i).getInt("heading");
                int route = json.getJSONObject(i).getInt("route");
                int lastStop = json.getJSONObject(i).getInt("lastStop");
                int lastTime = json.getJSONObject(i).getInt("lastUpdate");
                double lat = json.getJSONObject(i).getDouble("lat");
                double lon = json.getJSONObject(i).getDouble("lon");
                Bus bus = new Bus(id,name,heading,route,lastStop,lastTime,lat,lon);
                buses.add(bus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BusUMich", "Error parsing Bus json. Method: calcBuses");
        }
        return buses;
    }

    /**
     * Gets all of the Routes from the DoubleMap API
     * @return list of bus routes
     */
    private ArrayList<BusRoute> calcRoutes() {
        ArrayList<BusRoute> routes = new ArrayList<BusRoute>();
        try {
            String url = "http://mbus.doublemap.com/map/v2/routes";
            JSONArray json = new JSONArray(pullData(url));
            for (int i=0; i<json.length(); i++) {
                int id = json.getJSONObject(i).getInt("id");
                String name = json.getJSONObject(i).getString("name");
                String short_name = json.getJSONObject(i).getString("short_name");
                String description = json.getJSONObject(i).getString("description");
                String color = json.getJSONObject(i).getString("color");
                JSONArray patharray = json.getJSONObject(i).getJSONArray("path");
                double[] path = new double[patharray.length()];
                for (int j=0; j<path.length; j++)
                    path[j] = patharray.getDouble(j);
                String schedule_url = json.getJSONObject(i).getString("schedule_url");
                boolean active = json.getJSONObject(i).getBoolean("active");
                JSONArray stoparray = json.getJSONObject(i).getJSONArray("path");
                int[] stops = new int[stoparray.length()];
                for (int j=0; j<stops.length; j++)
                    stops[j] = stoparray.getInt(j);
                BusRoute busroute = new BusRoute(id,name,short_name,description,color,path,schedule_url,active,stops);
                routes.add(busroute);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BusUMich", "Error parsing BusRoute json. Method: calcRoutes");
        }
        return routes;
    }

    /**
     * Gets all of the Stops from the DoubleMap API
     * @return list of bus stops
     */
    private ArrayList<BusStop> calcStops() {
        ArrayList<BusStop> stops = new ArrayList<BusStop>();
        try {
            String url = "http://mbus.doublemap.com/map/v2/stops";
            JSONArray json = new JSONArray(pullData(url));
            for (int i=0; i<json.length(); i++) {
                int id = json.getJSONObject(i).getInt("id");
                String name = json.getJSONObject(i).getString("name");
                String description = json.getJSONObject(i).getString("description");
                double lat = json.getJSONObject(i).getDouble("lat");
                double lon = json.getJSONObject(i).getDouble("lon");
                BusStop busstop = new BusStop(id,name,description,lat,lon);
                stops.add(busstop);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BusUMich", "Error parsing BusStop json. Method: calcStops");
        }
        return stops;
    }

    /**
     * Gets all of the Buses from the DoubleMap API that are closest to stop
     * @param stop which is the closest bus stop to the user
     * @return buses which is the array list of closest buses
     */
    private ArrayList<Bus> calcClosestBuses(BusStop stop, ArrayList<Bus> all_buses) {
        ArrayList<Bus> filtered_buses = new ArrayList<Bus>();
        try {
            String url = "http://mbus.doublemap.com/map/v2/eta?stop=" + Integer.toString(stop.getId());
            JSONArray json = new JSONObject(pullData(url)).getJSONObject("etas").getJSONObject(Integer.toString(stop.getId())).getJSONArray("etas");
            for (int i=0; i<json.length(); i++) {
                String type = json.getJSONObject(i).getString("type");
                if (!(type.equals("schedule"))) {
                    int id = json.getJSONObject(i).getInt("bus_id");
                    int avg = json.getJSONObject(i).getInt("avg");
                    Bus bus = null;
                    for (int j = 0; j < all_buses.size(); j++) {
                        if (all_buses.get(j).getId() == id) {
                            bus = all_buses.get(j);
                            bus.setEta(avg);
                            break;
                        }
                    }
                    if (bus != null)
                        filtered_buses.add(bus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BusUMich", "Error parsing ETA json. Method: calcClosestBuses");
        }
        return filtered_buses;
    }

    /**
     * Adds the current route object and the last stop object to all of the bus objects
     * @param buses list of buses
     * @param routes list of routes
     * @param stops list of stops
     * @return list of buses with the associated route and stop data
     */
    private ArrayList<Bus> correlateBuses(ArrayList<Bus> buses, ArrayList<BusRoute> routes, ArrayList<BusStop> stops) {
        for (int i=0; i<buses.size(); i++) {
            for (int j=0; j<routes.size(); j++) {
                if (buses.get(i).getRoute() == routes.get(j).getId()) {
                    buses.get(i).setBusRoute(routes.get(j));
                    break;
                }
            }
            for (int j=0; j<stops.size(); j++) {
                if (buses.get(i).getLastStop() == stops.get(j).getId()) {
                    buses.get(i).setLastBusStop(stops.get(j));
                    //WRONG IMPLEMENTATION - edit this later
                    buses.get(i).setNextBusStop(stops.get(j));
                    break;
                }
            }
        }
        return buses;
    }

    /**
     * Adds the stop objects and the bus objects to all of the bus route objects
     * @param buses list of buses
     * @param routes list of routes
     * @return list of bus routes with the associated stop and bus data
     */
    private ArrayList<BusRoute> correlateRoutes(ArrayList<Bus> buses, ArrayList<BusRoute> routes) {
        for (int i=0; i<routes.size(); i++) {
            routes.get(i).setBuses(new ArrayList<Bus>());
            for (int j=0; j<buses.size(); j++) {
                if (routes.get(i).getId() == buses.get(j).getRoute()) {
                    routes.get(i).addBus(buses.get(j), true);
                }
            }
        }
        return routes;
    }

    /**
     * Calculates a list of closest bus stops to a specific location
     * @param location Users location
     * @param stops list of stops
     * @return list of sorted bus stops with closest to location on top (index of 0)
     */
    private ArrayList<BusStop> calcClosestStops(Location location, ArrayList<BusStop> stops) {
        if (location==null)
            return new ArrayList<BusStop>();
        for (int i=0; i<stops.size(); i++) {
            double distance = Math.sqrt(Math.pow(location.getLatitude() - stops.get(i).getLat(),2) + Math.pow(location.getLongitude() - stops.get(i).getLon(),2));
            stops.get(i).setDistance(distance);
        }
        Collections.sort(stops);
        return stops;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Getter methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Bus> getBuses() { return correlateBuses(calcBuses(),calcRoutes(),calcStops()); }
    public ArrayList<BusRoute> getRoutes() { return correlateRoutes(calcBuses(),calcRoutes()); }
    public ArrayList<BusStop> getStops() { return syncFavoriteData(calcStops(),readData()); }
    public ArrayList<BusStop> getClosestStops(Location location) { return calcClosestStops(location, getStops()); }
    public ArrayList<Bus> getClosestBuses(BusStop stop) { return calcClosestBuses(stop,getBuses());}
    public ArrayList<BusStop> getFavorites() {
        ArrayList<BusStop> full = getStops();
        ArrayList<BusStop> filtered = new ArrayList<BusStop>();
        for (int i=0; i<full.size(); i++) {
            if (full.get(i).getFavorite())
                filtered.add(full.get(i));
        }
        return filtered;
    }
}

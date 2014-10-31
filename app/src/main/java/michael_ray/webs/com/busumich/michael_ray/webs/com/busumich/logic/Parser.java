package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
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
    private ArrayList<Bus> buses;
    private ArrayList<BusRoute> routes;
    private ArrayList<BusStop> stops;

    public Parser() {
        this.buses = new ArrayList<Bus>();
        this.routes = new ArrayList<BusRoute>();
        this.stops = new ArrayList<BusStop>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Logic for parser methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
     * Modifies: buses (ArrayList<Bus>)
     * @return boolean if the parser executed correctly without errors
     */
    public boolean calcBuses() {
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
            return false;
        }
        return true;
    }

    /**
     * Gets all of the Routes from the DoubleMap API
     * Modifies: routes (ArrayList<BusRoutes>)
     * @return boolean if the parser executed correctly without errors
     */
    public boolean calcRoutes() {
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
            return false;
        }
        return true;
    }

    /**
     * Gets all of the Stops from the DoubleMap API
     * Modifies: stops (ArrayList<BusStop>)
     * @return boolean if the parser executed correctly without errors
     */
    public boolean calcStops() {
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
            return false;
        }
        return true;
    }

    /**
     * Assigns the buses the correct route that it is running from the DoubleMap API
     * Modifies: buses (ArrayList<Bus>) and routes (ArrayList<BusRoute>)
     */
    public void assignRoutes() {
        for (int i=0; i<buses.size(); i++) {
            for (int j=0; j<routes.size(); j++) {
                if (buses.get(i).getRoute() == routes.get(j).getId()) {
                    buses.get(i).setBusRoute(routes.get(j));
                    routes.get(j).addBus(buses.get(i), true);
                }
            }
        }
    }

    private void calcStopDistances(Location location) {
        for (int i=0; i<stops.size(); i++) {
            double distance = Math.sqrt(Math.pow(location.getLatitude() - stops.get(i).getLat(),2) + Math.pow(location.getLongitude() - stops.get(i).getLon(),2));
            stops.get(i).setDistance(distance);
        }
    }

    public ArrayList<BusStop> getClosestStops(Location location) {
        calcStopDistances(location);
        Collections.sort(stops);
        return stops;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Getter methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Bus> getBuses() { return this.buses; }
    public ArrayList<BusRoute> getRoutes() { return this.routes; }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Setter methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setBuses(ArrayList<Bus> buses) { this.buses = buses; }
    public void setRoutes(ArrayList<BusRoute> routes) { this.routes = routes; }
}

//Data regarding bus stops:
//http://mbus.doublemap.com/map/v2/stops

//Data regarding eta of a bus to a bus stop:
//http://mbus.doublemap.com/map/v2/eta?stop=92

package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic;

import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Parser.java
 * Parses information from online
 * @author Michael Ray
 * @version 1
 * @since 10-02-14
 */
public class Parser {

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

    public ArrayList<Bus> getBuses() {
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
            Log.d("BusUMich", "Error parsing Bus json. Method: getBuses");
        }
        return buses;
    }

    public ArrayList<BusRoute> getRoutes() {
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
            Log.d("BusUMich", "Error parsing BusRoute json. Method: getRoutes");
        }
        return routes;
    }
}

//Data regarding bus stops:
//http://mbus.doublemap.com/map/v2/stops

//Data regarding eta of a bus to a bus stop:
//http://mbus.doublemap.com/map/v2/eta?stop=92

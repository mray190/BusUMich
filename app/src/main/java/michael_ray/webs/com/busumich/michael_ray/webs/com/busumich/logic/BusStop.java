package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic;

/**
 * BusStop.java
 * Stores information regarding each bus stop
 * @author Michael Ray
 * @version 1
 * @since 10-02-14
 */
public class BusStop implements Comparable {
    private int id;
    private double lat, lon;
    private String name, description;
    private double distance;
    private boolean favorite;

    public BusStop(int id, String name, String description, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.description = description;
        this.favorite = false;
    }

    public int getId() { return this.id; }
    public double getLat() { return this.lat; }
    public double getLon() { return this.lon; }
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }

    public void setId(int id) { this.id = id; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLon(double lon) { this.lon = lon; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    public double getDistance() { return this.distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public boolean getFavorite() { return this.favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    @Override
    public int compareTo(Object other){
        double result =  distance - ((BusStop)other).getDistance();
        if (result<0) return -1;
        else if (result==0) return 0;
        else return 1;
    }
}
package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic;

/**
 * BusStop.java
 * Stores information regarding each bus stop
 * @author Michael Ray
 * @version 1
 * @since 10-02-14
 */
public class BusStop {
    private int id;
    private double lat, lon;
    private String name, description;

    public BusStop() {
        this.id = 137;
        this.lat = 42.277683;
        this.lon = -83.73494;
        this.name = "CC Little";
        this.description = "Central Campus Transit Center";
    }

    public BusStop(int id, double lat, double lon, String name, String description) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.description = description;
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
}
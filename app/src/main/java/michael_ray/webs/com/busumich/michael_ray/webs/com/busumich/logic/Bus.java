package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic;

/**
 * Bus.java
 * Stores information regarding each bus
 * @author Michael Ray
 * @version 1
 * @since 10-02-14
 */
public class Bus {
    private int id, heading, route, lastStop, lastTime;
    private String name;
    private double lat, lon;

    public Bus(int id, String name, int heading, int route, int lastStop, int lastTime, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.heading = heading;
        this.route = route;
        this.lastStop = lastStop;
        this.lastTime = lastTime;
    }

    public int getId() { return this.id; }
    public double getLat() { return this.lat; }
    public double getLon() { return this.lon; }
    public String getName() { return this.name; }
    public int getHeading() { return this.heading; }
    public int getRoute() { return this.route; }
    public int getLastStop() { return this.lastStop; }
    public int getLastTime() { return this.lastTime; }

    public void setId(int id) { this.id = id; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLon(double lon) { this.lon = lon; }
    public void setName(String name) { this.name = name; }
    public void setHeading(int heading) { this.heading = heading; }
    public void setRoute(int route) { this.route = route; }
    public void setLastStop(int lastStop) { this.lastStop = lastStop; }
    public void setLastTime(int lastTime) { this.lastTime = lastTime; }
}
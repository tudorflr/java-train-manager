package model;

public class RouteStop {
    private Station station;
    private String arrivalTime;
    private String departureTime;

    public RouteStop(Station station, String arrivalTime, String departureTime) {
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public Station getStation() { return station; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDepartureTime() { return departureTime; }

    @Override
    public String toString() {
        return station.getName() + " (arr " + arrivalTime + ", dep " + departureTime + ")";
    }
}

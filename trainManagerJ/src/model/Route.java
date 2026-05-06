package model;

import java.util.ArrayList;

public class Route {
    private int id;
    private Train train;
    private ArrayList<RouteStop> stops;

    public Route(int id, Train train) {
        this.id = id;
        this.train = train;
        this.stops = new ArrayList<>();
    }

    public int getId() { return id; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public ArrayList<RouteStop> getStops() { return stops; }

    public void addStop(RouteStop stop) {
        stops.add(stop);
    }

    public boolean removeStop(String stationName) {
        RouteStop stop = getStopByStation(stationName);

        if(stop == null) {
            return false;
        }

        stops.remove(stop);
        return true;
    }

    public boolean replaceStop(String oldStationName, RouteStop newStop) {
        int index = getStationIndex(oldStationName);

        if(index == -1) {
            return false;
        }

        stops.set(index, newStop);
        return true;
    }

    public boolean containsStation(String stationName) {
        for(RouteStop stop : stops) {
            if(stop.getStation().getName().equalsIgnoreCase(stationName)) {
                return true;
            }
        }
        return false;
    }

    public int getStationIndex(String stationName) {
        for(int i = 0; i < stops.size(); i++) {
            if(stops.get(i).getStation().getName().equalsIgnoreCase(stationName)) {
                return i;
            }
        }
        return -1;
    }

    public RouteStop getStopByStation(String stationName) {
        for(RouteStop stop : stops) {
            if(stop.getStation().getName().equalsIgnoreCase(stationName)) {
                return stop;
            }
        }
        return null;
    }

    public boolean canTravelDirect(String departure, String arrival) {
        int depIndex = getStationIndex(departure);
        int arrIndex = getStationIndex(arrival);

        return depIndex != -1 && arrIndex != -1 && depIndex < arrIndex;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("Route " + id + " on train " + train.getTrainName() + ": ");

        for(int i = 0; i < stops.size(); i++) {
            RouteStop stop = stops.get(i);
            text.append(stop.getStation().getName())
                    .append(" (arr ")
                    .append(stop.getArrivalTime())
                    .append(", dep ")
                    .append(stop.getDepartureTime())
                    .append(")");

            if(i < stops.size() - 1) {
                text.append(" -> ");
            }
        }

        return text.toString();
    }
}

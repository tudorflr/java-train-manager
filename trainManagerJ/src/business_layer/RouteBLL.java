package business_layer;

import data_access.RouteDAO;
import model.Route;
import model.RouteStop;
import model.TravelOption;

import java.util.ArrayList;

public class RouteBLL {
    private RouteDAO routeDAO;

    public RouteBLL(RouteDAO routeDAO) {
        this.routeDAO = routeDAO;
    }

    public ArrayList<TravelOption> findTravelOptions(String departure, String arrival) {
        ArrayList<TravelOption> options = new ArrayList<>();

        for(Route route : routeDAO.findAll()) {
            if(route.canTravelDirect(departure, arrival)) {
                options.add(new TravelOption(route, departure, arrival));
            }
        }

        for(Route firstRoute : routeDAO.findAll()) {
            if(!firstRoute.containsStation(departure)) {
                continue;
            }

            int departureIndex = firstRoute.getStationIndex(departure);

            for(RouteStop possibleChange : firstRoute.getStops()) {
                String changeStation = possibleChange.getStation().getName();
                int changeIndex = firstRoute.getStationIndex(changeStation);

                if(changeIndex <= departureIndex) {
                    continue;
                }

                for(Route secondRoute : routeDAO.findAll()) {
                    if(firstRoute.getId() == secondRoute.getId()) {
                        continue;
                    }

                    if(secondRoute.canTravelDirect(changeStation, arrival) && hasEnoughTimeToChange(firstRoute, secondRoute, changeStation)) {
                        options.add(new TravelOption(firstRoute, secondRoute, departure, changeStation, arrival));
                    }
                }
            }
        }

        return options;
    }

    public ArrayList<Route> getAllRoutes() {
        return routeDAO.findAll();
    }

    private boolean hasEnoughTimeToChange(Route firstRoute, Route secondRoute, String changeStation) {
        RouteStop firstStop = firstRoute.getStopByStation(changeStation);
        RouteStop secondStop = secondRoute.getStopByStation(changeStation);
        int firstArrival = parseTime(firstStop.getArrivalTime());
        int secondDeparture = parseTime(secondStop.getDepartureTime());

        if(firstArrival == -1 || secondDeparture == -1) {
            return true;
        }

        return secondDeparture >= firstArrival;
    }

    private int parseTime(String time) {
        if(time == null || time.equals("-")) {
            return -1;
        }

        String[] parts = time.split(":");

        if(parts.length != 2) {
            return -1;
        }

        try {
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}

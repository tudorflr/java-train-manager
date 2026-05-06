package data_access;

import model.Route;
import java.util.ArrayList;

public class RouteDAO {
    private ArrayList<Route> routes;

    public RouteDAO() {
        routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public void removeRoute(int id) {
        Route route = findById(id);

        if(route != null) {
            routes.remove(route);
        }
    }

    public void removeRoutesForTrain(int trainId) {
        routes.removeIf(route -> route.getTrain().getId() == trainId);
    }

    public Route findById(int id) {
        for(Route route : routes) {
            if(route.getId() == id) {
                return route;
            }
        }

        return null;
    }

    public ArrayList<Route> findAll() {
        return routes;
    }
}

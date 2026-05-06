package model;

public class TravelOption {
    private Route firstRoute;
    private Route secondRoute;
    private String departureStation;
    private String arrivalStation;
    private String changeStation;

    public TravelOption(Route firstRoute, String departureStation, String arrivalStation) {
        this.firstRoute = firstRoute;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.changeStation = null;
    }

    public TravelOption(Route firstRoute, Route secondRoute, String departureStation, String changeStation, String arrivalStation) {
        this.firstRoute = firstRoute;
        this.secondRoute = secondRoute;
        this.departureStation = departureStation;
        this.changeStation = changeStation;
        this.arrivalStation = arrivalStation;
    }

    public boolean hasChangeover() {
        return secondRoute != null;
    }

    @Override
    public String toString() {
        if(!hasChangeover()) {
            RouteStop dep = firstRoute.getStopByStation(departureStation);
            RouteStop arr = firstRoute.getStopByStation(arrivalStation);

            return "Direct trip: train " + firstRoute.getTrain().getTrainName() +
                    " leaves " + departureStation + " at " + dep.getDepartureTime() +
                    " and gets to " + arrivalStation + " at " + arr.getArrivalTime();
        }

        RouteStop dep = firstRoute.getStopByStation(departureStation);
        RouteStop changeArr = firstRoute.getStopByStation(changeStation);
        RouteStop changeDep = secondRoute.getStopByStation(changeStation);
        RouteStop arr = secondRoute.getStopByStation(arrivalStation);

        return "With a change: train " + firstRoute.getTrain().getTrainName() +
                " leaves " + departureStation + " at " + dep.getDepartureTime() +
                " and reaches " + changeStation + " at " + changeArr.getArrivalTime() +
                ", then train " + secondRoute.getTrain().getTrainName() +
                " leaves " + changeStation + " at " + changeDep.getDepartureTime() +
                " and gets to " + arrivalStation + " at " + arr.getArrivalTime();
    }
}

package business_layer;

import data_access.BookingDAO;
import data_access.RouteDAO;
import data_access.TrainDAO;
import model.Booking;
import model.Route;
import model.RouteStop;
import model.Train;

import java.util.ArrayList;

public class AdminBLL {
    private TrainDAO trainDAO;
    private RouteDAO routeDAO;
    private BookingDAO bookingDAO;
    private EmailService emailService;

    public AdminBLL(TrainDAO trainDAO, RouteDAO routeDAO, BookingDAO bookingDAO, EmailService emailService) {
        this.trainDAO = trainDAO;
        this.routeDAO = routeDAO;
        this.bookingDAO = bookingDAO;
        this.emailService = emailService;
    }

    public boolean addTrain(Train train) {
        if(trainDAO.findById(train.getId()) != null || train.getCapacity() <= 0) {
            return false;
        }

        trainDAO.addTrain(train);
        return true;
    }

    public boolean removeTrain(int id) {
        if(trainDAO.findById(id) == null) {
            return false;
        }

        routeDAO.removeRoutesForTrain(id);
        trainDAO.removeTrain(id);
        return true;
    }

    public boolean modifyTrain(int id, String newName, int newCapacity) {
        Train train = trainDAO.findById(id);

        if(train == null || newCapacity <= 0 || newCapacity < bookingDAO.countBookedTicketsForTrain(id)) {
            return false;
        }

        train.setTrainName(newName);
        train.setCapacity(newCapacity);
        return true;
    }

    public boolean addRoute(Route route) {
        if(routeDAO.findById(route.getId()) != null || route.getStops().size() < 2) {
            return false;
        }

        routeDAO.addRoute(route);
        return true;
    }

    public boolean removeRoute(int id) {
        if(routeDAO.findById(id) == null) {
            return false;
        }

        routeDAO.removeRoute(id);
        return true;
    }

    public boolean modifyRouteAddStop(int routeId, RouteStop stop) {
        Route route = routeDAO.findById(routeId);

        if(route == null) {
            return false;
        }

        route.addStop(stop);
        return true;
    }

    public boolean modifyRouteRemoveStop(int routeId, String stationName) {
        Route route = routeDAO.findById(routeId);

        if(route == null || route.getStops().size() <= 2) {
            return false;
        }

        return route.removeStop(stationName);
    }

    public boolean modifyRouteStop(int routeId, String oldStationName, RouteStop newStop) {
        Route route = routeDAO.findById(routeId);

        if(route == null) {
            return false;
        }

        return route.replaceStop(oldStationName, newStop);
    }

    public boolean modifyRouteTrain(int routeId, int trainId) {
        Route route = routeDAO.findById(routeId);
        Train train = trainDAO.findById(trainId);

        if(route == null || train == null) {
            return false;
        }

        route.setTrain(train);
        return true;
    }

    public Train getTrainById(int trainId) {
        return trainDAO.findById(trainId);
    }

    public ArrayList<Train> getAllTrains() {
        return trainDAO.findAll();
    }

    public ArrayList<Route> getAllRoutes() {
        return routeDAO.findAll();
    }

    public ArrayList<Booking> getBookingsForTrain(int trainId) {
        return bookingDAO.findBookingsForTrain(trainId);
    }

    public boolean setTrainDelay(int trainId, int delayMinutes) {
        Train train = trainDAO.findById(trainId);

        if(train == null || delayMinutes < 0) {
            System.out.println("I couldn't set that delay");
            return false;
        }

        train.setDelayMinutes(delayMinutes);

        ArrayList<Booking> bookings = bookingDAO.findBookingsForTrain(trainId);

        if(bookings.isEmpty()) {
            System.out.println("Delay saved, no booked customers to email yet");
            return true;
        }

        int sentEmails = 0;

        for(Booking booking : bookings) {
            if(emailService.sendDelayNotification(booking.getCustomer().getEmail(), train)) {
                sentEmails++;
            }
        }

        if(sentEmails == bookings.size()) {
            System.out.println("Delay saved and the booked customers were emailed");
        } else {
            System.out.println("Delay saved, but only " + sentEmails + " of " + bookings.size() + " emails were sent");
        }

        return true;
    }
}

package business_layer;

import data_access.BookingDAO;
import data_access.RouteDAO;
import data_access.TrainDAO;
import model.Booking;
import model.Customer;
import model.Route;
import model.Train;

import java.util.ArrayList;

public class BookingBLL {
    private BookingDAO bookingDAO;
    private TrainDAO trainDAO;
    private RouteDAO routeDAO;
    private EmailService emailService;
    private int nextBookingId = 1;

    public BookingBLL(BookingDAO bookingDAO, TrainDAO trainDAO, RouteDAO routeDAO, EmailService emailService) {
        this.bookingDAO = bookingDAO;
        this.trainDAO = trainDAO;
        this.routeDAO = routeDAO;
        this.emailService = emailService;
    }

    public boolean bookTickets(int trainId, String customerName, String email, String from, String to, int ticketCount) {
        Train train = trainDAO.findById(trainId);

        if(train == null) {
            System.out.println("I couldn't find that train");
            return false;
        }

        Route route = findRouteForTrain(trainId);

        if(route == null || !route.canTravelDirect(from, to)) {
            System.out.println("That train doesn't connect those stations in that order");
            return false;
        }

        if(ticketCount <= 0) {
            System.out.println("Ticket count needs to be at least 1");
            return false;
        }

        int availableSeats = getAvailableSeatsForPartOfRoute(route, trainId, from, to);

        if(ticketCount > availableSeats) {
            System.out.println("Not enough seats for that part of the trip, only " + availableSeats + " left");
            return false;
        }

        Customer customer = new Customer(customerName, email);
        Booking booking = new Booking(nextBookingId++, customer, train, from, to, ticketCount);

        bookingDAO.addBooking(booking);
        emailService.sendBookingConfirmation(email, booking);

        System.out.println("Booked, you're all set");
        return true;
    }

    public ArrayList<Booking> getBookingsForTrain(int trainId) {
        return bookingDAO.findBookingsForTrain(trainId);
    }

    private Route findRouteForTrain(int trainId) {
        for(Route route : routeDAO.findAll()) {
            if(route.getTrain().getId() == trainId) {
                return route;
            }
        }

        return null;
    }

    private int getAvailableSeatsForPartOfRoute(Route route, int trainId, String from, String to) {
        int departureIndex = route.getStationIndex(from);
        int arrivalIndex = route.getStationIndex(to);
        int[] seatsTakenBySegment = new int[route.getStops().size() - 1];

        for(Booking booking : bookingDAO.findBookingsForTrain(trainId)) {
            int bookedDepartureIndex = route.getStationIndex(booking.getDepartureStation());
            int bookedArrivalIndex = route.getStationIndex(booking.getArrivalStation());

            if(bookedDepartureIndex == -1 || bookedArrivalIndex == -1) {
                continue;
            }

            int overlapStart = Math.max(departureIndex, bookedDepartureIndex);
            int overlapEnd = Math.min(arrivalIndex, bookedArrivalIndex);

            for(int i = overlapStart; i < overlapEnd; i++) {
                seatsTakenBySegment[i] += booking.getNumberOfTickets();
            }
        }

        int minAvailable = route.getTrain().getCapacity();

        for(int i = departureIndex; i < arrivalIndex; i++) {
            int availableOnSegment = route.getTrain().getCapacity() - seatsTakenBySegment[i];

            if(availableOnSegment < minAvailable) {
                minAvailable = availableOnSegment;
            }
        }

        return minAvailable;
    }
}

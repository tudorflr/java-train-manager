package data_access;

import model.Booking;
import java.util.ArrayList;

public class BookingDAO {
    private ArrayList<Booking> bookings;

    public BookingDAO() {
        bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public ArrayList<Booking> findAll() {
        return bookings;
    }

    public ArrayList<Booking> findBookingsForTrain(int trainId) {
        ArrayList<Booking> result = new ArrayList<>();

        for(Booking booking : bookings) {
            if(booking.getTrain().getId() == trainId) {
                result.add(booking);
            }
        }

        return result;
    }

    public int countBookedTicketsForTrain(int trainId) {
        int total = 0;

        for(Booking booking : bookings) {
            if(booking.getTrain().getId() == trainId) {
                total += booking.getNumberOfTickets();
            }
        }

        return total;
    }
}

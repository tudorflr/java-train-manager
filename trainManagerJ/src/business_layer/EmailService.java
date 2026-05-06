package business_layer;

import model.Booking;
import model.Train;

public class EmailService {
    public void sendBookingConfirmation(String email, Booking booking) {
        System.out.println();
        System.out.println("------ email simulation ------");
        System.out.println("To: " + email);
        System.out.println("Subject: your train booking");
        System.out.println("Hi, your booking is in");
        System.out.println(booking);
        System.out.println("---------------------------");
        System.out.println();
    }

    public void sendDelayNotification(String email, Train train) {
        System.out.println();
        System.out.println("------ email simulation ------");
        System.out.println("To: " + email);
        System.out.println("Subject: train delay update");
        System.out.println("Heads up, train " + train.getTrainName() + " is running " + train.getDelayMinutes() + " min late");
        System.out.println("---------------------------");
        System.out.println();
    }
}

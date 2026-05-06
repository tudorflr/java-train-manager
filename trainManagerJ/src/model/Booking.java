package model;

public class Booking {
    private int id;
    private Customer customer;
    private Train train;
    private String departureStation;
    private String arrivalStation;
    private int numberOfTickets;

    public Booking(int id, Customer customer, Train train, String departureStation, String arrivalStation, int numberOfTickets) {
        this.id = id;
        this.customer = customer;
        this.train = train;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.numberOfTickets = numberOfTickets;
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Train getTrain() { return train; }
    public String getDepartureStation() { return departureStation; }
    public String getArrivalStation() { return arrivalStation; }
    public int getNumberOfTickets() { return numberOfTickets; }

    @Override
    public String toString() {
        return "Booking " + id + " | " + customer +
                " | train " + train.getTrainName() +
                " | " + departureStation + " to " + arrivalStation +
                " | tickets " + numberOfTickets;
    }
}

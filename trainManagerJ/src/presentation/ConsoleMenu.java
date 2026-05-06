package presentation;

import business_layer.AdminBLL;
import business_layer.BookingBLL;
import business_layer.RouteBLL;
import model.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner;
    private BookingBLL bookingBLL;
    private RouteBLL routeBLL;
    private AdminBLL adminBLL;

    public ConsoleMenu(BookingBLL bookingBLL, RouteBLL routeBLL, AdminBLL adminBLL) {
        this.bookingBLL = bookingBLL;
        this.routeBLL = routeBLL;
        this.adminBLL = adminBLL;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while(running) {
            System.out.println();
            System.out.println("===== Train ticketing =====");
            System.out.println("1. User menu");
            System.out.println("2. Admin menu");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int option = readInt();

            switch(option) {
                case 1:
                    userMenu();
                    break;
                case 2:
                    adminMenu();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("That option isn't in the menu");
            }
        }
    }

    private void userMenu() {
        boolean running = true;

        while(running) {
            System.out.println();
            System.out.println("----- User menu -----");
            System.out.println("1. Show trains");
            System.out.println("2. Show routes");
            System.out.println("3. Find departure/arrival times");
            System.out.println("4. Book tickets");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int option = readInt();

            switch(option) {
                case 1:
                    showTrains();
                    break;
                case 2:
                    showRoutes();
                    break;
                case 3:
                    findTravelOptions();
                    break;
                case 4:
                    bookTickets();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("That option isn't in the menu");
            }
        }
    }

    private void adminMenu() {
        boolean running = true;

        while(running) {
            System.out.println();
            System.out.println("----- Admin menu -----");
            System.out.println("1. Add train");
            System.out.println("2. Remove train");
            System.out.println("3. Edit train");
            System.out.println("4. Add route");
            System.out.println("5. Remove route");
            System.out.println("6. Add stop to route");
            System.out.println("7. Remove stop from route");
            System.out.println("8. Edit route stop");
            System.out.println("9. Change route train");
            System.out.println("10. Show bookings for train");
            System.out.println("11. Set train delay");
            System.out.println("12. Show trains");
            System.out.println("13. Show routes");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int option = readInt();

            switch(option) {
                case 1:
                    addTrain();
                    break;
                case 2:
                    removeTrain();
                    break;
                case 3:
                    modifyTrain();
                    break;
                case 4:
                    addRoute();
                    break;
                case 5:
                    removeRoute();
                    break;
                case 6:
                    addStopToRoute();
                    break;
                case 7:
                    removeStopFromRoute();
                    break;
                case 8:
                    modifyRouteStop();
                    break;
                case 9:
                    changeRouteTrain();
                    break;
                case 10:
                    showBookingsForTrain();
                    break;
                case 11:
                    setDelay();
                    break;
                case 12:
                    showTrains();
                    break;
                case 13:
                    showRoutes();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("That option isn't in the menu");
            }
        }
    }

    private void showTrains() {
        if(adminBLL.getAllTrains().isEmpty()) {
            System.out.println("No trains saved yet");
            return;
        }

        for(Train train : adminBLL.getAllTrains()) {
            System.out.println(train);
        }
    }

    private void showRoutes() {
        if(adminBLL.getAllRoutes().isEmpty()) {
            System.out.println("No routes saved yet");
            return;
        }

        for(Route route : adminBLL.getAllRoutes()) {
            System.out.println(route);
        }
    }

    private void findTravelOptions() {
        System.out.print("Departure station: ");
        String departure = scanner.nextLine();

        System.out.print("Arrival station: ");
        String arrival = scanner.nextLine();

        ArrayList<TravelOption> options = routeBLL.findTravelOptions(departure, arrival);

        if(options.isEmpty()) {
            System.out.println("I couldn't find a trip between those stations");
            return;
        }

        for(TravelOption option : options) {
            System.out.println(option);
        }
    }

    private void bookTickets() {
        System.out.print("Train id: ");
        int trainId = readInt();

        System.out.print("Your name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Departure station: ");
        String departure = scanner.nextLine();

        System.out.print("Arrival station: ");
        String arrival = scanner.nextLine();

        System.out.print("Number of tickets: ");
        int tickets = readInt();

        bookingBLL.bookTickets(trainId, name, email, departure, arrival, tickets);
    }

    private void addTrain() {
        System.out.print("Train id: ");
        int id = readInt();

        System.out.print("Train name: ");
        String name = scanner.nextLine();

        System.out.print("Capacity: ");
        int capacity = readInt();

        if(adminBLL.addTrain(new Train(id, name, capacity))) {
            System.out.println("Train added");
        } else {
            System.out.println("Couldn't add it, check the id and capacity");
        }
    }

    private void removeTrain() {
        System.out.print("Train id: ");
        int id = readInt();

        if(adminBLL.removeTrain(id)) {
            System.out.println("Train removed");
        } else {
            System.out.println("I couldn't find that train");
        }
    }

    private void modifyTrain() {
        System.out.print("Train id: ");
        int id = readInt();

        System.out.print("New train name: ");
        String name = scanner.nextLine();

        System.out.print("New capacity: ");
        int capacity = readInt();

        if(adminBLL.modifyTrain(id, name, capacity)) {
            System.out.println("Train updated");
        } else {
            System.out.println("Couldn't update it, check the id and capacity");
        }
    }

    private void addRoute() {
        System.out.print("Route id: ");
        int routeId = readInt();

        System.out.print("Train id for this route: ");
        int trainId = readInt();

        Train train = adminBLL.getTrainById(trainId);

        if(train == null) {
            System.out.println("I couldn't find that train");
            return;
        }

        Route route = new Route(routeId, train);

        System.out.print("How many stops? ");
        int stopCount = readInt();

        for(int i = 0; i < stopCount; i++) {
            System.out.println("Stop " + (i + 1));

            System.out.print("Station name: ");
            String stationName = scanner.nextLine();

            System.out.print("Arrival time: ");
            String arrival = scanner.nextLine();

            System.out.print("Departure time: ");
            String departure = scanner.nextLine();

            route.addStop(new RouteStop(new Station(stationName), arrival, departure));
        }

        if(adminBLL.addRoute(route)) {
            System.out.println("Route added");
        } else {
            System.out.println("Couldn't add the route, check the id and make sure it has at least 2 stops");
        }
    }

    private void removeRoute() {
        System.out.print("Route id: ");
        int id = readInt();

        if(adminBLL.removeRoute(id)) {
            System.out.println("Route removed");
        } else {
            System.out.println("I couldn't find that route");
        }
    }

    private void addStopToRoute() {
        System.out.print("Route id: ");
        int routeId = readInt();

        System.out.print("Station name: ");
        String stationName = scanner.nextLine();

        System.out.print("Arrival time: ");
        String arrival = scanner.nextLine();

        System.out.print("Departure time: ");
        String departure = scanner.nextLine();

        if(adminBLL.modifyRouteAddStop(routeId, new RouteStop(new Station(stationName), arrival, departure))) {
            System.out.println("Stop added");
        } else {
            System.out.println("I couldn't find that route");
        }
    }

    private void removeStopFromRoute() {
        System.out.print("Route id: ");
        int routeId = readInt();

        System.out.print("Station name to remove: ");
        String stationName = scanner.nextLine();

        if(adminBLL.modifyRouteRemoveStop(routeId, stationName)) {
            System.out.println("Stop removed");
        } else {
            System.out.println("Couldn't remove it, check the route and station");
        }
    }

    private void modifyRouteStop() {
        System.out.print("Route id: ");
        int routeId = readInt();

        System.out.print("Station name to edit: ");
        String oldStationName = scanner.nextLine();

        System.out.print("New station name: ");
        String newStationName = scanner.nextLine();

        System.out.print("New arrival time: ");
        String arrival = scanner.nextLine();

        System.out.print("New departure time: ");
        String departure = scanner.nextLine();

        RouteStop newStop = new RouteStop(new Station(newStationName), arrival, departure);

        if(adminBLL.modifyRouteStop(routeId, oldStationName, newStop)) {
            System.out.println("Route stop updated");
        } else {
            System.out.println("Couldn't update it, check the route and station");
        }
    }

    private void changeRouteTrain() {
        System.out.print("Route id: ");
        int routeId = readInt();

        System.out.print("New train id: ");
        int trainId = readInt();

        if(adminBLL.modifyRouteTrain(routeId, trainId)) {
            System.out.println("Route train updated");
        } else {
            System.out.println("Couldn't update it, check the route and train");
        }
    }

    private void showBookingsForTrain() {
        System.out.print("Train id: ");
        int trainId = readInt();

        ArrayList<Booking> bookings = adminBLL.getBookingsForTrain(trainId);

        if(bookings.isEmpty()) {
            System.out.println("No bookings for this train yet");
            return;
        }

        for(Booking booking : bookings) {
            System.out.println(booking);
        }
    }

    private void setDelay() {
        System.out.print("Train id: ");
        int trainId = readInt();

        System.out.print("Delay minutes: ");
        int delay = readInt();

        adminBLL.setTrainDelay(trainId, delay);
    }

    private int readInt() {
        while(true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch(NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}

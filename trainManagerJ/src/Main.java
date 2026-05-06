import business_layer.AdminBLL;
import business_layer.BookingBLL;
import business_layer.EmailService;
import business_layer.RouteBLL;
import data_access.BookingDAO;
import data_access.RouteDAO;
import data_access.TrainDAO;
import model.Route;
import model.RouteStop;
import model.Station;
import model.Train;
import presentation.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        TrainDAO trainDAO = new TrainDAO();
        RouteDAO routeDAO = new RouteDAO();
        BookingDAO bookingDAO = new BookingDAO();

        EmailService emailService = new EmailService();

        loadPredefinedData(trainDAO, routeDAO);

        BookingBLL bookingBLL = new BookingBLL(bookingDAO, trainDAO, routeDAO, emailService);
        RouteBLL routeBLL = new RouteBLL(routeDAO);
        AdminBLL adminBLL = new AdminBLL(trainDAO, routeDAO, bookingDAO, emailService);

        ConsoleMenu menu = new ConsoleMenu(bookingBLL, routeBLL, adminBLL);
        menu.start();
    }

    private static void loadPredefinedData(TrainDAO trainDAO, RouteDAO routeDAO) {
        Train train1 = new Train(1, "IR-100", 5);
        Train train2 = new Train(2, "R-220", 4);
        Train train3 = new Train(3, "IC-330", 6);

        trainDAO.addTrain(train1);
        trainDAO.addTrain(train2);
        trainDAO.addTrain(train3);

        Route route1 = new Route(1, train1);
        route1.addStop(new RouteStop(new Station(1, "Cluj"), "-", "08:00"));
        route1.addStop(new RouteStop(new Station(2, "Alba Iulia"), "09:20", "09:30"));
        route1.addStop(new RouteStop(new Station(3, "Sibiu"), "11:00", "11:10"));
        route1.addStop(new RouteStop(new Station(4, "Brasov"), "14:00", "-"));

        Route route2 = new Route(2, train2);
        route2.addStop(new RouteStop(new Station(5, "Cluj"), "-", "10:00"));
        route2.addStop(new RouteStop(new Station(6, "Oradea"), "12:10", "12:20"));
        route2.addStop(new RouteStop(new Station(7, "Arad"), "15:00", "-"));

        Route route3 = new Route(3, train3);
        route3.addStop(new RouteStop(new Station(8, "Sibiu"), "-", "12:00"));
        route3.addStop(new RouteStop(new Station(9, "Pitesti"), "14:00", "14:15"));
        route3.addStop(new RouteStop(new Station(10, "Bucuresti"), "16:30", "-"));

        routeDAO.addRoute(route1);
        routeDAO.addRoute(route2);
        routeDAO.addRoute(route3);
    }
}

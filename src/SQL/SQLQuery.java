package SQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.User;
import utils.DBConnection;
import utils.Query;
import utils.AppointmentTypeCount;
import static view_controller.CustomerManagementController.customers;
import view_controller.MainController;

/**
 *
 * @author Jacobi
 */
public class SQLQuery {

    public static Boolean authenticate(String userName, String password) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user WHERE userName = ? AND password = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String convertUTCtoLocalDateTime(String UTC_DB_String) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime UTCLDT = LocalDateTime.parse(UTC_DB_String, dtf);
        ZonedDateTime UTCZDT = UTCLDT.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        LocalDateTime LDT = UTCZDT.withZoneSameInstant(ZoneId.of(zid.toString())).toLocalDateTime();
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LDT);
    }

    public static String convertLocalDateTimeToUTCDateTime(LocalDateTime LDT) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZoneId zid = ZoneId.systemDefault();
        LocalDateTime UTC = LDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return UTC.toString();
    }

    public static Boolean checkIfOverlappingAppointments(int userId, LocalDateTime startLDT, LocalDateTime endLDT, int appointmentId) {
        try {
            //Returns true if no appointments are found that conflict with given user
            String startUTC = convertLocalDateTimeToUTCDateTime(startLDT);
            String endUTC = convertLocalDateTimeToUTCDateTime(endLDT);
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM appointment WHERE userId = ? AND appointmentId!=? AND"
                    + "? < end AND ? > start";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, userId);
            ps.setInt(2, appointmentId);
            ps.setString(3, startUTC);
            ps.setString(4, endUTC);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Boolean retrieveAppointmentsInFifteen(int userId) {
        try {
            //Returns true if no appointments are found that conflict with given user
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM appointment WHERE userId = ? AND start BETWEEN ? AND ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, userId);
            ps.setString(2, LocalDateTime.now(ZoneId.of("UTC")).toString());
            ps.setString(3, LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(15).toString());
            ps.execute();
            ResultSet rs = ps.getResultSet();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static User createUser(String userName) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user WHERE userName = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, userName);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            rs.next();
            User loggedInUser = new User(
                    rs.getInt("userId"),
                    rs.getString("userName"),
                    rs.getString("password"),
                    rs.getBoolean("active"),
                    rs.getTimestamp("createDate").toLocalDateTime(),
                    rs.getString("createdBy"),
                    rs.getTimestamp("lastUpdate").toLocalDateTime(),
                    rs.getString("lastUpdateBy")
            );
            return loggedInUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<User> retrieveAllUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getBoolean("active"),
                        rs.getTimestamp("createDate").toLocalDateTime(),
                        rs.getString("createdBy"),
                        rs.getTimestamp("lastUpdate").toLocalDateTime(),
                        rs.getString("lastUpdateBy")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public static ObservableList<Customer> retrieveAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM customer";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customerId"),
                        rs.getString("customerName"),
                        "phone",
                        "address",
                        "city",
                        "postalCode",
                        "country"
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    public static ObservableList<Appointment> retrieveAllAppointments(int userId) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM appointment INNER JOIN customer ON appointment.customerId = customer.customerId WHERE userId=?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, userId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointmentId"),
                        rs.getInt("customerId"),
                        rs.getInt("userId"),
                        convertUTCtoLocalDateTime(rs.getString("start")),
                        convertUTCtoLocalDateTime(rs.getString("end")),
                        rs.getString("type"),
                        rs.getString("customerName")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    public static ObservableList<Customer> retrieveCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT customerId, customerName, phone, address, city, postalCode, country FROM customer INNER JOIN address ON customer.addressId = address.addressId INNER JOIN city ON address.cityId = city.cityId INNER JOIN country on city.countryId = country.countryId ORDER By customerName ASC";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customerId"),
                        rs.getString("customerName"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("postalCode"),
                        rs.getString("country")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    public static ObservableList<Appointment> retrieveAppointments(LocalDateTime endTime, User loggedInUser) throws SQLException {
        ObservableList<Appointment> loggedInUsersAppointments = FXCollections.observableArrayList();
        int userId = loggedInUser.getUserId();
        LocalDateTime now = LocalDateTime.now();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user JOIN appointment ON user.userId = appointment.userId INNER JOIN customer ON appointment.customerId = customer.customerId WHERE user.userId=? AND appointment.end between ? and ? ORDER BY start ASC";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            //key-value mapping
            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setTimestamp(3, Timestamp.valueOf(endTime));
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointmentId"),
                        rs.getInt("customerId"),
                        rs.getInt("userId"),
                        convertUTCtoLocalDateTime(rs.getString("start")),
                        convertUTCtoLocalDateTime(rs.getString("end")),
                        rs.getString("type"),
                        rs.getString("customerName")
                );
                loggedInUsersAppointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("failed to retrieve appointments");
        }
        return loggedInUsersAppointments;
    }

    public static ObservableList<AppointmentTypeCount> retrieveAppointmentsFromMonth(LocalDateTime beginningOfMonthLDT, LocalDateTime endOfMonthLDT) throws SQLException {
        ObservableList<AppointmentTypeCount> appointmentTypesByMonth = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT type, COUNT(*) AS 'count' FROM appointment WHERE ? < start AND ? > end GROUP BY type";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            //key-value mapping
            ps.setTimestamp(1, Timestamp.valueOf(beginningOfMonthLDT));
            ps.setTimestamp(2, Timestamp.valueOf(endOfMonthLDT));
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                AppointmentTypeCount appointmentTypeCount = new AppointmentTypeCount(
                        rs.getString("type"),
                        rs.getInt("count")
                );
                appointmentTypesByMonth.add(appointmentTypeCount);
            }
        } catch (SQLException e) {
            System.out.println("failed to retrieve appointments");
        }
        return appointmentTypesByMonth;
    }

    public static Integer retrieveCustomerId(String customerName) throws SQLException {
        Integer ID = null;
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM customer WHERE customerName = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            //key-value mapping
            ps.setString(1, customerName);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                ID = rs.getInt("customerId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " unable to get customer id");
        }
        return ID;
    }

    public static Integer retrieveUserId(String userName) throws SQLException {
        Integer ID = null;
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT * FROM user WHERE userName = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            //key-value mapping
            ps.setString(1, userName);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                ID = rs.getInt("userId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " unable to get user id");
        }
        return ID;
    }

    public static int retrieveAddressId(String address, int cityId, String phone) throws SQLException {
        Integer addressId = null;
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT addressId FROM address WHERE address = ? AND cityId = ? AND phone = ?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, address);
            ps.setInt(2, cityId);
            ps.setString(3, phone);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                addressId = rs.getInt("addressId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return addressId;
    }

    public static int retrieveCountryId(String country) throws SQLException {
        Integer countryId = null;
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT countryId FROM country WHERE country=?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, country);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                countryId = rs.getInt("countryId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return countryId;
    }

    public static int retrieveCityId(String city, int countryId) throws SQLException {
        Integer cityId = null;
        try {
            Connection conn = DBConnection.startConnection();
            String selectStatement = "SELECT cityId FROM city WHERE city=? AND countryId=?";
            Query.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, city);
            ps.setInt(2, countryId);

            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                cityId = rs.getInt("cityId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cityId;
    }

    public static void insertAppointment(int customerId, int userId, String meetingType, LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO appointment (customerId, userId, type, start, end) VALUES (?,?,?,?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, customerId);
            ps.setInt(2, userId);
            ps.setString(3, meetingType);
            ps.setString(4, convertLocalDateTimeToUTCDateTime(startTime));
            ps.setString(5, convertLocalDateTimeToUTCDateTime(endTime));
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to add appointment");
        }
    }

    public static void modifyAppointment(int appointmentID, int customerId, LocalDateTime startTime, LocalDateTime endTime, String meetingType) {
        try {
            Connection conn = DBConnection.startConnection();
            String updateStatement = "UPDATE appointment SET customerId=?, start=?,end=?,type=? WHERE appointmentId=?";
            Query.setPreparedStatement(conn, updateStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, customerId);
            ps.setString(2, convertLocalDateTimeToUTCDateTime(startTime));
            ps.setString(3, convertLocalDateTimeToUTCDateTime(endTime));
            ps.setString(4, meetingType);
            ps.setInt(5, appointmentID);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to modify appointment");
        }
    }

    public static void modifyCustomer(int customerId, String customerName, int addressId) {
        try {
            Connection conn = DBConnection.startConnection();
            String updateStatement = "UPDATE customer SET customerName=?, addressId=? WHERE customerId=?";
            Query.setPreparedStatement(conn, updateStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, customerName);
            ps.setInt(2, addressId);
            ps.setInt(3, customerId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to insert customer");
        }
    }

    public static void deleteAppointment(int appointmentId) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String deleteStatement = "DELETE FROM appointment WHERE appointmentId = ?";
            Query.setPreparedStatement(conn, deleteStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, appointmentId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to delete appointment");
        }
    }

    public static void deleteCustomer(int customerId) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String deleteStatement = "DELETE FROM appointment WHERE customerId = ?";
            Query.setPreparedStatement(conn, deleteStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, customerId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable delete appointments that reference customer being deleted.");
        }
        try {
            Connection conn = DBConnection.startConnection();
            String deleteStatement = "DELETE FROM customer WHERE customerId = ?";
            Query.setPreparedStatement(conn, deleteStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, customerId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to delete customer");
        }
    }

    public static void insertAddress(int cityId, String phone, String address, String postalCode) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO address (cityId, phone, address, postalCode) VALUES (?,?,?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setInt(1, cityId);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.setString(4, postalCode);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertCity(String city, int countryId) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO city (city, countryId) VALUES (?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, city);
            ps.setInt(2, countryId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertCountry(String country) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO country (country) VALUES (?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, country);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to add address");
        }
    }

    public static void insertCustomer(String customerName, int addressId) throws SQLException {
        try {
            Connection conn = DBConnection.startConnection();
            String insertStatement = "INSERT INTO customer (customerName, addressId) VALUES (?,?)";
            Query.setPreparedStatement(conn, insertStatement);
            PreparedStatement ps = Query.getPreparedStatement();
            ps.setString(1, customerName);
            ps.setInt(2, addressId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "unable to insert customer");
        }
    }

}

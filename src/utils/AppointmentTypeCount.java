package utils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jacobi
 */
public class AppointmentTypeCount {
    public String type;
    public int count;
    
    public AppointmentTypeCount(String type, int count) throws SQLException {
        this.type = type;
        this.count = count;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}

package Model;

import java.io.Serializable;

/**
 * Created by hottelet on 10/24/17.
 */
public class Table implements Serializable {

    static final long serialVersionUID = 1L; //assign a long value

    private String restaurantId;
    private String seatNumber;
    private String tableId;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}

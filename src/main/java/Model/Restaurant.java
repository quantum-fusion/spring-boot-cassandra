package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hottelet on 10/23/17.
 */

public class Restaurant implements Serializable {

    static final long serialVersionUID = 1L; //assign a long value

    private String restaurantId;
    private String cuisine;
    private String seating;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getSeating() {
        return seating;
    }

    public void setSeating(String seating) {
        this.seating = seating;
    }
}

package ddwu.mobile.finalproject.ma01_20190936;

import java.io.Serializable;

public class FoodCodeDTO implements Serializable {
    private int _id;
    private String foodName;
    private String foodCode;

    public FoodCodeDTO() {
    }

    public FoodCodeDTO(String foodName, String foodCode) {
        this.foodName = foodName;
        this.foodCode = foodCode;
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    @Override
    public String toString() {
        return "FoodCodeDTO{" +
                "_id=" + _id +
                ", foodName='" + foodName + '\'' +
                ", foodCode='" + foodCode + '\'' +
                '}';
    }
}

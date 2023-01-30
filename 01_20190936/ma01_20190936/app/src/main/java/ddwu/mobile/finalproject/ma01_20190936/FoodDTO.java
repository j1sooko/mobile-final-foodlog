package ddwu.mobile.finalproject.ma01_20190936;

import java.io.Serializable;

public class FoodDTO  implements Serializable {
    private int _id;
    private String foodName;
    private String foodCode;
    private Double weight;
    private Double energy;

    public FoodDTO(String foodName, String foodCode) {
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "FoodDTO{" +
                "_id=" + _id +
                ", foodName='" + foodName + '\'' +
                ", foodCode='" + foodCode + '\'' +
                ", weight=" + weight +
                ", energy=" + energy +
                '}';
    }
}

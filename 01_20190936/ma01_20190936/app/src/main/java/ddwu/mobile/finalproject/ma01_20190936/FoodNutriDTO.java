package ddwu.mobile.finalproject.ma01_20190936;

import java.io.Serializable;

public class FoodNutriDTO implements Serializable {
    private int _id;
    private Double weight;
    private Double energy;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
        return "FoodNutriDTO{" +
                "_id=" + _id +
                ", weight=" + weight +
                ", energy=" + energy +
                '}';
    }
}

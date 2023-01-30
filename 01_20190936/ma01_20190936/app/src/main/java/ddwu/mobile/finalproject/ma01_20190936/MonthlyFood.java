package ddwu.mobile.finalproject.ma01_20190936;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MonthlyFood implements Serializable {

    private String yearMonth;
    private String totalMoney;
    private String totalCal;

    public MonthlyFood(String yearMonth, String totalMoney, String totalCal) {
        this.yearMonth = yearMonth;
        this.totalMoney = totalMoney;
        this.totalCal = totalCal;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalCal() {
        return totalCal;
    }

    public void setTotalCal(String totalCal) {
        this.totalCal = totalCal;
    }

    @Override
    public String toString() {
        return "MonthlyFood{" +
                "yearMonth='" + yearMonth + '\'' +
                ", totalMoney='" + totalMoney + '\'' +
                ", totalCal='" + totalCal + '\'' +
                '}';
    }
}

package ddwu.mobile.finalproject.ma01_20190936;

import java.io.Serializable;

/*
하나의 주소 정보를 저장하기 위한 DTO
Intent 에 저장 가능하게 하기 위하여
Serializable 인터페이스를 구현함
*/

public class RecordDTO implements Serializable{

    private long id;
    private String foodTime; //아침, 점심, 저녁, 간식
    private String date; //음식을 먹은 날짜
    private String foodName; //음식 이름
    private int price; //음식 가격
    private double calorie; //음식 열량
    private String address; //음식을 먹은 장소
    private String memo; //기타 메모
    private String photoPath; //사진 기록

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFoodTime() {
        return foodTime;
    }

    public void setFoodTime(String foodTime) {
        this.foodTime = foodTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    public String toString() {
        return "FoodDTO{" +
                "id=" + id +
                ", foodTime='" + foodTime + '\'' +
                ", date='" + date + '\'' +
                ", foodName='" + foodName + '\'' +
                ", price=" + price +
                ", calorie=" + calorie +
                ", address='" + address + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}

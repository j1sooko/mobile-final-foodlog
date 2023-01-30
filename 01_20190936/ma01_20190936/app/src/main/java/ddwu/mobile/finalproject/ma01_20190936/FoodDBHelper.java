package ddwu.mobile.finalproject.ma01_20190936;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "food_db";
    public final static String TABLE_NAME = "food_table";
    public final static String COL_ID = "_id";
    public final static String COL_FOODTIME = "foodTime";
    public final static String COL_DATE = "date";
    public final static String COL_MONTH_DATE = "monthDate"; //월별 조회용
    public final static String COL_FOODNAME = "foodName";
    public final static String COL_PRICE = "price";
    public final static String COL_CALORIE = "calorie";
    public final static String COL_ADDRESS = "address";
    public final static String COL_MEMO = "memo";
    public final static String COL_PATH = "path";


    public FoodDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_FOODTIME + " TEXT, " + COL_DATE + " TEXT, " + COL_MONTH_DATE + " TEXT, " + COL_FOODNAME + " TEXT, "
                + COL_PRICE + " TEXT, " + COL_CALORIE + " TEXT, " + COL_ADDRESS + " TEXT, " + COL_MEMO + " TEXT, " + COL_PATH + " TEXT);");

//		샘플 데이터
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '아침', '2021-11-5', '2021-11', '계란말이', '0', '100', '집', '계란은 2개 쓰는 게 좋다.\n설탕도 조금 넣자!!', null);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '점심', '2021-11-6', '2021-11', '알리오올리오', '10000', '412.15', '라라코스트', '역시 토마토나 크림보다 오일이 짱이다..', null);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '간식', '2021-11-6', '2021-11', '케이크', '8000', '450.7', '집', '오늘은 나의 생일~!!', null);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '아침', '2021-12-18', '2021-12', '시리얼', '2000', '100', '서경로 집', '그래놀라 블루베리맛', null);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '점심', '2021-12-18', '2021-12', '김치찌개', '5000', '350', '본가', '집에서 만들어먹음', null);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '간식', '2021-12-14', '2021-12', '붕어빵', '1000', '300', '학교 앞', '팥 반 슈크림 반이 최고', null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

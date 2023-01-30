package ddwu.mobile.finalproject.ma01_20190936;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {
    final static String TAG = "Fragment3";


    ListView lvMonthlyFoods = null;
    FoodDBHelper helper;
    Cursor cursor;
    MonthlyDataAdapter adapter;
    ArrayList<MonthlyFood> monthlyFoods;

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_3, container, false);

        lvMonthlyFoods = (ListView) rootView.findViewById(R.id.lvMonthlyFoods);
        helper = new FoodDBHelper(rootView.getContext());
        monthlyFoods = new ArrayList<>();
        adapter = new MonthlyDataAdapter(rootView.getContext(), R.layout.month_listview_layout, monthlyFoods);
        lvMonthlyFoods.setAdapter(adapter);

        return rootView;

    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume 실행");
        super.onResume();
        monthlyFoods.clear();
//        DB에서 데이터를 읽어와 Adapter에 설정
        readMonthlyData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        cursor 사용 종료
        if (cursor != null) cursor.close();
    }

    //DB에서 데이터를 읽어와 Adapter에 설정
    protected void readMonthlyData() {
        Log.d(TAG, "db 다시 읽기");
        SQLiteDatabase db = helper.getReadableDatabase();


        cursor = db.rawQuery("select " + FoodDBHelper.COL_MONTH_DATE + ", total(" + FoodDBHelper.COL_PRICE
                + "), total(" + FoodDBHelper.COL_CALORIE + ") from " + FoodDBHelper.TABLE_NAME
                + " group by " +  FoodDBHelper.COL_MONTH_DATE  + " order by " + FoodDBHelper.COL_DATE + " DESC" + ";", null);


        Log.d(TAG, "select " + FoodDBHelper.COL_MONTH_DATE + ", total(" + FoodDBHelper.COL_PRICE
                + "), total(" + FoodDBHelper.COL_CALORIE + ") from " + FoodDBHelper.TABLE_NAME
                + " group by " +  FoodDBHelper.COL_MONTH_DATE + ";");


        while (cursor.moveToNext()) {
            String yearMonth = cursor.getString(0);
            String totalMoney = cursor.getString(1);
            String totalCal = cursor.getString(2);
            monthlyFoods.add( new MonthlyFood(yearMonth, totalMoney, totalCal));
            Log.d(TAG, "monthlyFoods: " + monthlyFoods);
        }

        helper.close();


    }
}
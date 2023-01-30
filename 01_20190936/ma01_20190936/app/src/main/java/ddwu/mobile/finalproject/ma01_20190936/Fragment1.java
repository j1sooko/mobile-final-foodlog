package ddwu.mobile.finalproject.ma01_20190936;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    final static String TAG = "Fragment1";
    final static int UPDATE_ACTIVITY_CODE = 100;
    ListView lvFoods = null;
    FoodDBHelper helper;
    Cursor cursor;
    Cursor cursor2;
    MyCursorAdapter adapter;
    boolean needOnResume = true;

    TextView tvDate;
    Button btnDate;
    TextView tvTotalMoney;
    TextView tvTotalCal;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);

        tvDate = rootView.findViewById(R.id.tvDate);
        btnDate = rootView.findViewById(R.id.btnDate);
        tvTotalCal = rootView.findViewById(R.id.tvTotalCal);
        tvTotalMoney = rootView.findViewById(R.id.tvTotalMoney);

        lvFoods = (ListView) rootView.findViewById(R.id.lvFoods);
        helper = new FoodDBHelper(rootView.getContext());
        adapter = new MyCursorAdapter(rootView.getContext(), R.layout.listview_layout, null);
        lvFoods.setAdapter(adapter);

        //목표 칼로리 알림
        notificationTargetCal();

        //오늘 날짜로 설정
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String today = mYear +"-"+ (mMonth + 1) +"-"+ mDay;
        tvDate.setText(today);

        //날짜입력
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDate.setText(year+"-" + (month+1) + "-" + dayOfMonth);
                updateCursor();
            }
        }, mYear, mMonth, mDay);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnDate.isClickable()) {
                    datePickerDialog.show();
                }
            }
        });

//		리스트 뷰 클릭 처리(update)
        lvFoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RecordDTO food = new RecordDTO();

                Intent intent = new Intent(view.getContext(), UpdateFoodActivity.class);
                Log.d(TAG, "cursor id 값: " + cursor.getInt(cursor.getColumnIndex( FoodDBHelper.COL_ID)));

                //현재 클릭한 값의 Food 정보를 intent에 담음
                food.setId(cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COL_ID)));
                Log.d(TAG, "id 값: " + id);
                food.setFoodTime(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_FOODTIME)));
                food.setDate(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_DATE)));
                food.setFoodName(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_FOODNAME)));
                food.setPrice(cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COL_PRICE)));
                food.setCalorie(cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_CALORIE)));
                food.setAddress(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_ADDRESS)));
                food.setMemo(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_MEMO)));
                food.setPhotoPath(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_PATH)));

                intent.putExtra("food", food);
                getActivity().startActivityForResult(intent, UPDATE_ACTIVITY_CODE);
            }
        });



//		리스트 뷰 롱클릭 처리(remove)
        lvFoods.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final long del_id = id;

                String msg = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_FOODNAME)) + "을(를) 삭제하시겠습니까?";
                builder.setTitle("기록 삭제");
                builder.setMessage(msg);
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //"delete from contact_table where _id =" + id
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String[] whereArgs = new String[] { String.valueOf(del_id) };
                        String whereClause = FoodDBHelper.COL_ID + "=?";
                        int result = db.delete(FoodDBHelper.TABLE_NAME, whereClause, whereArgs);
                        updateCursor();

                    }
                });
                builder.setNegativeButton("취소", null);
                builder.setCancelable(false);
                builder.show();
                return true;
            }
        });



        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume 실행");
        super.onResume();
//        DB에서 데이터를 읽어와 Adapter에 설정
        if (needOnResume)
            updateCursor();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        cursor 사용 종료
        if (cursor != null) cursor.close();
        if (cursor2 != null) cursor2.close();
    }

    //DB에서 데이터를 읽어와 Adapter에 설정
    protected void updateCursor() {
        Log.d(TAG, "db 다시 읽기");
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = FoodDBHelper.COL_DATE + "=?";
        String[] selectArgs = new String[]{tvDate.getText().toString()};
        Log.d(TAG, selectArgs[0]);

        cursor = db.query(FoodDBHelper.TABLE_NAME, null, selection, selectArgs,
                null, null, null, null);

        adapter.changeCursor(cursor);


        cursor2 = db.rawQuery("select total(" + FoodDBHelper.COL_PRICE + "), total(" + FoodDBHelper.COL_CALORIE +
                ") from " + FoodDBHelper.TABLE_NAME + " where " +  FoodDBHelper.COL_DATE + "=?;", selectArgs);

        Log.d(TAG, "cursor2 sql : " + "select total(" + FoodDBHelper.COL_PRICE + "), total(" + FoodDBHelper.COL_CALORIE +
                ") from " + FoodDBHelper.TABLE_NAME + " where " +  FoodDBHelper.COL_DATE + "=" + tvDate.getText().toString() + ";");

        while (cursor2.moveToNext()) {
            tvTotalMoney.setText(cursor2.getString(0));
            tvTotalCal.setText(cursor2.getString(1));
        }
        helper.close();

        //목표 칼로리 알림
        notificationTargetCal();


    }

    //목표 칼로리 알림
    protected void notificationTargetCal() {
        SharedPreferences spf = getActivity().getSharedPreferences("targetNoti", 0);
        String targetCal = spf.getString("targetCal", null);

        if (targetCal != null && (Double.valueOf(tvTotalCal.getText().toString()) >= Double.valueOf(targetCal))) {
            Toast.makeText(getActivity(), "목표 칼로리 달성!", Toast.LENGTH_LONG).show();
            // Notification 출력
            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(getActivity(), getActivity().getString(R.string.CHANNEL_ID))
                    .setSmallIcon(R.drawable.ic_baseline_star_24)
                    .setShowWhen(true)
                    .setContentTitle("푸드로그")
                    .setContentText("내 목표 칼로리를 달성하였어요!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

            int notificationId = 200;

            notificationManager.notify(notificationId, builder.build());
        }
    }

}
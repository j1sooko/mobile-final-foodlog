package ddwu.mobile.finalproject.ma01_20190936;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    final static String TAG = "Fragment2";
    final static int UPDATE_ACTIVITY_CODE = 100;
    ListView lvFoods = null;
    FoodDBHelper helper;
    Cursor cursor;
    //    SimpleCursorAdapter adapter; //기본어댑터
    AllDataCursorAdapter adapter;
    boolean needOnResume = true;

    TextView tvDate;
    Button btnDate;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        tvDate = rootView.findViewById(R.id.tvDate);
        btnDate = rootView.findViewById(R.id.btnDate);

        lvFoods = (ListView) rootView.findViewById(R.id.lvFoods);
        helper = new FoodDBHelper(rootView.getContext());
        adapter = new AllDataCursorAdapter(rootView.getContext(), R.layout.all_listview_layout, null);
        lvFoods.setAdapter(adapter);


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
    }

    //DB에서 데이터를 읽어와 Adapter에 설정
    protected void updateCursor() {
        Log.d(TAG, "db 다시 읽기");
        SQLiteDatabase db = helper.getReadableDatabase();

        cursor = db.rawQuery("select * from " + FoodDBHelper.TABLE_NAME + " order by " + FoodDBHelper.COL_DATE + " DESC", null);

        adapter.changeCursor(cursor);
        helper.close();
    }
}
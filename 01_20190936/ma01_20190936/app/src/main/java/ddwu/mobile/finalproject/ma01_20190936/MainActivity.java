package ddwu.mobile.finalproject.ma01_20190936;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static int UPDATE_ACTIVITY_CODE = 100;
    private static final String TAG = "MainActivity";

    FoodDBHelper helper;
    Cursor cursor;
    //    SimpleCursorAdapter adapter; //기본어댑터
    MyCursorAdapter adapter;
    boolean needOnResume = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewPager와 VPAdapter 연결
        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        //연동
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(vp);

        //새로운 항목 추가
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertFoodActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_ACTIVITY_CODE) {
            switch (resultCode) {
                case RESULT_OK:
//                    needOnResume = true;
                    Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
//                    needOnResume = false;
                    Toast.makeText(this, "수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item01:
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.item02:
                Intent intent2 = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }
}



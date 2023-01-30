package ddwu.mobile.finalproject.ma01_20190936;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateFoodActivity extends AppCompatActivity {
    private static final String TAG = "UpdateFoodActivity";
    private static final int REQUEST_TAKE_PHOTO = 200;
    private static final int SEARCH_CAL_REQ_CODE = 300;


    EditText etFoodName;
    EditText etPrice;
    EditText etCal;
    EditText etAddress;
    EditText etMemo;
    TextView tvFoodTime;
    Spinner spinner;
    TextView tvDate;
    Button btnDate;
    String monthDate;

    //사진
    ImageView ivPhoto;
    private String mCurrentPhotoPath;

    FoodDBHelper helper;
    RecordDTO food;

    //지도
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private Marker centerMarker;
    private Geocoder geocoder;
    //마커를 담은 ArrayList
    private ArrayList<Marker> markers = new ArrayList<>();
    //새로운 마커
    private Marker newMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_activity);

        //수정할 movie 객체 꺼내옴
        food = (RecordDTO) getIntent().getSerializableExtra("food");

        etFoodName = findViewById(R.id.etFoodName);
        etPrice = findViewById(R.id.etPrice);
        etCal = findViewById(R.id.etCal);
        etAddress = findViewById(R.id.etAddress);
        etMemo = findViewById(R.id.etMemo);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvFoodTime = findViewById(R.id.tvFoodTime);
        tvDate = findViewById(R.id.tvDate);
        btnDate = findViewById(R.id.btnDate);
        ivPhoto = findViewById(R.id.ivPhoto);

        etFoodName.setText(food.getFoodName());
        etPrice.setText(String.valueOf(food.getPrice()));
        etCal.setText(String.valueOf(food.getCalorie()));
        etAddress.setText(food.getAddress());
        etMemo.setText(food.getMemo());
        tvFoodTime.setText(food.getFoodTime());
        tvDate.setText(food.getDate());
        String path = food.getPhotoPath();

        //사진 경로가 있을 경우에만 화면에 표시
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivPhoto.setImageBitmap(bitmap);
        }
        ivPhoto.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    외부 카메라 호출
                    dispatchTakePictureIntent();
                    return true;
                }
                return false;
            }
        });

        //지도
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);
        geocoder = new Geocoder(this, Locale.getDefault());


        //날짜입력
        String[] date = food.getDate().split("-");
        Log.d(TAG, "date: " + date);
        int mYear = Integer.parseInt(date[0]);
        int mMonth = Integer.parseInt(date[1]) - 1;
        int mDay = Integer.parseInt(date[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDate.setText(year+"-" + (month+1) + "-" + dayOfMonth);
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

        //foodTime 드롭다운 스피너
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvFoodTime.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        helper = new FoodDBHelper(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchCal:
                Log.d(TAG, "검색버튼 클릭");
                Intent intent = new Intent(this, SearhCalActivity.class);
//                startActivityForResult(intent, SEARCH_CAL_REQ_CODE);
                startActivityForResult(intent, SEARCH_CAL_REQ_CODE);
                break;
            case R.id.btnUpdate:
//                DB 데이터 업데이트 작업 수행
                food.setFoodName(etFoodName.getText().toString());
                food.setPrice(Integer.parseInt(etPrice.getText().toString()));
                food.setCalorie(Double.parseDouble(etCal.getText().toString()));
                food.setAddress(etAddress.getText().toString());
                food.setMemo(etMemo.getText().toString());
                food.setDate(tvDate.getText().toString());

                String date[] = food.getDate().split("-");
                monthDate = date[0] + "-" + date[1];

                Log.d(TAG, "update monthDate : " + monthDate);

                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues row = new ContentValues();
                row.put(FoodDBHelper.COL_FOODTIME, food.getFoodTime());
                row.put(FoodDBHelper.COL_DATE, food.getDate());
                row.put(FoodDBHelper.COL_MONTH_DATE, monthDate);
                row.put(FoodDBHelper.COL_FOODNAME, food.getFoodName());
                row.put(FoodDBHelper.COL_PRICE, food.getPrice());
                row.put(FoodDBHelper.COL_CALORIE, food.getCalorie());
                row.put(FoodDBHelper.COL_ADDRESS, food.getAddress());
                row.put(FoodDBHelper.COL_MEMO, food.getMemo());
                row.put(FoodDBHelper.COL_PATH, mCurrentPhotoPath);

                String whereClause = FoodDBHelper.COL_ID + "=?";
                //intent movie 객체의 id를 얻어옴
                String[] whereArgs = new String[]{String.valueOf(food.getId())};

                int result = db.update(helper.TABLE_NAME, row, whereClause, whereArgs);

                if (result > 0) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("food", food);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    Toast.makeText(this, "수정 실패!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.btnUpdateCancel:
//                DB 데이터 업데이트 작업 취소
                Toast.makeText(this, "수정 취소!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                break;
        }


    }

    /*원본 사진 파일 저장*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "ddwu.mobile.finalproject.ma01_20190936",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    /*현재 시간 정보를 사용하여 파일 정보 생성*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*사진의 크기를 ImageView에서 표시할 수 있는 크기로 변경*/
    private void setPic() {
        // Get the dimensions of the View
        int targetW = ivPhoto.getWidth();
        int targetH = ivPhoto.getHeight();
        Log.d(TAG, "w: " + targetW + " H: " + targetH);


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ivPhoto.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();

        }
        /* 음식 칼로리 검색 */
        if (requestCode == SEARCH_CAL_REQ_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    FoodDTO newFood = (FoodDTO) data.getSerializableExtra("food");
                    Log.d(TAG, "foodName: " + newFood.getFoodName());
                    Log.d(TAG, "foodCal: " + newFood.getEnergy());
                    etFoodName.setText(newFood.getFoodName());
                    etCal.setText(String.valueOf(newFood.getEnergy()));

                    break;
            }
        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) { //지도가 준비 됨
            Log.d(TAG, "맵 준비됨");

            mGoogleMap = googleMap;
            List<LatLng> address = getLatLng(etAddress.getText().toString());

            Log.d(TAG, "latlng: " + address);

            LatLng currentLoc;
            if (address != null)
                currentLoc = address.get(0);
            else
                currentLoc = new LatLng(37.606320, 127.041808);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions centerMarkerOptions = new MarkerOptions();
            centerMarkerOptions.position(currentLoc);
            centerMarkerOptions.title("먹은 장소");
            centerMarkerOptions.snippet("기록된 장소");
            centerMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            centerMarker = mGoogleMap.addMarker(centerMarkerOptions);
            centerMarker.showInfoWindow();

            //마커 윈도우 클릭 이벤트
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
//                    Toast.makeText(MainActivity.this, "마커 아이디: " + marker.getId(),
//                            Toast.LENGTH_SHORT).show();

//                    실제 주소 출력
                    double latitude = marker.getPosition().latitude;
                    double longitude = marker.getPosition().longitude;

                    Log.d(TAG, String.format("Latitude: %f\nLongitude: %f", latitude, longitude));
                    List<String> address = getAddress(latitude, longitude);
                    Log.d(TAG, address.get(0));
                    if (address == null) {
                        etAddress.setText("No data");
                    }

                    //현재 위치를 지오코딩한 값을 화면에 표시
                    etAddress.setText(address.get(0));

                }
            });

            //지도 클릭 이벤트
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
//                    Toast.makeText(MainActivity.this, "위도: " + latLng.latitude +
//                            "\n경도: " + latLng.longitude, Toast.LENGTH_SHORT).show();
                }
            });

            //지도 롱클릭 이벤트: 새로운 마커 추가
            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MarkerOptions newOptions;
                    newOptions = new MarkerOptions();
                    newOptions.position(latLng);
                    newOptions.title("추가한 마커");
                    newOptions.snippet(String.format("위도: %.6f/ 경도: %.6f", latLng.latitude, latLng.longitude));
                    newOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                    newMarker = mGoogleMap.addMarker(newOptions);

                    markers.add(newMarker);

                }
            });
        }
    };


    //    Geocoding
    private List<String> getAddress(double latitude, double longitude) {

        List<Address> addresses = null;
        ArrayList<String> addressFragments = null;

//        위도/경도에 해당하는 주소 정보를 Geocoder 에게 요청
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses == null || addresses.size()  == 0) {
            return null;
        } else {
            Address addressList = addresses.get(0);
            addressFragments = new ArrayList<String>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                addressFragments.add(addressList.getAddressLine(i));
            }
        }

        return addressFragments;
    }

    //        주소에 해당하는 위도/경도 정보를 Geocoder 에게 요청
    private List<LatLng> getLatLng(String location) {

        List<Address> addresses = null;
        ArrayList<LatLng> addressFragments = null;

        try {
            addresses = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) { // Catch network or other I/O problems.
            e.printStackTrace();
        } catch (IllegalArgumentException e) { // Catch invalid address values.
            e.printStackTrace();
        }

//        결과로부터 위도/경도 추출
        if (addresses == null || addresses.size()  == 0) {
            return null;
        } else {
            Address addressList = addresses.get(0);
            addressFragments = new ArrayList<LatLng>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                LatLng latLng = new LatLng(addressList.getLatitude(), addressList.getLongitude());
                addressFragments.add(latLng);
            }

        }
        return addressFragments;

    }
}

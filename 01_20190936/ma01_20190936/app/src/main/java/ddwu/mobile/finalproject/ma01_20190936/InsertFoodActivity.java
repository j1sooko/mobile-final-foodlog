package ddwu.mobile.finalproject.ma01_20190936;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InsertFoodActivity extends AppCompatActivity {
    private static final String TAG = "InsertFoodActivity";
    private static final int REQUEST_TAKE_PHOTO = 200;
    final static int PERMISSION_REQ_CODE = 100;
    private static final int SEARCH_CAL_REQ_CODE = 300;


    FoodDBHelper helper;


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

    //??????
    private String mCurrentPhotoPath;
    ImageView ivPhoto;


    //??????
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private Marker centerMarker;
    private Geocoder geocoder;
    //????????? ?????? ArrayList
    private ArrayList<Marker> markers = new ArrayList<>();
    //????????? ??????
    private Marker newMarker;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_food);

        helper = new FoodDBHelper(this);

        etFoodName = findViewById(R.id.etFoodName);
        etPrice = findViewById(R.id.etPrice);
        etCal = findViewById(R.id.etCal);
        etAddress = findViewById(R.id.etAddress);
        etMemo = findViewById(R.id.etMemo);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvFoodTime = findViewById(R.id.tvFoodTime);
        tvDate = findViewById(R.id.tvDate);
        btnDate = findViewById(R.id.btnDate);

        //??????
        ivPhoto = (ImageView)findViewById(R.id.ivPhoto);
        ivPhoto.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    ?????? ????????? ??????
                    dispatchTakePictureIntent();
                    return true;
                }
                return false;
            }
        });

        //??????
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);
        geocoder = new Geocoder(this, Locale.getDefault());

        locationUpdate();

        //?????? ????????? ??????
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String today = mYear +"-"+ (mMonth + 1) +"-"+ mDay;
        tvDate.setText(today);

        //????????????
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

        //foodTime ???????????? ?????????
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvFoodTime.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSearchCal:
                Log.d(TAG, "???????????? ??????");
                Intent intent = new Intent(this, SearhCalActivity.class);
//                startActivityForResult(intent, SEARCH_CAL_REQ_CODE);
                startActivityForResult(intent, SEARCH_CAL_REQ_CODE);
                break;
            case R.id.btnInsert:
//			DB ????????? ?????? ?????? ??????
                SQLiteDatabase db = helper.getWritableDatabase();

                String date[] = tvDate.getText().toString().split("-");
                monthDate = date[0] + "-" + date[1];

                Log.d(TAG, "insert monthDate : " + monthDate);
                //type1
                ContentValues row = new ContentValues();
                row.put(FoodDBHelper.COL_FOODTIME, tvFoodTime.getText().toString());
                row.put(FoodDBHelper.COL_DATE, tvDate.getText().toString());
                row.put(FoodDBHelper.COL_MONTH_DATE, monthDate);
                row.put(FoodDBHelper.COL_FOODNAME, etFoodName.getText().toString());
                row.put(FoodDBHelper.COL_PRICE, etPrice.getText().toString());
                row.put(FoodDBHelper.COL_CALORIE, etCal.getText().toString());
                row.put(FoodDBHelper.COL_ADDRESS, etAddress.getText().toString());
                row.put(FoodDBHelper.COL_MEMO, etMemo.getText().toString());
                row.put(FoodDBHelper.COL_PATH, mCurrentPhotoPath);


                db.insert(FoodDBHelper.TABLE_NAME, null, row);

                //type2
//		db.execSQL("insert into " + ContactDBHelper.TABLE_NAME + " values ( NULL, '"
//				+ etName.getText().toString()  + "', " + etPhone.getText().toString()
//				+ "', '" + etCategory.getText().toString() + "'');");


                helper.close();
                finish();

                break;
            case R.id.btnInsertCancel:
                finish();

//			DB ????????? ?????? ?????? ??????
                break;
        }
    }


    /*?????? ?????? ?????? ??????*/
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

    /*?????? ?????? ????????? ???????????? ?????? ?????? ??????*/
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

    /*????????? ????????? ImageView?????? ????????? ??? ?????? ????????? ??????*/
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
        /* ?????? ????????? ?????? */
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

    private void locationUpdate() {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5000, 0, locationListener);
        }

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            //????????? ????????? ????????? ?????? ??????
            centerMarker.setPosition(currentLoc);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) { //????????? ?????? ???
            Log.d(TAG, "??? ?????? ??? ");

            mGoogleMap = googleMap;

            LatLng currentLoc = new LatLng(37.606320, 127.041808);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions centerMarkerOptions = new MarkerOptions();
            centerMarkerOptions.position(currentLoc);
            centerMarkerOptions.title("?????? ??????");
            centerMarkerOptions.snippet("?????? ???");
            centerMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            centerMarker = mGoogleMap.addMarker(centerMarkerOptions);
            centerMarker.showInfoWindow();

            //?????? ????????? ?????? ?????????
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
//                    Toast.makeText(MainActivity.this, "?????? ?????????: " + marker.getId(),
//                            Toast.LENGTH_SHORT).show();

//                    ?????? ?????? ??????
                    double latitude = marker.getPosition().latitude;
                    double longitude = marker.getPosition().longitude;

                    Log.d(TAG, String.format("Latitude: %f\nLongitude: %f", latitude, longitude));
                    List<String> address = getAddress(latitude, longitude);
                    Log.d(TAG, address.get(0));
                    if (address == null) {
                        etAddress.setText("No data");
                    }

                    //?????? ????????? ??????????????? ?????? ????????? ??????
                    etAddress.setText(address.get(0));

                }
            });

            //?????? ?????? ?????????
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
//                    Toast.makeText(MainActivity.this, "??????: " + latLng.latitude +
//                            "\n??????: " + latLng.longitude, Toast.LENGTH_SHORT).show();
                }
            });

            //?????? ????????? ?????????: ????????? ?????? ??????
            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MarkerOptions newOptions;
                    newOptions = new MarkerOptions();
                    newOptions.position(latLng);
                    newOptions.title("????????? ??????");
                    newOptions.snippet(String.format("??????: %.6f/ ??????: %.6f", latLng.latitude, latLng.longitude));
                    newOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                    newMarker = mGoogleMap.addMarker(newOptions);

                    markers.add(newMarker);

                }
            });
        }
    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    //    Geocoding
    private List<String> getAddress(double latitude, double longitude) {

        List<Address> addresses = null;
        ArrayList<String> addressFragments = null;

//        ??????/????????? ???????????? ?????? ????????? Geocoder ?????? ??????
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
}

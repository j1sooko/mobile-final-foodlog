package ddwu.mobile.finalproject.ma01_20190936;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearhCalActivity extends AppCompatActivity {
    public static final String TAG = "SearchCalActivity";
    EditText etFoodName;
    ListView lvSearchFood;

    String codeApiAddress;
    String nutriApiAddress;
    String food_Name;

    FoodCalAdapter adapter;
    ArrayList<FoodCodeDTO> foodCodeList;
    ArrayList<FoodNutriDTO> foodNutriList;
    ArrayList<FoodDTO> foodList;

    FoodCodeXmlParser codeParser;
    FoodNutriXmlParser nutriParser;

    NetworkManager networkManager;

    FoodDTO newFood;
    Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foodcal);

        etFoodName = findViewById(R.id.etSearchFoodName);


        lvSearchFood = findViewById(R.id.lvSearchFood);

        foodCodeList = new ArrayList();
        foodNutriList = new ArrayList();

        adapter = new FoodCalAdapter(this, R.layout.listview_search_layout, foodCodeList);
        lvSearchFood.setAdapter(adapter);

        codeApiAddress = getResources().getString(R.string.foodcode_api_url);
        codeParser = new FoodCodeXmlParser();
        nutriApiAddress = getResources().getString(R.string.foodnutri_api_url);
        nutriParser = new FoodNutriXmlParser();

        networkManager = new NetworkManager(this);

        resultIntent = new Intent();

        lvSearchFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                리스트 항목을 클릭하면 activity를 닫고 칼로리 정보(지금은 임시로 푸드코드)와 음식 이름을 저장한다->칼로리와 음식 이름을 각각 원래 insert액티비티의 etFoodName곽 etCal에 설정

                //foodcode랑 nutri랑 조합한 food
                newFood = new FoodDTO(foodCodeList.get(position).getFoodName(), foodCodeList.get(position).getFoodCode());

                try {
                    StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/MzenFoodNutri/getKoreanFoodIdntList"); /*URL*/
                    urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + (getResources().getString(R.string.foodnutri_service_key))); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("food_Code","UTF-8") + "=" + URLEncoder.encode(foodCodeList.get(position).getFoodCode(), "UTF-8")); /*음식 코드 식별ID 값 * 식단관리(메뉴젠) 음식 정보 API 참조 (https://www.data.go.kr/data/15077804/openapi.do)*/
//                    String nutriAddress = urlBuilder.toString();
                    Log.d(TAG, "url: " + urlBuilder.toString());

                    new NetworkAsyncTask2().execute(urlBuilder.toString()); //2개
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void onClick(View v) {
//        버튼의 종류에 따라 결과 설정 후 finish()
        switch (v.getId()) {
            case R.id.btnSearch:
                Log.d(TAG, "검색 버튼 클릭");
                //네트워크 매니저에서 주소, 인증키, 음식이름으로 푸드 코드를 얻어와 parser로 해석한 후 List<DTO>를 받아와 adapter에 설정
                food_Name = etFoodName.getText().toString();  // UTF-8 인코딩 필요
                // OpenAPI 주소와 query 조합 후 서버에서 데이터를 가져옴
                // 가져온 데이터는 파싱 수행 후 어댑터에 설정
                try {
                    //url 생성
                    StringBuilder urlBuilder = new StringBuilder(getResources().getString(R.string.foodcode_api_url)); /*URL*/
                    urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + getResources().getString(R.string.foodcode_service_key)); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("Page_No","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                    urlBuilder.append("&" + URLEncoder.encode("Page_Size","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8")); /*한 페이지 결과 수*/
                    urlBuilder.append("&" + URLEncoder.encode("food_Group_Code","UTF-8") + "=" + URLEncoder.encode("06", "UTF-8")); /*식품분류 식별ID 값 * 식품분류코드표 참조*/
                    urlBuilder.append("&" + URLEncoder.encode("food_Name","UTF-8") + "=" + URLEncoder.encode(food_Name, "UTF-8")); /*식품명 (검색어 입력값 포함 검색)*/
//                    String codeAdderss = urlBuilder.toString();
                    Log.d(TAG, "url: " + urlBuilder.toString());

                    new NetworkAsyncTask1().execute(urlBuilder.toString()); //2개
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                break;
        }

    }

    class NetworkAsyncTask1 extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearhCalActivity.this, "Wait", "Downloading...");
        }

        //        별개의 스레드 작업(화면요소x)
        @Override
        protected String doInBackground(String... strings) {
            String codeAddress = strings[0];
            String codeResult = null;
            // networking
            codeResult = networkManager.downloadContents(codeAddress);
            if (codeResult == null) return "Error!";

            foodCodeList = codeParser.parse(codeResult);
            Log.d(TAG, "foodList: " + foodCodeList);


            return codeResult;
        }


        @Override
        protected void onPostExecute(String result) {
//            // parsing - 수행시간이 짧을 경우 이 부분에서 수행하는 것을 고려

            adapter.setList(foodCodeList);    // Adapter 에 결과 List 를 설정 후 notify
            progressDlg.dismiss();
        }

    }

    class NetworkAsyncTask2 extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearhCalActivity.this, "Wait", "Downloading...");
        }

        //        별개의 스레드 작업(화면요소x)
        @Override
        protected String doInBackground(String... strings) {
            String codeAddress = strings[0];
            String codeResult = null;
            // networking
            codeResult = networkManager.downloadContents(codeAddress);
            if (codeResult == null) return "Error!";

            foodNutriList = nutriParser.parse(codeResult);
            Log.d(TAG, "foodNutriList: " + foodNutriList);


            return codeResult;
        }


        @Override
        protected void onPostExecute(String result) {
//            // parsing - 수행시간이 짧을 경우 이 부분에서 수행하는 것을 고려
//            resultList = parser.parse(result);

            double sumEnergy = 0;
            for (FoodNutriDTO f : foodNutriList) {
                sumEnergy += f.getEnergy();
            }
            newFood.setEnergy((double)Math.round(sumEnergy));
            Log.d(TAG, "newFood: " + newFood);
            resultIntent.putExtra("food", newFood);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(SearhCalActivity.this, newFood.getFoodName() + ":" + newFood.getEnergy() + " 검색!", Toast.LENGTH_SHORT).show();
            finish();

            progressDlg.dismiss();

        }

    }

}

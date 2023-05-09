package com.example.onair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    TextView title;
    RecyclerView content;
    ImageButton info;
    Button searchButton;
    EditText editText;
    String cityArr[] = {"전국", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
    String sidoName;
    int totalCount;
    String serviceKey = "q5DKcdsDvkj7U6lHlGV2vcW6qz4XEGcdXp%2BG3Pe15l03OIJb7Zn0%2BIBGJTTZmTByK16tYukvvpXYGi3wXYp4ig%3D%3D";
    String urlStr;
    String tempCity;
    PMAdapter adapter;
    RecyclerView recyclerView;
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.textView);
        content = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PMAdapter();
        recyclerView.setAdapter(adapter);

        info = findViewById(R.id.imageButton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage();
            }
        });

        searchButton = findViewById(R.id.button2);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.updateList(adapter.items);
                tempCity = editText.getText().toString();
                if (citySearch(tempCity)) {
                    title.setText(tempCity + " 실시간 미세먼지 정보");
                    sidoName = tempCity;
                    urlStr = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName="+ sidoName +"&pageNo=1&numOfRows=1000&returnType=json&serviceKey=" + serviceKey+ "&ver=1.0";
                    makeRequest();
                } else {
                    title.setText("해당 시/도는 목록에 없습니다.");
                }
            }
        });
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private boolean citySearch(String city) {
        if (city.length() == 3 && (city.endsWith("시") || city.endsWith("도"))) {
            city = city.substring(0, 2);
        } else if(city.length() == 4 && city.endsWith("도")) {
            city = city.substring(0, 1) + city.substring(2, 3);
        }
        for (int i = 0; i < cityArr.length; i++) {
            if (cityArr[i].equals(city)) {
                tempCity = city;
                return true;
            }
        }
        return false;
    }

    private void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.info);
        builder.setTitle(" 도움말");
        builder.setMessage("\n입력 가능한 시/도 목록 " +
                "\n\n전국      서울      부산      대구      인천     광주" +
                "\n\n대전      울산      경기      강원      충북     충남" +
                "\n\n전북      전남      경북      경남      제주     세종" +
                "\n\n통합대기환경지수 기준"+
                "\n\n좋음(0 ~ 50)\n\n보통(51~100)\n\n나쁨(101~250)\n\n매우나쁨(251~500)\n");
        builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        }).create().show();
    }

    private void makeRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("에러 : ", "error");
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void processResponse(String response) {
        Gson gson = new Gson();
        PMResponse pmResponse = gson.fromJson(response, PMResponse.class);
        for (int i = 0; i < pmResponse.response.body.items.size(); i++) {
            PMItem item = pmResponse.response.body.items.get(i);
            adapter.addItem(item);
        }
        adapter.notifyDataSetChanged();
    }
}
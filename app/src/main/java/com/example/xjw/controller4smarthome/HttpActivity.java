package com.example.xjw.controller4smarthome;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpActivity extends AppCompatActivity {
    private final static String TAG = "HttpActivity";

    final String[] myGetUrl = {
            "",
            "",
            "http://192.168.123.2:8123/api/states/switch.heater",
            "http://192.168.123.2:8123/api/states/switch.cooler",
            "http://192.168.123.2:8123/api/states/switch.light1",
            "http://192.168.123.2:8123/api/states/switch.light2",
            "http://192.168.123.2:8123/api/states/sensor.temperature",
            "http://192.168.123.2:8123/api/states/sensor.humidity",
            "http://192.168.123.2:8123/api/states/sensor.dust",
            "http://192.168.123.2:8123/api/states/sensor.fire",
            "http://www.baidu.com"
    };
    final String[] myPostUrl = {
            "http://192.168.123.2:8123/api/services/mqtt/publish",
            "http://192.168.123.2:8123/api/services/mqtt/publish",
            "http://192.168.123.2:8123/api/services/mqtt/publish",
            "http://192.168.123.2:8123/api/services/mqtt/publish",
            "http://192.168.123.2:8123/api/services/mqtt/publish",
            "http://192.168.123.2:8123/api/services/mqtt/publish"
    };

    String[] devices = {
            "door", "window", "switch.heater", "switch.cooler",
            "switch.light1", "switch.light2", "sensor.temperature",
            "sensor.humidity", "sensor.dust", "sensor.fire"
    };

    String[] topics = {
            "/home/door/state", "/home/window/state", "/ESP_Easy/GPIO/15",
            "/ESP_Easy/GPIO/14", "/ESP_Easy/GPIO/4", "/ESP_Easy/GPIO/5"
    };
    volatile static String[] devStates = {"off", "off", "off", "off", "off", "off"};
    volatile static String[] sensor = {"0", "0", "0", "0"};

    private static int currIndex = 0;

    JSONObject jsonObject = new JSONObject();
    ProgressDialog pDialog;
    TextView edit_receive;
    String line;
    static int testCount = 0;

    Button btn_0;
    Button btn_1;
    Button btn_2;
    Button btn_3;
    Button btn_4;
    Button btn_5;
    Button[] buttons = new Button[]{};

    TextView label_0, label_1, label_2, label_3, label_4, label_5, label_6;
    TextView[] labels = new TextView[]{};

    public int myFind(String item){
        int i;
        for (i = 0; i < 10; i ++)
        {
            if (item.equals(devices[i]))
                return i;
        }
        return -1;
    }

    private void operate(int i){
        JSONObject obj = new JSONObject();
        try{
            if (devStates[i].equals("off"))
                obj.put("payload", "on");
            else
                obj.put("payload", "off");
            obj.put("topic", topics[i]);
            obj.put("retain", "True");
        }catch (JSONException e){
            Toast.makeText(HttpActivity.this, i, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        postData(myPostUrl[i], obj);
        getDataTest(myGetUrl[i]);
        labels[i].setText(devStates[i]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceStage) {
        super.onCreate(savedInstanceStage);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_http);
        Button btn_get;
        Button btn_post;
        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        buttons = new Button[]{
                btn_0, btn_1, btn_2, btn_3, btn_4, btn_5
        };
        label_0 = findViewById(R.id.textView0);
        label_1 = findViewById(R.id.textView1);
        label_2 = findViewById(R.id.textView2);
        label_3 = findViewById(R.id.textView3);
        label_4 = findViewById(R.id.textView4);
        label_5 = findViewById(R.id.textView5);
        label_6 = findViewById(R.id.textView6);
        labels = new TextView[]{
                label_0, label_1, label_2, label_3, label_4, label_5, label_6
        };
        btn_get = findViewById(R.id.btn_get_test);
        btn_post = findViewById(R.id.btn_post_test);
        edit_receive = findViewById(R.id.edit_receive);


        btn_get.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                for (int i = 2; i < 10; i ++){

                    getDataTest(myGetUrl[i]);
                    Log.d(TAG, "ggggggggggggggggggggggg");
                }

                for (int i = 0; i < 6; i ++){
                    labels[i].setText(devStates[i]);
                    Log.d(TAG, "LLLLLLLLLLLLLLLLLLLLLLL");
                }

                /*
                String tmp = "Temp: "+sensor[0]+"  Humid: "+sensor[1]+"  Dust: "+sensor[2]
                        +"  Fire: "+sensor[3];
                label_6.setText(tmp);
                */
                Log.d(TAG, "66666666666666666666");
            }
        });

        buttons[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                JSONObject obj = new JSONObject();
                try{
                    if (devStates[0].equals("off"))
                        obj.put("payload", "on");
                    else
                        obj.put("payload", "off");
                    obj.put("topic", "/home/door/state");
                    obj.put("retain", "True");
                }catch (JSONException e){
                    Toast.makeText(HttpActivity.this, "errBTN0", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                postData(myPostUrl[0], obj);
                if (devStates[0].equals("off"))
                    devStates[0] = "on";
                else
                    devStates[0] = "off";
                labels[0].setText(devStates[0]);

                //operate(0);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //getData(myGetUrl[1], 1);
                currIndex = 1;

                JSONObject obj = new JSONObject();
                try{
                    if (devStates[1].equals("off"))
                        obj.put("payload", "on");
                    else
                        obj.put("payload", "off");
                    obj.put("topic", "/home/window/state");
                    obj.put("retain", "True");
                }catch (JSONException e){
                    Toast.makeText(HttpActivity.this, "errBTN1", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                postData(myPostUrl[1], obj);
                if (devStates[1].equals("off"))
                    devStates[1] = "on";
                else
                    devStates[1] = "off";
                labels[1].setText(devStates[1]);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currIndex = 2;
                String tmp = labels[2].getText().toString();
                JSONObject obj = new JSONObject();
                try{
                    if (tmp.equals("off"))
                        obj.put("payload", "0");
                    else
                        obj.put("payload", "1");
                    obj.put("topic", "/ESP_Easy/GPIO/15");
                    obj.put("retain", "True");
                }catch (JSONException e){
                    Toast.makeText(HttpActivity.this, "errBTN2", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                postData(myPostUrl[2], obj);
                try{
                    Thread.currentThread().sleep(500);

                }catch(Exception e){

                }
                getDataTest(myGetUrl[2]);
                labels[2].setText(devStates[2]);
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currIndex = 3;
                JSONObject obj = new JSONObject();
                try{
                    if (devStates[3].equals("off"))
                        obj.put("payload", "0");
                    else
                        obj.put("payload", "1");
                    obj.put("topic", "/ESP_Easy/GPIO/14");
                    obj.put("retain", "True");
                }catch (JSONException e){
                    Toast.makeText(HttpActivity.this, "errBTN3", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                postData(myPostUrl[3], obj);
                getDataTest(myGetUrl[3]);
                labels[3].setText(devStates[3]);
            }
        });

        buttons[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currIndex = 4;
                JSONObject obj = new JSONObject();
                try{
                    if (devStates[4].equals("off"))
                        obj.put("payload", "0");
                    else
                        obj.put("payload", "1");
                    obj.put("topic", "/ESP_Easy/GPIO/4");
                    obj.put("retain", "True");
                }catch (JSONException e){
                    Toast.makeText(HttpActivity.this, "errBTN4", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                postData(myPostUrl[4], obj);
                getDataTest(myGetUrl[4]);
                labels[4].setText(devStates[4]);
            }
        });

        buttons[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currIndex = 5;
                JSONObject obj = new JSONObject();
                try{
                    if (devStates[5].equals("off"))
                        obj.put("payload", "0");
                    else
                        obj.put("payload", "1");
                    obj.put("topic", "/ESP_Easy/GPIO/5");
                    obj.put("retain", "True");
                }catch (JSONException e){
                    Toast.makeText(HttpActivity.this, "errBTN5", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                postData(myPostUrl[5], obj);
                getDataTest(myGetUrl[5]);
                labels[5].setText(devStates[5]);
            }
        });
//*/

        btn_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                postData(myPostUrl[0], jsonObject);
            }
        });

    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            labels[6].setText(line);

        }
    };


    public void getDataTest(String myUrl) {
        AsyncHttpClient client = new AsyncHttpClient();
        //client.addHeader("x-ha-access", "password");

        client.addHeader("content-type", "application/json");
        client.get(myUrl, new AsyncHttpResponseHandler() {


            @Override
            public void onStart() {
                Log.d(TAG, "Get Start 6666666666666");
                pDialog = new ProgressDialog(HttpActivity.this);
                pDialog.setMessage("GetLoading...");
                //pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //line = new String(responseBody);
                //Message msg = handler.obtainMessage();
                //msg.obj = line;
                //handler.sendMessage(msg);
                String tmp = new String(responseBody);

                try{
                    JSONObject json = new JSONObject(tmp);

                    String stateString = json.getString("state");
                    String deviceString = json.getString("entity_id");
                    int tmpIndex = myFind(deviceString);
                    Toast.makeText(HttpActivity.this, stateString, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Getting ZZZZZZZZZZ"+deviceString+stateString);

                    if (tmpIndex < 6){

                        devStates[tmpIndex] = stateString;
                        labels[tmpIndex].setText(devStates[tmpIndex]);
                        Log.d(TAG, "Getting YYYYYYYYYYYYYY");
                    }
                    else
                    {
                        sensor[tmpIndex-6] = stateString;
                        line = "Temp: "+sensor[0]+"  Humid: "+sensor[1]+"  Dust: "+sensor[2]
                                +"  Fire: "+sensor[3];
                        Message msg = handler.obtainMessage();
                        msg.obj = line;
                        handler.sendMessage(msg);
                    }


                }catch (JSONException e){
                    System.out.println(7777777);
                }

                Log.d(TAG, "Get End 7777777777777");
                //Toast.makeText(HttpActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(HttpActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

        });

    }


    public void getData(String myUrl) {
        AsyncHttpClient client = new AsyncHttpClient();
        //client.addHeader("x-ha-access", "password");
        client.addHeader("content-type", "application/json");

        client.get(myUrl, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                pDialog = new ProgressDialog(HttpActivity.this);
                pDialog.setMessage("LoadingGet");
                pDialog.show();
                //changeBtn(1,1);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //line = new String(responseBody);
                //Message msg = handler.obtainMessage();
                //msg.obj = line;
                //handler.sendMessage(msg);

                try{
                    JSONObject json = responseBody;
                    String stateString = json.getString("state");
                    if (currIndex < 6)
                        devStates[currIndex] = stateString;
                    else
                        sensor[currIndex-6] = stateString;
                    //labels[2].setText(devStates[2]);
                }catch (JSONException e){
                    Toast.makeText(HttpActivity.this, "GetJsonErr", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    //btn_0.setText("666");
                }
                //Toast.makeText(HttpActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject e){
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(HttpActivity.this, "GetErr", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

        });
    }
//*/
    public void postData(String myUrl, JSONObject jsonParams) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //client.addHeader("x-ha-access", "password");
        client.addHeader("content-type", "application/json");
        //JSONObject jsonParams = new JSONObject();
        StringEntity se = null;
        //StringEntity se = new StringEntity();
        try{
            //jsonParams.put("name", "abc");
            se = new StringEntity(jsonParams.toString());
        }catch (Exception e){
            Log.i(TAG, "66666666666666666666");
        }

        params.put("data", "Android");
        client.post(HttpActivity.this, myUrl, se, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.d(TAG, "Post Start 88888888888888");
                pDialog = new ProgressDialog(HttpActivity.this);
                pDialog.setMessage("LoadingPost:"+currIndex);
                //pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Log.d(TAG, "Post End 99999999999999999");
                //Toast.makeText(HttpActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(HttpActivity.this, "PostErr", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }


        });
    }
}

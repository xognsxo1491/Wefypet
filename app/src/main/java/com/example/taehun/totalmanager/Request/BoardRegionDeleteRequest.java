package com.example.taehun.totalmanager.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BoardRegionDeleteRequest extends StringRequest {

    final static private String URL = "http://xognsxo1491.cafe24.com/Board_Region_Delete_connect.php";
    private Map<String, String> parameters;

    public BoardRegionDeleteRequest(String userId, String number, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("Id",userId);
        parameters.put("Number", number);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

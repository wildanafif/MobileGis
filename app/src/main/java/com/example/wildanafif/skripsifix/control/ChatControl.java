package com.example.wildanafif.skripsifix.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wildanafif.skripsifix.entitas.ChatMessage;

import java.util.HashMap;
import java.util.Map;

import static com.example.wildanafif.skripsifix.entitas.request.Url_request._SEND_MESSAGE_;

/**
 * Created by wildan afif on 7/16/2017.
 */

public class ChatControl {
    private Context context;

    public ChatControl(Context context) {
        this.context = context;
    }

    public void sendMessage(final String nama, final String id_ketemuan, final String email_receiver, final String message){
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = _SEND_MESSAGE_;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("id_ketemuan", id_ketemuan);
                params.put("email_receiver", email_receiver);
                params.put("pesan", message);

                return params;
            }
        };
        queue.add(postRequest);
    }
}

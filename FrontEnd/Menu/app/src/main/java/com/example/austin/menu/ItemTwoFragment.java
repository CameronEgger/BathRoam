/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.example.austin.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import 	android.widget.Button;
import 	android.widget.TextView;
import 	android.app.Activity;
import android.support.v4.app.FragmentActivity;
import 	android.widget.EditText;
import android.widget.Spinner;
import android.location.Location;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.android.volley.VolleyError;
import com.android.volley.*;
import com.android.volley.toolbox.*;






public class ItemTwoFragment extends Fragment {
    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }
    EditText text, text1;
    TextView location;
    Spinner spin, spin1;
    Location locate;
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // this is all dummy data lolzz
        String url ="https://warm-thicket-66081.herokuapp.com/create/?latitude=10&longitude=10&height=10&title=testfromclient&comment=pls&public=True&gender=m&created_by=1";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                       System.out.println("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("adsfadf");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        // ((MapsActivity)getActivity()).setContentView(R.layout.activity_maps);

        //Button submit = (Button)getActivity().findViewById(R.id.button);
      /*  Button submit = ((MapsActivity)getActivity()).getBut();
        text   = (EditText)getActivity().findViewById(R.id.editText1);
        text1   = (EditText)getActivity().findViewById(R.id.editText2);
        spin = (Spinner)getActivity().findViewById(R.id.spinner1);
        spin1 = (Spinner)getActivity().findViewById(R.id.spinner2);
        //locate = (MapsActivity)getActivity().getLocation();
        location = (TextView)getActivity().findViewById(R.id.textDummy);
      //  try {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nsfdjkgjkhgkjsdhgkshgksfhglkjdhgjsfdhgkfshgl\n\n\n\n\n\n\n\n\n\n");
            submit.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n+"+text.getText()+" "+text1.getText()+" "+spin.getSelectedItem().toString()+"\n\n\n\n\n\n\n\n\n\n");

                            if (text.getText() != null && text1.getText() != null && spin.getSelectedItem().toString() != null && spin.getSelectedItem().toString() != null) {
                                try {
                                    System.out.println("1");

                                    String USER_AGENT = "Mozilla/5.0";
                                        String POST_URL2 = "https://warm-thicket-66081.herokuapp.com/create/";
                                        String POST_PARAMS2 = "latitude=10&longitude=10&radius=1000";

                                    System.out.println("2");

                                        URL obj = new URL(POST_URL2);
                                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                        con.setRequestMethod("POST");
                                        con.setRequestProperty("User-Agent", USER_AGENT);

                                        con.setDoOutput(true);
                                        OutputStream os = con.getOutputStream();
                                        os.write(POST_PARAMS2.getBytes());
                                        os.flush();
                                        os.close();
                                    System.out.println("4");

                                        int responseCode = con.getResponseCode();
                                        System.out.println("POST Response Code :: " + responseCode);

                                        if (responseCode == HttpURLConnection.HTTP_OK) { //success
                                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                                    con.getInputStream()));
                                            String inputLine;
                                        StringBuffer response = new StringBuffer();

                                        while ((inputLine = in.readLine()) != null) {
                                            response.append(inputLine);
                                        }
                                        in.close();

                                        System.out.println(response.toString());
                                    } else {
                                        System.out.println("POST request not worked");
                                    }
                                    System.out.println("9");

                                } catch (Exception e) {System.out.println(e.toString());}
                            }

                        }
                    });
      //  }catch(NullPointerException e){}*/
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_two, container, false);
    }
}

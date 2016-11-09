package com.example.gaurav.card;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gaurav on 13-10-2016.
 */
public class EnterInfo extends Fragment {

    Button next;
    EditText name,password,cnpassword,email;
    public RadioGroup radioSexGroup;
    public RadioButton male,female;
    private static final String REGISTER_URL = Constants.ip+"/user/";
    public String gender;
    public static final String KEY_Name = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL="email_id";
    public static final String KEY_GENDER="gender";

    public EnterInfo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_enter_info, container, false);

        name=(EditText) view.findViewById(R.id.name);
        password=(EditText) view.findViewById(R.id.password);
        cnpassword=(EditText) view.findViewById(R.id.cnpassword);
        email=(EditText) view.findViewById(R.id.email);
        next=(Button) view.findViewById(R.id.nextbutton);
        radioSexGroup=(RadioGroup) view.findViewById(R.id.radioSex);
        radioSexGroup.clearCheck();

        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    // Toast.makeText(getContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                    gender = (String) rb.getText();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
                Profilepic fragment1 = new Profilepic();
                Bundle args = new Bundle();
                args.putString("email", email.getText().toString());
                args.putString("password",password.getText().toString());
                fragment1.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, fragment1);
                fragmentTransaction.commit();

            }
        });

                return view;

    }

    private void registerUser() {
        final String name2 = name.getText().toString().trim();
        final String password2 = password.getText().toString().trim();
        final String cnfpass = cnpassword.getText().toString().trim();
        final String email_id = email.getText().toString().trim();

        if (cnfpass.equals(password2)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        //sdd
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_Name, name2);
                    params.put(KEY_PASSWORD, password2);
                    params.put(KEY_GENDER, gender);
                    params.put(KEY_EMAIL, email_id);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    Toast.makeText(getContext(),"male",Toast.LENGTH_SHORT).show();
                    // Pirates are the best
                    break;
            case R.id.radioFemale:
                if (checked)
                    Toast.makeText(getContext(),"female",Toast.LENGTH_SHORT).show();
                    // Ninjas rule
                    break;
        }
    }


}

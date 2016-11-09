package com.example.gaurav.card;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SessionManager session;
    //Constants ip;
    public String url = Constants.ip + "/user/";
    RequestQueue requestQueue;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public EditText email, password;
    public Button login, signup;
    public String pass, id;
    public String Fbemail = null;
    public String Fbfirstname = null, Fblastname = null, Fbgender = null;
    private static final String REGISTER_URL = Constants.ip + "/user/";
    // public String gender;
    public static final String KEY_Name = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email_id";
    public static final String KEY_GENDER = "gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        requestQueue = Volley.newRequestQueue(this);
        login = (Button) findViewById(R.id.signin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pa);
        session = new SessionManager(getApplicationContext());
        loginButton = (LoginButton) findViewById(R.id.login_button);
        // Casts results into the TextView found within the main layout XML with id jsonData
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String value = editText.getText().toString();
                id = email.getText().toString();
                pass = password.getText().toString();
                url = url + id + "/?format=json";
                if (id == null || pass == null)
                    Toast.makeText(getApplicationContext(), "Please fill the details", Toast.LENGTH_SHORT).show();
                checklogin(id, pass);

            }
        });

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admin = new Intent(getApplicationContext(), SignUp.class);
                startActivity(admin);
            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        setFacebookData(loginResult);
                        registerUser();
                        session.createLoginSession(null, Fbemail);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }


        /*loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                session.createLoginSession("gaurav123","gaurav23dec@gmail.com");
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });*/
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

public void checklogin(final String ids, final String passw)
{
Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();
    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
            url, null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Log.d("TAG", response.toString());

            try {
                String b = response.getString("password");
                String a = response.getString("email_id");
                Toast.makeText(getApplicationContext(),a+"--"+b,Toast.LENGTH_SHORT).show();
                if (a == null) {
                    Toast.makeText(getApplicationContext(), "Invalid Email Id", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(),"pass"+passw,Toast.LENGTH_SHORT).show();

                if (b.equals(passw)) {
                    session.createLoginSession(b, a);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Password  ", Toast.LENGTH_SHORT).show();
                }
               // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.d("TAG", "Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_SHORT).show();
                  }
    });


    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(jsonObjReq);

    email.setText("");
    password.setText("");
    id="";
    pass="";
    url =Constants.ip+ "/user/";


}
    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response", response.toString());

                            Fbemail= response.getJSONObject().getString("email");
                            Fbfirstname = response.getJSONObject().getString("first_name");
                            Fblastname  = response.getJSONObject().getString("last_name");
                            Fbgender = response.getJSONObject().getString("gender");
                            String bday = response.getJSONObject().getString("birthday");

                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link", link);
                            if (Profile.getCurrentProfile() != null) {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }

                            Log.i("Login" + "Email", Fbemail);
                            Log.i("Login" + "FirstName",Fbfirstname);
                            Log.i("Login" + "LastName", Fblastname);
                            Log.i("Login" + "Gender", Fbgender);
                            Log.i("Login" + "Bday", bday);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public String getid()
    {
        return id;
    }

    public String getpass()
    {
        return pass;
    }
    private void registerUser() {
      //  final String name2 = name.getText().toString().trim();
       // final String password2 = password.getText().toString().trim();
        //final String cnfpass = cnpassword.getText().toString().trim();
        //final String email_id = email.getText().toString().trim();



            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        //sdd
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_Name, Fbfirstname);
                    params.put(KEY_PASSWORD, "fblogin");
                    params.put(KEY_GENDER, Fbgender);
                    params.put(KEY_EMAIL, Fbemail);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

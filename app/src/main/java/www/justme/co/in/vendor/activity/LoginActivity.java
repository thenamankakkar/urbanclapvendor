package www.justme.co.in.vendor.activity;

import static www.justme.co.in.vendor.utils.SessionManager.currency;
import static www.justme.co.in.vendor.utils.SessionManager.login;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.partner.R;
import www.justme.co.in.vendor.model.ContryCode;
import www.justme.co.in.vendor.model.LoginUser;
import www.justme.co.in.vendor.model.ResponseMessge;
import www.justme.co.in.vendor.model.User;
import www.justme.co.in.vendor.retrofit.APIClient;
import www.justme.co.in.vendor.retrofit.GetResult;
import www.justme.co.in.vendor.utils.CustPrograssbar;
import www.justme.co.in.vendor.utils.SessionManager;
import www.justme.co.in.vendor.utils.Utility;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.txt_slogin)
    TextView txtSlogin;
    @BindView(R.id.txt_sregister)
    TextView txtSregister;
    @BindView(R.id.lvl_login)
    LinearLayout lvlLogin;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_passsword)
    EditText edPasssword;
    @BindView(R.id.txt_forgotpassword)
    TextView txtForgotpassword;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.lvl_register)
    LinearLayout lvlRegister;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_emailnew)
    EditText edEmailnew;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.ed_passswordnew)
    EditText edPassswordnew;

    @BindView(R.id.ed_address)
    EditText edAddress;

    @BindView(R.id.txt_register)
    TextView txtRegister;
    @BindView(R.id.at_code)
    AutoCompleteTextView atCode;

    @BindView(R.id.at_city)
    AutoCompleteTextView atCity;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);

        getCodelist();

        atCity.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                // on focus off
                String str = atCity.getText().toString();
                ListAdapter listAdapter = atCity.getAdapter();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    String temp = listAdapter.getItem(i).toString();
                    if (str.compareTo(temp) == 0) {
                        return;
                    }
                }
                atCity.setText("");

            }
        });

        atCode.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                // on focus off
                String str = atCode.getText().toString();

                ListAdapter listAdapter = atCode.getAdapter();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    String temp = listAdapter.getItem(i).toString();
                    if (str.compareTo(temp) == 0) {
                        return;
                    }
                }
                atCode.setText("");

            }
        });
    }

    private void getCodelist() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    private void loginUser() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edEmail.getText().toString());
            jsonObject.put("password", edPasssword.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().loginUser(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void checkUser() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edMobile.getText().toString());
            jsonObject.put("ccode", atCode.getText().toString());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getMobileCheck(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
            custPrograssbar.prograssCreate(LoginActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.txt_slogin, R.id.txt_sregister, R.id.txt_register, R.id.txt_login, R.id.txt_forgotpassword})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_slogin:
                txtSlogin.setBackground(getResources().getDrawable(R.drawable.tab1));
                txtSlogin.setTextColor(getResources().getColor(R.color.white));
                txtSregister.setBackground(getResources().getDrawable(R.drawable.tab2));
                txtSregister.setTextColor(getResources().getColor(R.color.black));
                lvlLogin.setVisibility(View.VISIBLE);
                lvlRegister.setVisibility(View.GONE);

                break;

            case R.id.txt_sregister:
                txtSlogin.setBackground(getResources().getDrawable(R.drawable.tab2));
                txtSlogin.setTextColor(getResources().getColor(R.color.black));
                txtSregister.setBackground(getResources().getDrawable(R.drawable.tab1));
                txtSregister.setTextColor(getResources().getColor(R.color.white));
                lvlLogin.setVisibility(View.GONE);
                lvlRegister.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_register:
                if (validationCreate()) {
                    checkUser();

                }

                break;
            case R.id.txt_login:
                if (validation()) {
                    loginUser();
                }
                break;

            case R.id.txt_forgotpassword:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                LoginUser loginUser = gson.fromJson(result.toString(), LoginUser.class);
                Toast.makeText(LoginActivity.this, loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    User user = loginUser.getUser();
                    OneSignal.deleteTag("Categoryid");
                    OneSignal.sendTag("PartnerId", user.getId());
                    user.setCategory(loginUser.getCategory());
                    sessionManager.setUserDetails("", user);
                    sessionManager.setStringData(currency, loginUser.getCurrency());
                    sessionManager.setBooleanData(login, true);
                    if (loginUser.getUser().getCategoryid().equalsIgnoreCase("0")) {
                        startActivity(new Intent(LoginActivity.this, CategoryActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    } else {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                ContryCode contryCode = gson.fromJson(result.toString(), ContryCode.class);
                ArrayList<String> countries = new ArrayList<>();
                ArrayList<String> city = new ArrayList<>();
                if (contryCode.getResult().equalsIgnoreCase("true")) {
                    countries = new ArrayList<>();
                    for (int i = 0; i < contryCode.getCountryCode().size(); i++) {
                        countries.add(contryCode.getCountryCode().get(i).getCcode());

                    }
                    for (int i = 0; i < contryCode.getCityList().size(); i++) {
                        city.add(contryCode.getCityList().get(i).getCname());
                    }
                }
                ArrayAdapter<String> citylist = new ArrayAdapter<>
                        (this, android.R.layout.simple_list_item_1, city);

                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (this, android.R.layout.simple_list_item_1, countries);
                atCode.setAdapter(adapter);
                atCode.setThreshold(1);

                atCity.setAdapter(citylist);
                atCity.setThreshold(1);
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                ResponseMessge response = gson.fromJson(result.toString(), ResponseMessge.class);
                if (response.getResult().equals("true")) {
                    Utility.isvarification = 1;
                    User user = new User();
                    user.setName(edName.getText().toString());
                    user.setEmail(edEmailnew.getText().toString());
                    user.setCcode(atCode.getText().toString());
                    user.setMobile(edMobile.getText().toString());
                    user.setPassword(edPassswordnew.getText().toString());
                    user.setCity(atCity.getText().toString());
                    user.setAddress(edAddress.getText().toString());
                    user.setPassword(edPassswordnew.getText().toString());
                    sessionManager.setUserDetails("", user);
                    startActivity(new Intent(this, VerifyPhoneActivity.class).putExtra("code", atCode.getText().toString()).putExtra("phone", edMobile.getText().toString()));
                } else {
                    Toast.makeText(LoginActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());

        }
    }

    public boolean validation() {
        if (TextUtils.isEmpty(edEmail.getText().toString())) {
            edEmail.setError("Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(edPasssword.getText().toString())) {
            edPasssword.setError("Enter Password");
            return false;
        }
        return true;
    }

    public boolean validationCreate() {
        if (TextUtils.isEmpty(edName.getText().toString())) {
            edName.setError("Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(edEmailnew.getText().toString())) {
            edEmailnew.setError("Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(atCode.getText().toString())) {
            atCode.setError("Enter Code");
            return false;
        }

        if (TextUtils.isEmpty(edMobile.getText().toString())) {
            edMobile.setError("Enter Mobile");
            return false;
        }
        if (TextUtils.isEmpty(edPassswordnew.getText().toString())) {
            edPassswordnew.setError("Enter Password");
            return false;
        }
        if (TextUtils.isEmpty(edAddress.getText().toString())) {
            edAddress.setError("Enter Address");
            return false;
        }
        if (TextUtils.isEmpty(atCity.getText().toString())) {
            atCity.setError("Enter City");
            return false;
        }
        if (!TextUtils.isEmpty(atCity.getText().toString())) {
            String str = atCity.getText().toString();

            ListAdapter listAdapter = atCity.getAdapter();
            for (int i = 0; i < listAdapter.getCount(); i++) {
                String temp = listAdapter.getItem(i).toString();
                if (str.compareTo(temp) == 0) {
                    return true;
                }
            }

            atCity.setText("");
            atCity.setError("Enter City");

            return false;
        }

        return true;
    }
}
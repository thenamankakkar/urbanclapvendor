package www.justme.co.in.vendor.fragment;

import static www.justme.co.in.vendor.utils.SessionManager.currency;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.partner.R;
import www.justme.co.in.vendor.activity.AddCreditActivity;
import www.justme.co.in.vendor.adepter.TrazectionAdapter;
import www.justme.co.in.vendor.model.CreditTrasection;
import www.justme.co.in.vendor.model.User;
import www.justme.co.in.vendor.retrofit.APIClient;
import www.justme.co.in.vendor.retrofit.GetResult;
import www.justme.co.in.vendor.utils.CustPrograssbar;
import www.justme.co.in.vendor.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class EarningFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.txt_wallet)
    TextView txtWallet;
    @BindView(R.id.txt_income)
    TextView txtIncome;
    @BindView(R.id.txt_expence)
    TextView txtExpence;
    @BindView(R.id.recycle_trazection)
    RecyclerView recycleTrazection;

    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earnig, container, false);
        ButterKnife.bind(this, view);

        recycleTrazection.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleTrazection.setItemAnimator(new DefaultItemAnimator());


        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        getCredit();
        return view;
    }

    private void getCredit() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", user.getId());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getCredit(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                CreditTrasection wallet = gson.fromJson(result.toString(), CreditTrasection.class);
                if (wallet.getResult().equalsIgnoreCase("true")) {
                    txtWallet.setText(sessionManager.getStringData(currency) + wallet.getCreditval());
                    txtExpence.setText(sessionManager.getStringData(currency) + wallet.getPartnerdebittotal());
                    txtIncome.setText(sessionManager.getStringData(currency) + wallet.getCreditval());
                    TrazectionAdapter adapter = new TrazectionAdapter(getActivity(), wallet.getCreditItem());
                    recycleTrazection.setAdapter(adapter);
                }
            }

        } catch (Exception e) {
            Log.e("Error","-->"+e.toString());

        }
    }

    @OnClick({R.id.flb1})
    public void onClick(View view) {
        if (view.getId() == R.id.flb1) {
            startActivity(new Intent(getActivity(), AddCreditActivity.class));
        }
    }
}
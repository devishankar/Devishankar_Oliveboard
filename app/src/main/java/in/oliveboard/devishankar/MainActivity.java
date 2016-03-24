package in.oliveboard.devishankar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.oliveboard.devishankar.adapters.ExamsPagerAdapter;
import in.oliveboard.devishankar.fragments.ExamFragment;
import in.oliveboard.devishankar.handlers.response.ApiResponseHandler;
import in.oliveboard.devishankar.listeners.IHttpResponseListener;
import in.oliveboard.devishankar.utils.AppHttpClient;
import in.oliveboard.devishankar.views.SlidingTabLayout;

public class MainActivity extends AppCompatActivity implements IHttpResponseListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL = "http://android.oliveboard.in/hiring/mocks.cgi";
    private static ProgressDialog mDialog;
    private boolean mStateRefreshed = false;
    private Context mContext;
    private ExamsPagerAdapter adapter;
    private ViewPager pagerExams;
    private SlidingTabLayout tabs;
    private JSONObject body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(mContext, android.R.color.white);
            }
        });

        pagerExams = (ViewPager) findViewById(R.id.pagerExams);
        loadFromRemote();
        adapter = new ExamsPagerAdapter(getSupportFragmentManager());
        pagerExams.setAdapter(adapter);
        pagerExams.setOffscreenPageLimit(1);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mStateRefreshed = true;
            loadFromRemote();
            return true;
        } else if (id == R.id.action_add) {
            try {
                processData(body);
                Toast.makeText(mContext, "For demo purpose, to showcase growing adapter dynamically", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFromRemote() {
        showLoadingDialog();
        AppHttpClient.get(URL, new ApiResponseHandler(mContext));
    }

    private void processData(JSONObject body) throws JSONException {
        JSONArray jArr = body.optJSONArray("exams");
        if (jArr != null) {

            for (int i = 0; i < jArr.length(); i++) {
                JSONArray innerArr = jArr.optJSONArray(i);
                String title = "", content = "";
                for (int j = 0; j < innerArr.length(); j++) {
                    if (j == 0) {
                        title = innerArr.getString(j);
                    } else if (j == 1) {
                        content = innerArr.getString(j);
                    }
                }
                if (!title.equals("") && !content.equals(""))
                    adapter.addFragment(ExamFragment.newInstance(content), title);
            }
            adapter.notifyDataSetChanged();
            tabs.setViewPager(pagerExams);
        }
    }

    @Override
    public void onSuccess(JSONObject body) throws JSONException {
        dismissDialog();
        if (body != null) {
            this.body = body;
            if (mStateRefreshed) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                mStateRefreshed = false;
            }
            processData(body);
        }
    }

    @Override
    public void onMessage(String resp) {
        dismissDialog();
        Toast.makeText(mContext, resp, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String resp, Throwable throwable) {
        dismissDialog();
        Toast.makeText(mContext, resp, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onJsonParseError() {
        dismissDialog();
        Toast.makeText(mContext, "Something went wrong, please try again after sometime.", Toast.LENGTH_SHORT).show();
    }


    private void showLoadingDialog() {
        try {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Please wait...");
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissDialog() {
        mDialog.dismiss();
    }
}

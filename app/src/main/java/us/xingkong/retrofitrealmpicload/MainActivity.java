package us.xingkong.retrofitrealmpicload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import us.xingkong.retrofitrealmpicload.bean.Items;
import us.xingkong.retrofitrealmpicload.presenter.AppInfoAdapter;
import us.xingkong.retrofitrealmpicload.presenter.RetrofitLoadAppInfo;

import static us.xingkong.retrofitrealmpicload.bean.AppInfo.appInfo;

public class MainActivity extends AppCompatActivity{

    TextView textView;
    RecyclerView recyclerView;
    EditText editText;
    AppInfoAdapter appInfoAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    Context context;

    Button query;
    Button upload;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                appInfoAdapter.notifyDataSetChanged();
//                Log.i("adapterChanged---", "true");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        new RetrofitLoadAppInfo(MainActivity.this).call();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        appInfoAdapter = new AppInfoAdapter(MainActivity.this);
//        Log.i("ViewHolderAlready---", "true");
        recyclerView.setAdapter(appInfoAdapter);

        context = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isShow = false;
                while (!isShow) {
                    if (appInfo != null) {
//                        showAppInfo();
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
//                        AppInfoAdapter.itemCount = appInfo.getApps_count();
//                        new GlideLoadPic(context, appIcon, appInfo.getItems().get(1).getIcon_url(), 0);
                        isShow = true;
                    }
                }
            }
        }).start();
        //上拉刷新（有动画）
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RetrofitLoadAppInfo(MainActivity.this).call();
                appInfoAdapter.notifyDataSetChanged();
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);//defult=6000
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                System.out.println(scrollState);//停止滚动==0，开始滚动时==1，滚动中==2
                //下拉加载
                if(recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                        >= recyclerView.computeVerticalScrollRange() && scrollState == 0)
                {

                }
            }

        });
    }

    private void initView() {

        textView = (TextView) findViewById(R.id.textView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        editText = (EditText) findViewById(R.id.edit_query);

        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadData();
            }
        });

        query = (Button) findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                queryAppInfoById(s);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
    }

    public Items queryAppInfoById(String appName) {
        Realm  mRealm=Realm.getDefaultInstance();
        Items items = mRealm.where(Items.class).equalTo("name", appName).findFirst();

        try {
            String itemsId = items.getId();
            Log.i("items---", itemsId);
            Toast.makeText(MainActivity.this, "Query is successful! ItemsId = "+itemsId, Toast.LENGTH_SHORT).show();
            Log.i("Query---", "success");
        } catch (NullPointerException e) {
            Toast.makeText(this, "Query is failed!Please check your message!", Toast.LENGTH_SHORT).show();
        }
        return items;
    }

//    public static List<AppInfo> queryAllAppInfo() {
//        Realm  mRealm=Realm.getDefaultInstance();
//        RealmResults<AppInfo> appInfo = mRealm.where(AppInfo.class).findAll();
//        return mRealm.copyFromRealm(appInfo);
//    }

    public void upLoadData() {
        Realm m_realm=Realm.getDefaultInstance();
        // Copy the object to Realm. Any further changes must happen on realmUser
        m_realm.beginTransaction();
        try {
            m_realm.deleteAll();
            m_realm.copyToRealm(appInfo);
            Toast.makeText(MainActivity.this, "UpLoad Data to Realm is successful!", Toast.LENGTH_SHORT).show();
            m_realm.commitTransaction();
            Log.i("UpLoad---", "success");
        } catch (RealmPrimaryKeyConstraintException | IllegalArgumentException e) {
            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        m_realm.close();
    }
}


package com.commonframe.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.commonframe.App;
import com.commonframe.R;
import com.commonframe.base.MVPBaseActivity;
import com.commonframe.mvp.entity.PostQueryInfo;
import com.commonframe.mvp.presenter.PostPresenter;
import com.commonframe.mvp.ui.adapter.LogisticsAdapter;
import com.commonframe.mvp.view.MainView;
import com.netlibrary.listener.OnResultListener;
import com.netlibrary.NetClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MVPBaseActivity implements View.OnClickListener, MainView{
    private EditText post_name_et;
    private EditText post_id_et;
    private ListView post_list_lv;
    private Button post_search_bn;
    private PostPresenter postPresenter;
    private List<PostQueryInfo.DataBean> dataArray = new ArrayList<>();
    private LogisticsAdapter logisticsAdapter;

    @Override
    protected void initVariables(Bundle savedInstanceState){
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews(Bundle savedInstanceState){
        
        post_name_et = (EditText) findViewById(R.id.post_name_et);
        post_id_et = (EditText) findViewById(R.id.post_id_et);
        
        post_list_lv = (ListView) findViewById(R.id.post_list_lv);
        logisticsAdapter = new LogisticsAdapter(getApplicationContext(),dataArray);
        postPresenter = new PostPresenter(this);
        post_list_lv.setAdapter(logisticsAdapter);
        
        post_search_bn = (Button) findViewById(R.id.post_search_bn);
        post_search_bn.setOnClickListener(this);

        
    }

    @Override
    protected void loadData(){
        new NetClient.Builder(App.getContext())
                .baseUrl("https://test.gongyebangshou.com:8080/")
                .url("usr_verflogin.php")
                .param("phone","13113613681")
                .param("login_type","2")
                .param("verfcode","1234")
                .bodyType(NetClient.STRING,PostQueryInfo.class) //当要得到已经解析完成的实体类时，添加此方法即可
                .build()
                .post(new OnResultListener(){
                    @Override
                    public void onSuccess(String string){
                        super.onSuccess(string);
                    }

                    @Override
                    public void onFailure(String message){
                        super.onFailure(message);
                    }
                });
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.post_search_bn:
                //postPresenter.requestHomeData(post_name_et.getText().toString(),post_id_et.getText().toString());


                new NetClient.Builder(App.getContext())
                        .baseUrl("https://test.gongyebangshou.com:8080/")
                        .url("usr_get_rptrecords.php")
                        .param("eid", "10000524")
                        .param("page", "1")
                        .param("type", "2") //1:企业　2:我的
                        .param("status", "2")
                        .bodyType(NetClient.STRING,PostQueryInfo.class) //当要得到已经解析完成的实体类时，添加此方法即可
                        .build()
                        .get(new OnResultListener(){
                            @Override
                            public void onSuccess(String string){
                                super.onSuccess(string);
                            }

                            @Override
                            public void onFailure(String message){
                                super.onFailure(message);
                            }
                        });
                break;
        }
    }
    
    @Override
    public void updateListUI(PostQueryInfo postQueryInfo) {
        dataArray.clear();
        dataArray.addAll(postQueryInfo.getData());
        logisticsAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void errorToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    
    @Override
    protected void onDestroy(){
        //防止activity销毁,postPresenter对象还存在造成的内存泄漏
        if(postPresenter!=null) postPresenter.detach();
        super.onDestroy();
    }
}

package com.commaai.commalog;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.commaai.commalog.log.Constants;
import com.commaai.commalog.log.Logger;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private TextView logTv;

//    private StringBuilder sb = null;

    private ImageView toTopBtn, toBottomBtn, clearBtn;

    private ScrollView scrollView;

    private Handler handler;

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: ");
        initView();
        initLog();
    }

    private void initView() {

        handler=new Handler();

        logTv = findViewById(R.id.logContent);
//        sb = new StringBuilder();

        toTopBtn = findViewById(R.id.toTopBtn);
        toBottomBtn = findViewById(R.id.toBottomBtn);
        clearBtn = findViewById(R.id.clearBtn);

        scrollView = findViewById(R.id.logScroll);

        searchView=findViewById(R.id.searchView);

        toTopBtn.setOnClickListener(this);
        toBottomBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String matchKeyContent=matcherSearchText(s);
                logTv.setText("");
                logTv.append(Html.fromHtml(matchKeyContent));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    private void initLog() {
        LoggHandler handler = new LoggHandler(this);
        Logger logger = new Logger(handler);
        logger.collectLog();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toTopBtn:
                toTop();
                break;
            case R.id.toBottomBtn:
                toBottom();
                break;
            case R.id.clearBtn:
//                sb.setLength(0);
                logTv.setText("");
                break;
            default:
                break;
        }
    }


    private void toBottom(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void toTop(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    public static class LoggHandler extends Handler {

        private final WeakReference<MainActivity> mActivity;

        private LoggHandler(MainActivity mActivty) {
            this.mActivity = new WeakReference<MainActivity>(mActivty);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constants.LoggerMsg.WHAT_NEXT_LOG:
                        String line = (String) msg.obj;
                        updateLogView(line);
                        break;
                    default:
                        break;
                }
            }
        }

        private void updateLogView(String line) {
//            mActivity.get().logTv.setText("");
//            mActivity.get().sb.append(line + "\n");
//            mActivity.get().logTv.setText(mActivity.get().sb.toString());
            mActivity.get().showLine(line,"black");
        }
    }

    private String matcherSearchText(String keyword){
        String content = logTv.getText().toString();
        //用(?i)来忽略大小写
        String wordReg = "(?i)"+keyword;
        StringBuffer sb = new StringBuffer();
        Matcher matcher = Pattern.compile(wordReg).matcher(content);
        while(matcher.find()){
            //这样保证了原文的大小写没有发生变化
            matcher.appendReplacement(sb, "<font color=\"#ff0014\">"+matcher.group()+"</font>");
        }
        matcher.appendTail(sb);
        content = sb.toString();
        //如果匹配和替换都忽略大小写,则可以用以下方法
        //content = content.replaceAll(wordReg,"<font color=\"#ff0014\">"+keyword+"</font>");
        return content;
    }

    private void showLine(String line, String color) {
        if (line.contains("http://")||line.contains("https://")) {
            String url = line.substring(line.indexOf("http"));
            logTv.append(Html.fromHtml("<font color='" + color + "'>" + line.substring(0, line.indexOf("http")) + "</font>"));
            logTv.append(Html.fromHtml("<a href='" + url + "'>" + url + "</a>"));
        } else {
            logTv.append(Html.fromHtml("<font color='" + color + "'>" + line + "</font>"+"<br />"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

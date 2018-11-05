package android.work.fina.coursetable.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.work.fina.coursetable.service.MainService;
import android.work.fina.coursetable.R;
import android.work.fina.coursetable.adapter.CustomAdapter;
import android.work.fina.coursetable.bean.CourseInfo;
import android.work.fina.coursetable.adapter.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView teacherName = null;
    private Button searchBtn;
    private Spinner weekSpinner;
    private List<String> teacher_list = new ArrayList<String>();
    private List<String> week_list = new ArrayList<String>();
    private AlertDialog alertDialog = null;
    private ListView listView = null;
    List<CourseInfo> list = new ArrayList<CourseInfo>();
    private Handler handler ;
    private String selweek = null;
    private MainService mainService;
    private Boolean flag = false;

    private ServiceConnection conn= new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mainService = ((MainService.MyBinder)iBinder).getService();
            initTnameList();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mainService = null;
        }
    };


    @Override
    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(0xff2E3032);

        teacherName = (AutoCompleteTextView) findViewById(R.id.teacher_name);
        searchBtn = (Button) findViewById(R.id.search_button);
        weekSpinner = (Spinner) findViewById(R.id.term);
        listView = (ListView) findViewById(R.id.showcourse);
        weekSpinner.setBackgroundColor(0x0);

        //绑定service
        Intent intent = new Intent(this,MainService.class);
        bindService(intent, conn, this.BIND_AUTO_CREATE);

        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                //获取选中值
                Spinner spinner = (Spinner) adapterView;
                selweek = (String) spinner.getItemAtPosition(position);
                if(flag){
                    searchBtn.performClick();
                }
                flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //term spinner 下拉事件
        initWeekList();
        ArrayAdapter<String> term_adadpter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, week_list);
        term_adadpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(term_adadpter);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    //nameAdapter.notifyDataSetChanged();
                    final SearchAdapter<String> nameAdapter = new SearchAdapter<String>(MainActivity.this,
                            android.R.layout.simple_list_item_1, teacher_list, SearchAdapter.ALL);
                    teacherName.setAdapter(nameAdapter);
                }
            }
        };

    }

    //老师姓名自动提示
    public void initTnameList() {
        new Thread() {
            @Override
            public void run() {
                teacher_list.clear();
                JSONArray jsa = mainService.getTeacherName();
                try {
                    String tname = "";
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject jso = jsa.getJSONObject(i);
                        tname = jso.getString("name");
                        teacher_list.add(tname);
                    }
                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    //week_list 初始化
    private void initWeekList(){
        int i;
        for(i=1; i<=20; i++){
            String data = "第"+i+"周";
            week_list.add(data);
        }
    }

    public void change_code(View view)
    {
        new Thread(){
            @Override
            public void run() {
                try
                {
                    Bitmap bitmap = BitmapFactory.decodeStream(mainService.searchClick());
                    showValCode(bitmap);
                }catch (Exception e)
                {
                    showMsg(getResources().getString(R.string.code_error));
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //点击查找事件
    public void search_click(View view) {
        new Thread(){
            @Override
            public void run() {
                try
                {
                    JSONObject jsonObject = new JSONObject();
                    String week = selweek.substring(1,selweek.length()-1);
                    String tname = teacherName.getText().toString().trim();

                    if("".equals(tname))
                    {
                        showMsg(getResources().getString(R.string.name_empty));
                        return;
                    }

                    jsonObject = mainService.checkTeacherName(tname);
                    if(!jsonObject.getBoolean("msg")) {
                        showMsg(getResources().getString(R.string.check_name));
                        return;
                    }

                    jsonObject = mainService.getLocalCourseInfo(week,tname);
                    if(jsonObject.length() == 0)
                    {
                        Bitmap bitmap = BitmapFactory.decodeStream(mainService.searchClick());
                        showValCode(bitmap);
                    }
                    else
                    {
                        initCourseInfo(jsonObject.getString("name"));
                    }

                }catch (Exception e)
                {
                    showMsg(getResources().getString(R.string.code_error));
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showValCode(final Bitmap bitmap){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(alertDialog != null && alertDialog.isShowing())
                {
                    alertDialog.getImageView().setImageBitmap(bitmap);
                }
                else
                {
                    alertDialog = new AlertDialog(MainActivity.this,R.style.my_dialog_theme,onClickListener);
                    alertDialog.show();
                    alertDialog.getImageView().setImageBitmap(bitmap);
                }
            }
        });
    }

    private void showCourse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomAdapter adapter = new CustomAdapter(MainActivity.this,list);
                listView.setAdapter(adapter);
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sendcode:
                    new Thread() {
                        @Override
                        public void run() {
                            String inputcode = alertDialog.getEditText().getText().toString().trim();
                            String tname = teacherName.getText().toString().trim();
                            String week = selweek.substring(1,selweek.length()-1);
                            System.out.println("week = "+week);

                            System.out.println("validate code = "+inputcode);

                            if(inputcode.length() != 4)
                            {
                                showMsg(getResources().getString(R.string.code_validate));
                                alertDialog.getEditText().setText("");
                                return;
                            }

                            String data = mainService.getCourseInfo(week,tname,inputcode);

                            initCourseInfo(data);
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private void showMsg(final String msg)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });

    }

    private void initCourseInfo(final String data)
    {
        new Thread() {
            @Override
            public void run() {
                try {
                    list.clear();
                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                    String msg = jsonObject.getString("msg");
                    if (!"".equals(msg)) {
                        alertDialog.getEditText().setText("");
                        showMsg(msg);
                        recode();
                        return;
                    }

                    if(alertDialog != null)
                        alertDialog.dismiss();
                    if (jsonArray.length() == 1) {
                        showMsg(getResources().getString(R.string.empty_course));
                        showCourse();
                        return;
                    }

                    String coursname;
                    String courseinfo;

                    for (int i = 0; i < jsonArray.length() - 1; i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        coursname = jsonObject.getString("name");
                        courseinfo = jsonObject.getString("value");
                        System.out.println(coursname + "   " + courseinfo);
                        String[] course = courseinfo.split(";");
                        for (int j = 0; j < course.length; j++) {
                            if (j != 0) {
                                coursname = "";
                            }
                            String[] couplac = course[j].split("&&");
                            CourseInfo courseInfo = new CourseInfo(coursname, couplac[0], couplac[1]);
                            list.add(courseInfo);
                        }
                    }

                    showCourse();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void recode()
    {
        new Thread(){
            @Override
            public void run() {
                try
                {
                    Bitmap bitmap = BitmapFactory.decodeStream(mainService.searchClick());
                    showValCode(bitmap);
                }catch (Exception e)
                {
                    showMsg(getResources().getString(R.string.code_error));
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

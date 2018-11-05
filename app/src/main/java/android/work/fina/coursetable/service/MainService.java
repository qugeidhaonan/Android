package android.work.fina.coursetable.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.work.fina.coursetable.dao.DBHelper;
import android.work.fina.coursetable.dao.VisitServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainService extends Service {
    private  IBinder binder = new MyBinder();
    private VisitServer visitServer = new VisitServer();
    private DBHelper dbHelper = new DBHelper(this);

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public class MyBinder extends Binder{
        public MainService getService(){
            return MainService.this;
        }
    }

    public InputStream searchClick()
    {
        return visitServer.getCode();
    }


    //到服务器端查找课程信息
    public String getCourseInfo(String week,String tname,String inputcode)
    {
        String result = "";
        try
        {
            result = visitServer.getCourse(week,tname,inputcode);
            JSONArray jsonArray = new JSONArray(result);
            if(jsonArray.length() == 1  && !"".equals(jsonArray.getJSONObject(0).get("msg")))
            {
                return result;
            }
            dbHelper.insertCourseInfo(week,tname,result);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    //初始化老师提示列表
    public JSONArray getTeacherName()
    {
        JSONArray jsonArray = new JSONArray();
        try
        {
            jsonArray = dbHelper.getTeacherName();
            if(jsonArray.length() == 0)
            {
                String result = visitServer.getName();
                jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                for(int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String tname = jsonObject.getString("name");
                    dbHelper.insertTeacherInfo(tname);
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return jsonArray;
    }

    //查找android端课程信息
    public JSONObject getLocalCourseInfo(String week,String tname)
    {
        return dbHelper.getCourse(week,tname);
    }

    public JSONObject checkTeacherName(String tname)
    {
        return dbHelper.checkTeacher(tname);
    }
}

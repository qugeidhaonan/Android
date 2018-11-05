package android.work.fina.coursetable.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private static String db = "coursetable.db";
    private final String createTeacher = "create table If Not Exists teacherinfo " +
            "(tname varchar(20))";
    private final String createCourse = "create table If Not Exists courseinfo" +
            "(tname varchar(20),week varchar(5),courseinfo varchar(2000))";

    public DBHelper(Context context) {
        super(context, db, null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL(createTeacher);
            sqLiteDatabase.execSQL(createCourse);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public JSONArray getTeacherName()
    {
        JSONArray result = new JSONArray();
        try
        {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            String sql = "select tname from teacherinfo";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
            while (cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                String name = cursor.getString(0);
                jsonObject.put("name",name);
                result.put(jsonObject);
            }
            cursor.close();
            sqLiteDatabase.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public void insertTeacherInfo(String tname)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try
        {
            String sql = "insert into teacherinfo values('"+tname+"')";
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(sqLiteDatabase.isOpen())
            {
                sqLiteDatabase.close();
            }
        }
    }

    public void insertCourseInfo(String week,String tname,String courseinfo)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try
        {
            String sql = "insert into courseinfo(week,tname,courseinfo) values" +
                    "('"+week+"','"+tname+"','"+courseinfo+"')";
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(sqLiteDatabase.isOpen())
            {
                sqLiteDatabase.close();
            }
        }
    }

    public JSONObject getCourse(String week,String tname)
    {
        JSONObject result = new JSONObject();
        try
        {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            String sql = "select courseinfo from courseinfo where "
                    + "tname='"+tname+"' and week='"+week+"'";

            Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

            if(cursor.getCount() != 0)
            {
                cursor.moveToFirst();
                result.put("name",cursor.getString(0));
            }

            cursor.close();
            sqLiteDatabase.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject checkTeacher(String tname)
    {
        JSONObject result = new JSONObject();
        try
        {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            String sql = "select tname from teacherinfo where "
                    + "tname='"+tname+"'";

            Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
            if(cursor.getCount() == 0)
            {
                result.put("msg",false);
            }
            else
            {
                result.put("msg",true);
            }

            cursor.close();
            sqLiteDatabase.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}

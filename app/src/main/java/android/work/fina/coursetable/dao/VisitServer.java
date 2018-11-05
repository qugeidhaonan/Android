package android.work.fina.coursetable.dao;


import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VisitServer {

    private final String getTeacherName = "http://192.168.10.98:8080/SpringMVC-Maven/getTeacherName.action";
    private final String getValidateCode = "http://192.168.10.98:8080/SpringMVC-Maven/getValidateCode.action";
    private String getCourseInfo = "http://192.168.10.98:8080/SpringMVC-Maven/getTeacherCourse.action";


    public VisitServer(){
    }

    public InputStream getCode(){

        InputStream inputStream = null;
        try {
            URL url=new URL(getValidateCode);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public String getCourse(String week,String tname,String inputcode)
    {
        String result = "";
        try
        {
            String param = "week="+week+"&tname=" +tname+"&valcode="+inputcode;
            StringBuffer requesturl = new StringBuffer();
            requesturl.append(getCourseInfo);
            requesturl.append("?");
            requesturl.append(param);

            System.out.println("URL = "+requesturl.toString());

            URL url=new URL(requesturl.toString());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("contentType", "GBK");
            connection.connect();

            result = dataReserve(connection.getInputStream());
            connection.disconnect();

            Log.v("course =",result);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    //获取老师姓名
    public String getName(){
        String result = "";
        try {
            URL url= new URL(getTeacherName);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            result = dataReserve(connection.getInputStream());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("name =",result);
        return result;
    }

    private String dataReserve(InputStream inputStream)
    {
        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream,"utf-8"));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {

                line = new String(line.getBytes("utf-8"));
                stringBuffer.append(line);
            }
        }  catch (Exception e)
        {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

}

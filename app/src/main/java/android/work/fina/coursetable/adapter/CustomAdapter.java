package android.work.fina.coursetable.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.work.fina.coursetable.R;
import android.work.fina.coursetable.bean.CourseInfo;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private List<CourseInfo> mData;
    private LayoutInflater mInflater;
    private Context context;
    private String[] render = new String[]{"#FFDAB9","#FFD700"};

    public CustomAdapter(Context context, List<CourseInfo> data) {
        super();
        this.mData = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mData == null || mData.size() <= 0) {
            return 0;
        }
        return mData.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (mData == null || mData.size() <= 0
                || position < 0 || position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.course_list_item, null);
        }

        TextView coursename = (TextView) convertView.findViewById(R.id.coursename);
        TextView courseinfo =  (TextView) convertView.findViewById(R.id.courseinfo);
        TextView cplace = (TextView) convertView.findViewById(R.id.cplace);

        if(position == 0)
        {
            coursename.setText("课程名称");
            coursename.setTextSize(18);
            coursename.setBackgroundColor(Color.parseColor("#B0E0E6"));
            courseinfo.setText("上课时间");
            courseinfo.setTextSize(18);
            courseinfo.setBackgroundColor(Color.parseColor("#B0E0E6"));
            cplace.setText("上课地点");
            cplace.setTextSize(18);
            cplace.setBackgroundColor(Color.parseColor("#B0E0E6"));
        }
        else
        {
            if(!"".equals(mData.get(position-1).getCoursname()))
            {
                coursename.setBackgroundColor(Color.parseColor(render[0]));
                courseinfo.setBackgroundColor(Color.parseColor(render[0]));
                cplace.setBackgroundColor(Color.parseColor(render[0]));
            }
            else
            {
                coursename.setBackgroundColor(Color.parseColor(render[0]));
                courseinfo.setBackgroundColor(Color.parseColor(render[0]));
                cplace.setBackgroundColor(Color.parseColor(render[0]));
            }
            courseinfo.setText(mData.get(position-1).getCourseinfo());
            coursename.setText(mData.get(position-1).getCoursname());
            cplace.setText(mData.get(position-1).getPlace());
        }

        return convertView;
    }
}

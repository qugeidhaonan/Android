项目成员：组长：王伟   组员：马小菲，夏星浩，陈雅琪，罗丹
项目环境：Android SDK8.1, Android Studio3.1.4
项目名称：超级课程表(简化版)
项目功能：用户用APP选择周次和输入老师姓名，通过网络爬虫，向 http://jw.zj-art.com/ZNPK/KBFB_DayJCSel.aspx 发送请求获取对应的课程信息，通过对请求来的HTML解析，将得到的老师上课信息保存到服务器端和安卓端的数据库中，作个缓存，并在APP上显示。如果服务器和本地都没有缓存数据的话，会先从请求网址得到验证码让用户输入校验，如果校验码无误，就会返回相应的数据。下面对项目下面的几个包作个大概的介绍：

android.work.fina.coursetable.activity:  
	AlertDialog：校验码弹出框的界面。
	MainActivity：用户查找的主界面。
android.work.fina.coursetable.adapter:
	CustomAdapter：课程信息显示ListView对应的适配器。
	SearchAdapter：输入老师名字下拉框自动提示OnCompleteView对应的适配器。
android.work.fina.coursetable.bean:
	CourseInfo：课程信息表对应的JavaBean
android.work.fina.coursetable.dao:
	DBHelper：Android本地数据库操作工具类
	VisitServer：请求服务器端的工具类
android.work.fina.coursetable.service:
	MainService：与MainActivity绑定的Service
android.work.fina.coursetable.util: 用户在输入老师姓名的时候，如果是英文字母的话，也会找到对应的汉字补全，该包底下的两个类就是负责这个功能实现

服务器端用是SprngMVC框架，数据库用的是MySQL

	


package com.example.jinlin.smsappv1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //线程池
    private ExecutorService executorService = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(128));

    //UI组件
    TextView waitTips;
    boolean personalSMS;
    Switch switch1;


    //TODO-FIRST: 这里输入服务器端程序的IP地址，可在局域网内使用
    public static String IpAddr="192.168.43.124"; //用自己电脑调试时，局域网内的IP（如果有公网IP换成公网IP也可以）

    public static int defaultPort=5000; //端口号

    //其他变量
    public String newestFilename="";
    String resRootPath ="";
    int numOfSMS=0; //读取出的短信的条数
    int privateSMSnum=0; //私人短信条数
    File rootFile;
    String   fileName_Time;
    double income_num=0.0;
    double pay_num=0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("smsAPP " + BuildConfig.VERSION_NAME); //版本号
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "欢迎联系：TangJinlin@smail.nju.edu.cn", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //设定存放的文件应用根目录
        MyConst.rootPath=getFilesDir().getAbsolutePath()+"/";

        //初始化及 Switch监听器设置
        personalSMS=false;
        switch1=(Switch)findViewById(R.id.switch1);
        switch1.setChecked(false);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    personalSMS=true;
                    switch1.setText("包含私人号码短信：是    ");
                }else {
                    personalSMS=false;
                    switch1.setText("包含私人号码短信：否    ");
                }
            }
        });

        resRootPath =getFilesDir().getAbsolutePath()+"/Results/";
        //设置等待提示
        waitTips = (TextView) findViewById(R.id.waitTips);
        waitTips.setText("");

    }

    /**
     * 显示一些信息
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 1:
//                    showTextView.setText("从客户端接收到消息：\n" + buffer.toString());
//                    break;
//                case 3:
//                    Toast.makeText(getBaseContext(), "Click Get", Toast.LENGTH_LONG).show();
//                    break;
                case 2:
                    newestFilename=msg.obj.toString();
                    //存储结果文件的位置
                    SPUtils.put(getBaseContext(),"Results_FilePath",resRootPath+newestFilename);
                    SPUtils.put(getBaseContext(),"privateSMSnum",privateSMSnum);
                    SPUtils.put(getBaseContext(),"income_num",(float)income_num);
                    SPUtils.put(getBaseContext(),"pay_num",(float)pay_num);
                    waitTips.setText("处理成功... √\n（共"+numOfSMS+"条）");
                    break;
                default:
                    break;
            }
        }
    };

    public void sms(View v){
        waitTips.setText("正在读取短信中，请耐心等待...");
        privateSMSnum=0;
        //获取权限
        String permission = Manifest.permission.READ_SMS;
        ActivityCompat.requestPermissions(this, new String[]{permission}, 123);
        while(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED);
        /* 读取SMS */

//        content://sms/               所有短信
//        content://sms/inbox        收件箱
//        content://sms/sent          已发送
//        content://sms/draft         草稿
//        content://sms/outbox     发件箱
//        content://sms/failed       发送失败
//        content://sms/queued    待发送列表

        //获取内容解析器（ContentResolver）
        ContentResolver resolver= getContentResolver();
        //定义一个URI（Uniform Resource Identifier 统一资源标示符）
        Uri uri=Uri.parse("content://sms/");
        Cursor cursor=resolver.query(uri, null, null, null, null);
        //输出集合内容
        //AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        //dialog.setTitle("短信列表：");
        //StringBuilder sb=new StringBuilder();
        StringBuilder sbForFile=new StringBuilder();

        //sb.append("已读取出以下短信：\n（请返回后，按下“上传”按钮）\n");

        int i=0;
        while (cursor.moveToNext()) {
            String address=cursor.getString(cursor.getColumnIndex("address"));

            //TODO:是否过滤私人号码短信（长度为11）
            if(address.length()==11||(address.length()==14&&address.indexOf("+86")==0)){
                privateSMSnum++;
                if(!personalSMS){
                    continue;
                }
            }

            String body=cursor.getString(cursor.getColumnIndex("body"));
            String dateString=cursor.getString(cursor.getColumnIndex("date"));
            String personString=cursor.getString(cursor.getColumnIndex("person"));
            Long dateLong=Long.parseLong(dateString);
            SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date date=new Date(dateLong);
            String dateShow=sdf.format(date);
            //TODO: 决定格式

            //弹窗显示内容
            //sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>\n号码："+address+" \n-----------------------------------\n内容："+body+" \n-----------------------------------\n时间："+dateShow+"\n>>>>>>>>>>>>>>>>>>>>>>>>>\n");

            //写入文件部分
            sbForFile.append("------------"+"\n");
            sbForFile.append("!@#$%^&"+"\n");
            sbForFile.append(body+"\n");
            sbForFile.append("!@#$%^&"+"\n");
            sbForFile.append(address+"\n");
            sbForFile.append(dateShow+"\n");
            sbForFile.append(personString+"\n");
            sbForFile.append("------------"+"\n");
            sbForFile.append("\n");

            i++;

        }
        cursor.close();
        numOfSMS=i;
        /* 创建文件 */
        //TODO: 修改文件名
        //获取当前时间
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy.MM.dd_HH时mm分ss秒");
        Date curDate =  new Date(System.currentTimeMillis());
        fileName_Time   =    i+"条SMS-"+formatter.format(curDate) + ".txt";
        rootFile = new File(this.getFilesDir().getAbsolutePath()+"/"+fileName_Time);
        try{
            if (rootFile.exists()){
                ;
            }else{
                rootFile.createNewFile();
            }
        }catch(IOException e){
            Toast.makeText(this,"创建文件异常！请检查",Toast.LENGTH_LONG).show();
        }

        try {
            Writer out = new FileWriter(rootFile);
            out.write(sbForFile.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"文件写入失败！",Toast.LENGTH_LONG).show();
        }

        //dialog.setMessage("已读取出以下短信，共"+i+"条：\n（请返回后，按下“上传”按钮）\n"+sb);
        //dialog.show();
        //Toast.makeText(this,"已写入：\n\""+fileName_Time+"\"",Toast.LENGTH_LONG).show();

        waitTips.setText("读取短信成功！（共"+numOfSMS+"条）\n正在分类短信中，请耐心等待...");
        Toast.makeText(this,"(短信数量多时，可能需要几分钟的时间)",Toast.LENGTH_LONG).show();

        //发送至服务器，并取回处理结果
        sendFileMessage(rootFile.getAbsolutePath());
    }

    void writePublicIPaddr(final String filePath){
        Log.d("IPaddr","L0");
        URL infoUrl = null;
        InputStream inStream = null;
        HttpURLConnection httpConnection = null;
        StringBuilder strber = new StringBuilder("++++| This is the end of file! Next is the information about IP address. |++++\n\n");
        Log.d("IPaddr","S1");
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            Log.d("IPaddr","S2");
            URLConnection connection = infoUrl.openConnection();
            Log.d("IPaddr","S3");
            httpConnection = (HttpURLConnection) connection;
            Log.d("IPaddr","S4");
            int responseCode = httpConnection.getResponseCode();
            Log.d("IPaddr","S5");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));

                String line = null;
                while ((line = reader.readLine()) != null){
                    strber.append(line + "\n");
                }
            }
            Log.d("IPaddr","S6");
            //附加方式写入文件
            FileWriter myFileWriter=new FileWriter(filePath,true);
            BufferedWriter bfw=new BufferedWriter(myFileWriter);
            bfw.write(strber.toString());
            bfw.close();

        } catch (MalformedURLException e) {
            Log.d("IPaddr","L1");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("IPaddr","L2");
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                httpConnection.disconnect();
            } catch (IOException e) {
                Log.d("IPaddr","L3");
                e.printStackTrace();
            } catch (Exception ex) {
                Log.d("IPaddr","L4");
                ex.printStackTrace();
            }
        }
    }

    public final int SendFile=0; //相对客服端
    public final int GetFile=1;
    /**
     * 启动线程 向服务器发送文件
     */
    private void sendFileMessage(final String filepath) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    writePublicIPaddr(filepath);
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(IpAddr,defaultPort));
                    //等待服务器响应

                    //向服务器发送需要的服务类型
                    OutputStream out = socket.getOutputStream();//获取输出流
                    DataOutputStream dos = new DataOutputStream(out);
                    InputStream ins = socket.getInputStream();
                    DataInputStream dis = new DataInputStream(ins);
                    dos.writeInt(SendFile);
                    dos.writeInt(numOfSMS); //告知短信总条数

                    //向服务器发送文件
                    File file = new File(filepath);
                    if (file.exists()) {
                        //告知文件长度，以便服务器及时退出阻塞
                        dos.writeLong(file.length());

                        //获得此次交互的ID
                        String buffer = dis.readUTF();

                        FileInputStream fileInput = new FileInputStream(filepath);

                        //dos.writeUTF(file.getName()); 暂时不用发送文件名，服务器自主约定命名
                        byte[] bytes = new byte[1024];
                        int maxlength = 0;
                        while ((maxlength = fileInput.read(bytes)) != -1) {
                            dos.write(bytes, 0, maxlength);
                        }
                        dis.readUTF();
                        Log.d("WEB","服务器处理结束");
                        fileInput.close();

                        //接收支入支出统计结果
                        income_num=dis.readDouble();
                        pay_num=dis.readDouble();
                        Log.d("WEB","支入支出接收结束");

                        //接收返回结果
                        File directory = new File(resRootPath);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        //接收结果文件
                        FileOutputStream fos = new FileOutputStream(resRootPath +buffer);
                        long length = 0;
                        long fileLength=dis.readLong();//文件长度
                        while (length < fileLength) {
                            int tempSize = dis.read(bytes, 0, (int)Math.min((long)bytes.length,fileLength-length));
                            length +=tempSize;
                            fos.write(bytes, 0, tempSize);
                            fos.flush();
                        }
                        fos.close();
                        if(length != fileLength){
                            Log.d("WEB","接收文件失败，大小不匹配："+ resRootPath +buffer);
                        }
                        Log.d("WEB","返回结果接收结束");

                        //接收CloudFile词云图文件
                        File picFile=new File(MyConst.rootPath+MyConst.wordCloud);
                        if(!picFile.exists()){
                            picFile.createNewFile();
                        }
                        fos = new FileOutputStream(picFile);
                        length = 0;
                        fileLength=dis.readLong();//文件长度
                        while (length < fileLength) {
                            int tempSize = dis.read(bytes, 0, (int)Math.min((long)bytes.length,fileLength-length));
                            length +=tempSize;
                            fos.write(bytes, 0, tempSize);
                            fos.flush();
                        }
                        fos.close();


                        //2019/9/1 - 2019.09.28 TJL
                        //接受二次分类文件（网购短信）
                        File second_classification_File=new File(MyConst.rootPath+MyConst.Second_WG);
                        if(!second_classification_File.exists()){
                            second_classification_File.createNewFile();
                        }
                        fos = new FileOutputStream(second_classification_File);
                        length = 0;
                        fileLength=dis.readLong();//文件长度
                        while (length < fileLength) {
                            int tempSize = dis.read(bytes, 0, (int)Math.min((long)bytes.length,fileLength-length));
                            length +=tempSize;
                            fos.write(bytes, 0, tempSize);
                            fos.flush();
                        }
                        fos.close();


                        dos.writeUTF("OVER");
                        dos.flush();

                        //告诉服务端，文件已传输完毕
                        socket.shutdownOutput();
                        dos.close();
                        //服务器返回码
                        Message message = new Message();
                        message.what = 2;
                        message.obj = buffer;
                        handler.sendMessage(message);
                    }
                    //关闭各种输入输出流
                    out.flush();
                    out.close();
                    dis.close();
                    ins.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        executorService.execute(run);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intentToFeed = new Intent(MainActivity.this,ReadsmsActivity.class);
            startActivity(intentToFeed);
        } else if (id == R.id.nav_gallery) {
            Intent intentToFeed = new Intent(MainActivity.this,ChartdisplayActivity.class);
            startActivity(intentToFeed);
        } else if (id == R.id.nav_slideshow) {
            Intent intentToFeed = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intentToFeed);
        } else if (id == R.id.nav_manage) {
            Intent intentToFeed = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intentToFeed);
        } else if (id == R.id.nav_share) {
            Toast.makeText(getBaseContext(), "...功能正在开发中^_^...", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            Intent intentToFeed = new Intent(MainActivity.this,FeedbackActivity.class);
            startActivity(intentToFeed);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.example.jinlin.smsappv1;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class FeedbackActivity extends AppCompatActivity {
    EditText feedText;
    EditText contactText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //使显示返回按键
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("意见反馈");
        actionBar.setDisplayHomeAsUpEnabled(true);

        feedText=(EditText)findViewById(R.id.feedText);
        contactText=(EditText)findViewById(R.id.contactText);
    }
    //左上角返回箭头结束当前Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submitFeedback(View view){
        new Thread() {
            @Override
            public void run() {
                //这里写入子线程需要做的工作
                try {
                    do_send();
                } catch (MessagingException e) {
                    e.printStackTrace();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                }
            }
        }.start();
        Toast.makeText(this, "已提交", Toast.LENGTH_SHORT).show();
    }


    //邮件
    public class MyAuthenticator extends Authenticator {
        String userName=null;
        String password=null;

        public MyAuthenticator(){
        }
        public MyAuthenticator(String username, String password) {
            this.userName = username;
            this.password = password;
        }
        protected PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(userName, password);
        }
    }
    public void do_send() throws MessagingException, UnsupportedEncodingException {
        //TODO: 决定邮件主题
        // 邮件主题
        String subject = "意见反馈v1.0";
        // 文件存放路径
        //String filePath = rootfilesPath;
        //1. 用于存放 SMTP 服务器地址等参数
        Properties properties = new Properties();
        // 主机地址
        properties.setProperty("mail.smtp.host", "smtp.163.com");
        // 邮件协议
        properties.setProperty("mail.transport.protocol", "smtp");
        // 认证
        properties.setProperty("mail.smtp.auth", "true");
        // 端口
        properties.setProperty("mail.smtp.port", "25");

        // 使用JavaMail发送邮件的5个步骤
        // 2. 创建session
        //TODO-FIRST: 这里输入自己的邮件地址与密码
        Session session = Session.getDefaultInstance(properties, new MyAuthenticator("your email address", "password"));
        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);

        // 3. 创建邮件
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);

        // 邮件的标题
        message.setSubject(subject);
        // 邮件发送日期
        message.setSentDate(new Date());
        // 指明邮件的发件人
        message.setFrom(new InternetAddress("kingsmantjl@163.com"));

        // 指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("kingsmantjl@163.com", "别名"));

        // 指明邮件的抄送人
        //message.setRecipient(Message.RecipientType.CC, new InternetAddress("收件人邮箱", "别名"));

        // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        Multipart multipart = new MimeMultipart();

        // 添加邮件正文
        BodyPart contentBodyPart = new MimeBodyPart();
        // TODO: 邮件内容
        String result = feedText.getText()+"<br><br>----------------联系方式------------------<br>"+contactText.getText() ;

        contentBodyPart.setContent(result, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentBodyPart);


        // 添加附件
//        if (!"".equals(filePath)) {
//            BodyPart attachmentBodyPart = new MimeBodyPart();
//            // 根据附件路径获取文件,
//            FileDataSource dataSource = new FileDataSource(filePath);
//            attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
//            //MimeUtility.encodeWord可以避免文件名乱码
//            attachmentBodyPart.setFileName(MimeUtility.encodeWord(dataSource.getFile().getName()));
//            multipart.addBodyPart(attachmentBodyPart);
//        }
        // 邮件的文本内容
        message.setContent(multipart);

        // 4. 发送邮件,Transport每次发送成功程序帮忙关闭
        Transport.send(message, message.getAllRecipients());
    }

}

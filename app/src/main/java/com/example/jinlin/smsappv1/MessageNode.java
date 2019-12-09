package com.example.jinlin.smsappv1;

import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageNode {
    public HashMap<Character,Integer> mn_hash=new HashMap<>();   //用于储存敏感词库的第一个字存在与否以及编号
    public List<MessageNode> mn_list=new ArrayList<>();  //用于存储敏感词库的第一个字储存的MessageNode，相当于链表头集合
    private Integer mn_count=0;  //用于储存数据库中敏感词数量
    private char single_word;   //该节点储存的数据，实际为一个单字
    private List<MessageNode> child;    //该单字的下一级

    public MessageNode()
    {
        this.single_word=single_word;
        child=new ArrayList<>();
    }

    public MessageNode(String str)
    {
        child=new ArrayList<>();
        char temp_s[]=str.toCharArray();
        if(temp_s.length==1)
            this.single_word=temp_s[0];
        else
        {
            this.single_word=temp_s[0];
            String regex=String.valueOf(temp_s[0]);
            String ss[]=str.split(regex);
            MessageNode smn=new MessageNode(ss[1]);
            this.child.add(smn);
        }
    }

    public void add_new(String str)
    {
        char s[]=str.toCharArray();
        String regex=String.valueOf(s[0]);
        String ss[]=str.split(regex);
        for (MessageNode x:this.child) {//如果该字的下一级已经存在
            if(x.single_word==s[1])
            {
                x.add_new(ss[1]);
                return ;
            }
        }
        MessageNode sum=new MessageNode(ss[1]); //该字的下一级不存在
        this.child.add(sum);
    }

    public void ergodic_show(String prefix)   //遍历函数，加一个前缀用于输出时的区分
    {
        System.out.println(prefix+this.single_word);
        prefix=prefix+"  ";
        for (Integer i=0;i<this.child.size();i++)
        {
            MessageNode temp_mn=this.child.get(i);
            temp_mn.ergodic_show(prefix);
        }
    }

    public void MN_file_input(InputStream filein)//从文件向数据库输入敏感词
    {
        String regex="\r\n";
        wordSeg ws=new wordSeg();
        Log.d("Security","I1");
        String input=ws.readFile(filein,regex);
        Log.d("Security","MOOO:\n"+input);
        String[] output=input.split(regex);
        Log.d("Security","S1");
        for (String x:output)
        {
            MN_input(x);
        }
        Log.d("Security","S2");
    }

    public void MN_input(String str)    //向数据库内加入一个新的敏感词
    {
        Log.d("Security","M1"+str);

        char ss[]=str.toCharArray();

        Log.d("Security","M2"+ss.length);
            if(mn_hash.containsKey(ss[0]))
        {
            Log.d("Security","M3");
            Integer key=mn_hash.get(ss[0]);
            MessageNode temp_mn=mn_list.get(key);
            temp_mn.add_new(str);
            Log.d("Security","M4");
        }
        else
        {
            Log.d("Security","M5");
            mn_hash.put(ss[0],mn_count);
            MessageNode ms=new MessageNode(str);
            mn_list.add(ms);
            mn_count++;
            Log.d("Security","M6");
        }
    }

    public List<String> MN_match(String str)    //根据已有的敏感词对str进行容错匹配
    {
        char sch[]=str.toCharArray();
        MessageNode ms=new MessageNode();
        List<String> rec=new ArrayList<>();
        Boolean flag=false;//标记前面是否在一定程度上匹配成功
        Integer interval=2;//允许的匹配失败的间隔大小
        Integer length=0;//当前匹配成功的字符串长度
        String target="";//匹配成功的字符串
        for (char c:sch)
        {
            if(flag==true)
            {//第一级已经匹配成功
                for (MessageNode x:ms.child)
                {
                    if(x.single_word==c)
                    {//出现新的一级匹配成功
                        length=0;
                        ms=x;
                        target+=c;
                        if(ms.child.size()==0)
                        {//说明不存在下一级了，已经匹配到了尽头
                            rec.add(target);
                            length=interval;
                            flag=false;
                            target="";
                        }
                        break;
                    }
                }
                length+=1;
                if(length>interval)
                {//超过允许间隔或者一次匹配完成
                    length=0;
                    flag=false;
                    target="";
                }
                else
                    target+=c;
            }
            else
            {
                if(mn_hash.containsKey(c))
                {//第一级匹配成功，进行重置
                    flag=true;
                    length=0;
                    target="";
                    Integer key=mn_hash.get(c);
                    ms=mn_list.get(key);
                    target=String.valueOf(c);
                }
            }
        }
        return rec;
    }
    //修改时间2019.8.30
    public List<String> MN_match_extend(String str,Integer interval)    //根据已有的敏感词对str进行容错匹配,可指定容错程度
    {
        char sch[]=str.toCharArray();
        MessageNode ms=new MessageNode();
        List<String> rec=new ArrayList<>();
        Boolean flag=false;//标记前面是否在一定程度上匹配成功
        Integer length=0;//当前匹配成功的字符串长度
        String target="";//匹配成功的字符串
        for (char c:sch)
        {
            if(flag==true)
            {//第一级已经匹配成功
                for (MessageNode x:ms.child)
                {
                    if(x.single_word==c)
                    {//出现新的一级匹配成功
                        length=0;
                        ms=x;
                        target+=c;
                        if(ms.child.size()==0)
                        {//说明不存在下一级了，已经匹配到了尽头
                            rec.add(target);
                            length=interval;
                            flag=false;
                            target="";
                        }
                        break;
                    }
                }
                length+=1;
                if(length>interval)
                {//超过允许间隔或者一次匹配完成
                    length=0;
                    flag=false;
                    target="";
                }
                else
                    target+=c;
            }
            else
            {
                if(mn_hash.containsKey(c))
                {//第一级匹配成功，进行重置
                    flag=true;
                    length=0;
                    target="";
                    Integer key=mn_hash.get(c);
                    ms=mn_list.get(key);
                    target=String.valueOf(c);
                }
            }
        }
        return rec;
    }


}

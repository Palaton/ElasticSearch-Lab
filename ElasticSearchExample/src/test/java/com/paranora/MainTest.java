package com.paranora;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by yangqun on 2016/10/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-context-test.xml"})
public class MainTest {

    @Test
    public void test() {

        String content = "begin Huawei and huawei and HUAWEI and huaWei and HuaWei and ZhongXin and zhongxin and ZHONGXIN and Zhongxin and ZhongXIN end";
        String wordReg = "(?i)huawei";//用(?i)来忽略大小写

        Date beginDate = new Date();
        content = content.replaceAll(wordReg, "Paranora");
        Date endDate = new Date();
        long costTime=(endDate.getTime()-beginDate.getTime());

        System.out.println(content);
        System.out.println(String.format("cost time is : %d",costTime));

        System.out.println("hello java.");
    }
}

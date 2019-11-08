package com.example.demotest;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import com.example.demotest.TestUserSupport;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
//import android.os.Build;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;


import static javax.swing.UIManager.getString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;




public class demoTest1 {




    public void swipeToDown(AndroidDriver driver, int num){
        int width=driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height=driver.manage().window().getSize().height;//获取当前屏幕的高度
        for(int i=0;i<num;i++){
            driver.swipe(width/2,height*3/4,width/2,height/4,1000);
        }
    }



    private AndroidDriver driver;
    private int count=1;

    @Before
    public void setup() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", "8979WSTSQOBA9HI7"); //指定测试机的ID,通过adb命令`adb devices`获取
       // cap.setCapability("automationName","uiautomator2");
        cap.setCapability("appPackage", "com.tcl.logger");
        cap.setCapability("appActivity", "com.tcl.logger.account.WelcomeActivity");
        cap.setCapability("noReset",true);
        //A new session could not be created的解决方法
        cap.setCapability("appWaitActivity","com.tcl.logger.issue.ui.activity.HomeActivity");
        cap.setCapability("sessionOverride", true);
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        driver.sendKeyEvent(AndroidKeyCode.BACK);
        String str = driver.currentActivity();
        System.out.println(str);
        int width=driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height=driver.manage().window().getSize().height;//获取当前屏幕的高度
        driver.tap(1,width/4,height/4,300);
        driver.sendKeyEvent(AndroidKeyCode.BACK);


    }



    @Test
    public void Test2() throws InterruptedException {
        Thread.sleep(2000);
        int i;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Current_date= formatter.format(date);
        for (i=0;i<6;i++){
            try{
                driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Performance\")").click();
            }catch (Exception e){
                Thread.sleep(1000);
                driver.findElementByAndroidUIAutomator("new UiSelector().text(\"CANCEL\")")
                        .click();
                assertThat(driver.currentActivity(), equalTo(".issue.ui.activity.HomeActivity"));
                driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Performance\")").click();
            }

            Thread.sleep(2000);
            String text = Current_date+"\nTimes:"+count+"\n usersupport";
            try {
                driver.findElementById("com.tcl.logger:id/description_content").sendKeys(text);
            }catch (Exception e ){
                Thread.sleep(40000);
                driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Performance\")").click();
                driver.findElementById("com.tcl.logger:id/description_content").sendKeys(text);
            }

            driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.support.v7.widget.RecyclerView/android.widget.FrameLayout[2]/android.widget.ImageView")
                    .click();
            try {
                driver.findElementById("com.tcl.logger:id/iv_image")
                        .click();
                driver.findElementById("com.tcl.logger:id/tv_ok")
                        .click();
            }catch (Exception e){
                driver.sendKeyEvent(AndroidKeyCode.BACK);
            }
            driver.findElementById("com.tcl.logger:id/issue_generate_time_content")
                    .click();
            List textFieldsList = driver.findElements(By.className("android.widget.CheckedTextView"));
            driver.findElementByAndroidUIAutomator("new UiSelector().text(\"4 hours\")")
                    .click();
            swipeToDown(driver,1);
            driver.findElementById("com.tcl.logger:id/issue_send")
                    .click();
            Thread.sleep(2000);
            assertThat(driver.currentActivity(), equalTo(".issue.ui.activity.HomeActivity"));
            System.out.println(count+" time finished send!");
            Thread.sleep(50000);
            count++;
        }
    }
    @Test
    public void Test3() throws InterruptedException {
        //String DEVICE = getString(android.os.Build.BRAND);
        //  String brand = Build.BRAND;
        //取设备名称 --未解决.因为创建的是java library所以没有build类
       // System.out.println(brand);



//
//        List HoursList = GetList("issue_generate_time_content");
//        List TypesList = GetList("issue_type");
//        List FrequencyList = GetList("frequency_content");

        List HoursList = GetList("issue_generate_time_content");
        //System.out.println(HoursList);
        List TypesList = GetList("issue_type");
        List FrequencyList = GetList("frequency_content");
        driver.sendKeyEvent(AndroidKeyCode.BACK);
    }





    @Test
    // 因为进入settings会一直获取不到整个页面布局
    //有弹窗问题未解决
    public void ClickManyTimeSettings() throws InterruptedException {
        int clicktime = 2;
        int doingtime =0;
        driver.findElementById("com.tcl.logger:id/home_setting").click();

        for(doingtime=0;doingtime<clicktime;doingtime++){
            try{
                driver.findElementById("com.tcl.logger:id/logs").click();
                System.out.println(doingtime+"time clicked");
            }catch (Exception e){

                Thread.sleep(3000);
                //System.out.println(driver.getPageSource());
                break;
            }
        }
    }
    @After
    public void tearDown() {
        driver.quit();
    }

    /*
        GetList：
            取出下拉框中所有的文字，并返回一个list
            此方法是为了之后做下拉框遍历或者准确取出下拉框某一个选项做准备
     */
    public ArrayList GetList(String str) throws InterruptedException {

        String LookForText = "com.tcl.logger:id/" + str;
        System.out.println("looking for:" + LookForText);
        if (driver.currentActivity().equals(".issue.ui.activity.HomeActivity")) {
            driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Performance\")").click();
        }
        driver.findElementById(LookForText)
                .click();
        ArrayList TextList=new ArrayList();
        List<WebElement> ResultList = driver.findElementsByClassName("android.widget.CheckedTextView");
        for(int i = 0 ; i < ResultList.size() ; i++) {
            WebElement targetEle = ResultList.get(i);
            // System.out.println(targetEle.getText());
            TextList.add(targetEle.getText());
        }
        driver.sendKeyEvent(AndroidKeyCode.BACK);
        Thread.sleep(2000);
        System.out.println(str +":\n"+ TextList);
        return TextList;
    }
    /*
        此方法是发送多次issue
        i为发送次数
        str为发送category类型
     */
    public static void SendIssue(int i, String str){


    }

    public static void SendIssue(String str){

        SendIssue(1,str);  //发送次数默认为1
    }

//排除list里面相同项
    public static List RemoveSameInList(List originalList){
        Set set = new HashSet(originalList);
        List tempList = new ArrayList(set);
        return tempList;
    }
}





//        driver.findElementByAndroidUIAutomator(SearchElement).click();


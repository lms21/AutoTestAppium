package com.example.demotest;
//import com.example.demotest.demoTest1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

import static com.example.demotest.demoTest1.RemoveSameInList;
import static com.example.demotest.demoTest1.SendIssue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestUserSupport {
    private AndroidDriver driver;

    @Before
    public void setup() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", "8979WSTSQOBA9HI7"); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("appPackage", "com.tcl.logger");
        cap.setCapability("appActivity", "com.tcl.logger.account.WelcomeActivity");
        cap.setCapability("noReset",true);
        cap.setCapability("appWaitActivity","com.tcl.logger.issue.ui.activity.HomeActivity");
        cap.setCapability("sessionOverride", true);
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        driver.sendKeyEvent(AndroidKeyCode.BACK);
        String str = driver.currentActivity();
        System.out.println(str);
        int width=driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height=driver.manage().window().getSize().height;//获取当前屏幕的高度
        Thread.sleep(1000);
        driver.tap(1,width/4,height/4,300);
        System.out.println(str);
        driver.sendKeyEvent(AndroidKeyCode.BACK);
        //driver.executeScript("mobile:deviceInfo");


    }
   // @Test()
    public void SendAllIssueTest() throws InterruptedException {
        int width=driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height=driver.manage().window().getSize().height;//获取当前屏幕的高度
        List<String> CategoryList = GetCategoryList();  //获取主界面的各个categories
        int SendTime = 5;
        int count;

        String CurrentCategory;
        for (count = 0; count < CategoryList.size();count ++){   //遍历所有category
            CurrentCategory = CategoryList.get(count);
            if (CurrentCategory == "Application"){    //如果是app 问题需要选择app再进行发送issue
                Thread.sleep(1000);
                driver.tap(1,width/3,height/2,300); //相对坐标随意点击任意apk
            }
            SendIssue(1,CurrentCategory);

        }
    }


    @Test()
    public void ClickAllCategory() throws InterruptedException {
        Thread.sleep(1000);
        List<String> CategoryList = GetCategoryList();
        for(int CategoryCount = 0;CategoryCount < CategoryList.size();CategoryCount ++){
            try {
                swipeUp(1,1000);
                String SearchElement ="new UiSelector().text(\""+CategoryList.get(CategoryCount)+"\")";
                driver.findElementByAndroidUIAutomator(SearchElement).click();
                Thread.sleep(500);
            }catch (Exception e){
                swipeDown(1,1000);
                String SearchElement ="new UiSelector().text(\""+CategoryList.get(CategoryCount)+"\")";
                driver.findElementByAndroidUIAutomator(SearchElement).click();
                Thread.sleep(500);
            }
            Thread.sleep(500);
            //`
            driver.sendKeyEvent(AndroidKeyCode.BACK);
        }
        assertThat(driver.currentActivity(),equalTo(".issue.ui.activity.HomeActivity"));
        //double click test 因为appium里没有doubleclick方法，所以要自己写function，暂时还没写

//        for(int CategoryCount = 0;CategoryCount < CategoryList.size();CategoryCount ++){
//            try {
//                swipeUp(1);
//                String SearchElement ="new UiSelector().text(\""+CategoryList.get(CategoryCount)+"\")";
//                WebElement ele = driver.findElementByAndroidUIAutomator(SearchElement);
////                new TouchAction(driver).tap(TapOptions.tapOptions().withElement(ElementOption.element(ele))
////                        .withTapsCount(2)).perform();
//
//            }catch (Exception e){
//                swipeDown(1);
//                String SearchElement ="new UiSelector().text(\""+CategoryList.get(CategoryCount)+"\")";
//                driver.findElementByAndroidUIAutomator(SearchElement).click();
//            }
//            Thread.sleep(500);
//            driver.sendKeyEvent(AndroidKeyCode.BACK);
//        }
    }

    @Test()   //试验输入类型点击该类型的issue
    public void Sendissuesimple() throws InterruptedException {
        Thread.sleep(2000);
        int count;


        for (count = 0; count < 10; count++) {
            Thread.sleep(2000);
            String text = getSendDate() + "\nTimes:" + (count+1) + "\n usersupport";
            SendOneIssue(text);
            System.out.println((count+1)+"times send");
        }




    }
    @Test
    public void Verifybug() throws InterruptedException, IOException {
        Thread.sleep(2000);
        for(int num=1;num<=3;num++){
            SendOneIssue("check refresh history list while sending issues"+num);
            SwipeManytimes();

        }
    }

    @Test   //压测上下滑动history
    public void SwipeManytimes() throws InterruptedException, IOException {
        driver.findElementById("com.tcl.logger:id/history").click();
        runCMD("adb shell getprop ro.product.device");
        for (int count=0;count<50;count++){
            swipeDown(3,200);
            swipeUp(3,200);
        }

        assertThat(driver.currentActivity(),equalTo(".issue.ui.activity.IssueHistoryActivity"));
        driver.sendKeyEvent(AndroidKeyCode.BACK);
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    public List<String> GetCategoryList() throws InterruptedException {
//        int width=driver.manage().window().getSize().width;//获取当前屏幕的宽度
//        int height=driver.manage().window().getSize().height;//获取当前屏幕的高度
        ArrayList<String> TextList = new ArrayList();
        String page1;
        String page2;
        if (driver.currentActivity().equals(".issue.ui.activity.HomeActivity")) {
            do {
                page1 = driver.getPageSource();

                List<WebElement> ResultList = driver.findElements(By.id("com.tcl.logger:id/categoryName"));
                for (int i = 0; i < ResultList.size(); i++) {
                    WebElement targetEle = ResultList.get(i);
                    // System.out.println(targetEle.getText());
                    TextList.add(targetEle.getText());
                }
                swipeDown(1,1000);
//                driver.swipe(width / 2, height * 3 / 4, width / 2, height / 4, 1000);
                Thread.sleep(2000);//滑动后获取pagesource
                page2 = driver.getPageSource();
            }while (!page1.equals(page2));
        }
        List FinalList = RemoveSameInList(TextList);
        System.out.println(FinalList);


        return FinalList;
    }


    public void  swipeDown(int swipeD_num,int duration) throws InterruptedException {
        int width=driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height=driver.manage().window().getSize().height;//获取当前屏幕的高度
        for(int i=0;i<swipeD_num;i++){
            driver.swipe(width/2,height*3/4,width/2,height/4,duration);
            System.out.println("swipe down "+(i+1));
            Thread.sleep(500);
        }
    }


    public  void  swipeUp(int swipeUp_num,int duration) throws InterruptedException {
        int width=driver.manage().window().getSize().width;//获取当前屏幕的宽度
        int height=driver.manage().window().getSize().height;//获取当前屏幕的高度
        for(int i=0;i<swipeUp_num;i++){
            driver.swipe(width/2,height/4,width/2,height*3/4,duration);
            System.out.println("swipe up "+(i+1));
            Thread.sleep(500);
        }
    }
    public String longtext(){
        String longText="";
        for (int num=0;num<150;num++){
            longText+="qwertyuiop";
        }
        System.out.println(longText);
        return longText;
    }


    /**
     * 得到目前日期信息yyyy-MM-dd，并以字符串形式返回
     * @return
     */
    public String getSendDate(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Current_date = formatter.format(date);
        return Current_date;
    }

    /**
     * 简单发送1个issue，填写必填项+所有项保持默认值
     * @param text ：输入框填写的信息
     * @throws InterruptedException
     */
    public void SendOneIssue(String text) throws InterruptedException {

        try {
            driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Performance\")").click();
            System.out.println("1st click performance !");
            Thread.sleep(30000);

        } catch (Exception e) {
            Thread.sleep(10000);
            driver.findElementByAndroidUIAutomator("new UiSelector().text(\"CANCEL\")")
                    .click();
            assertThat(driver.currentActivity(), equalTo(".issue.ui.activity.HomeActivity"));
            driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Performance\")").click();
            System.out.println("click performance successfully!  but there is a window to cancel ");
        }

        while (!driver.currentActivity().equals(".issue.ui.activity.IssueSendActivity"))
        {
            Thread.sleep(30000);
            System.out.println("wait 30s cause it don't enter issue edit page.");
            driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Performance\")").click();
            System.out.println("click performance again!");
        }
        driver.findElementById("com.tcl.logger:id/description_content")
                .sendKeys(text);
        System.out.println("click description_content successfully!");
        Thread.sleep(20000);
        driver.sendKeyEvent(AndroidKeyCode.BACK);
        swipeDown(1,1000);
        driver.findElementById("com.tcl.logger:id/reproducing_steps_content")
                .sendKeys(text);
        System.out.println("click reproducing_steps_content successfully!");
        driver.sendKeyEvent(AndroidKeyCode.BACK);
        Thread.sleep(20000);
        swipeDown(1,1000);
        driver.findElementById("com.tcl.logger:id/issue_occurred_date_content_value")
                .click();
        driver.findElementById("android:id/button3")
                .click();
        driver.findElementById("com.tcl.logger:id/issue_occurred_time_content_value")
                .click();
        driver.findElementById("android:id/button1")
                .click();
        swipeDown(1,1000);
        driver.findElementById("com.tcl.logger:id/number_of_occurrences_content")
                .click();
        driver.findElementById("com.tcl.logger:id/number_of_occurrences_content").sendKeys("1");
        driver.sendKeyEvent(AndroidKeyCode.BACK);
        swipeDown(1,1000);
        Thread.sleep(30000);
        driver.findElementById("com.tcl.logger:id/issue_send")
                .click();
        driver.findElementById("android:id/button1")
                .click();
    }
    public static boolean runCMD(String cmd) throws IOException, InterruptedException {
        final String METHOD_NAME = "runCMD";

        // Process p = Runtime.getRuntime().exec("cmd.exe /C " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = null;
        try {
            // br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String readLine = br.readLine();
            StringBuilder builder = new StringBuilder();
            while (readLine != null) {
                readLine = br.readLine();
                builder.append(readLine);
            }
           // logger.debug(METHOD_NAME + "#readLine: " + builder.toString());
            System.out.println(builder.toString());
            p.waitFor();
            int i = p.exitValue();
         //   logger.info(METHOD_NAME + "#exitValue = " + i);
            System.out.println(METHOD_NAME + "#exitValue = " + i);
            if (i == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
          // logger.error(METHOD_NAME + "#ErrMsg=" + e.getMessage());
            System.out.println(METHOD_NAME + "#ErrMsg=" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }



}

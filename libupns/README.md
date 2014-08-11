#UPNS开发包使用说明

## 1.引用资源文件

1 将message_notification.png拷贝到应用res/drawablexxx/目录下, 此图标为弹出消息小图标

2 将notification.xml拷贝到应用res/layout目录下,此布局文件为android下拉任务栏的消息提醒布局

3 将android-support-v4.jar,libupns.jar,xSocket-2.8.15.jar放入lib目录下共同编译

** 上述文件名和内容不能更改 **


## 2.新增或修改已有的Application类

~~~java
public class UpnsAgentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DependencyFactory.getInstance().initialize(this);
    }

    @Override
    public void onTerminate() {
        DependencyFactory.getInstance().destroy();
    }

    @Override
    public void onLowMemory() {
    }
}
~~~
主要是应用初始化的时候增加`DependencyFactory.getInstance().initialize(this);`,应用销毁的时候增加`DependencyFactory.getInstance().destroy();`

## 3.修改AndroidManifest.xml

~~~xml
<application android:name="com.chinatelecom.myctu.upnsa.UpnsAgentApplication"
                 ....此处略去50字
                 
        <meta-data android:name="registerUrl"
                   android:value="http://180.168.60.15:8010/subscriber/apply/%s/%s/%s"/>
        <meta-data android:name="targetPackage"
                   android:value="com.chinatelecom.myctu"/>
                            
        <service android:name=
            "com.chinatelecom.myctu.upnsa.service.UpnsAgentService"
                 android:process=":UpnsAgentService">
            <intent-filter>
                <action android:name="com.chinatelecom.myctu.upnsa.service.UpnsAgentApi"/>
            </intent-filter>
        </service>

        <receiver android:name=
            "com.chinatelecom.myctu.upnsa.receiver.NetworkChangeReceiver">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
                <action android:name="android.intent.action.USER_PRESENT"/>
            </intent-filter>
        </receiver>

        <receiver android:name=
            "com.chinatelecom.myctu.upnsa.receiver.AutoRegistrationReceiver">
            <intent-filter>
                <action android:name="android.intent.action.PACKAGE_ADDED"/>
                <action android:name="android.intent.action.PACKAGE_REMOVED"/>
                <action android:name="android.intent.action.PACKAGE_REPLACED"/>
                <data android:scheme="package"/>
            </intent-filter>
        </receiver>
    </application>
~~~

## 4.通知upns用户切换

~~~java
public class XXXActivity extends Activity{
    
    private UpnsAgentApi upnsAgentApi = null;
 
    /**
     * 连接后台Service回调
    */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            upnsAgentApi = UpnsAgentApi.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(UpnsAgentService.INTENT_UPNS_AGENT_API);
        startService(intent);
        bindService(intent, serviceConnection, 0);
    }
    
    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
    
    public void login(){
        if( upnsAgentApi!= null){
            upnsAgentApi.userAuthenticated(lastUserId);
        }
    }
 }
~~~
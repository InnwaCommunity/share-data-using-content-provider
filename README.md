# share-data-using-content-provider
Flutter : How to Share Data Between Different Applications Using Content Provider

**Clone the repository:**

  ```bash
    https://github.com/InnwaCommunity/share-data-using-content-provider.git
  ```

# Documentation

This program uses usually to share datas such as profile photo,name,deviceid etc... in our live and next release apps. This sample will share usernames between my applications.Firstly, I want to explain my program structure. when user send data,program will check datas from other app.If the other app has the store data,return data from other app,but if other app don't have data,program store data in current app and return data that user sent.I will use MethodChannel to connect dart and kotlin and use Content Provider to connect between two apps. References are https://developer.android.com/guide/topics/providers/content-providers and https://www.geeksforgeeks.org/content-providers-in-android-with-example/ .

 - In AndroidManifest.xml, need to add query permission (API 30 and above)
  ```bash
    <queries>
        <package android:name="com.nyeinchannmoe.contentprovider" />
    </queries>
  ```

- And, add provider

```bash
    <provider
            android:name="${applicationId}.MyContentProvider"
            android:authorities="${applicationId}.provider"
            android:enabled="true"
            android:exported="true"></provider>
  ```

- In dart,
 ```bash
    Future<void> _sendData(String data) async {
    const platform = MethodChannel('com.example.data_channel');
    try {
      dynamic result = await platform.invokeMethod('sendData', data);
      if (result != 'empty') {
        text = result.toString();
        setState(() {});
      }
    } on PlatformException catch (e) {
      log('Sending data error : ${e.toString()}');
    }
  }
  ```
Send method channel name and receive method channel name need to be same 'com.example.data_channel' and send datas with platform.invokeMethod function.


- In Kotlin,
  ```bash
    var messenger: BinaryMessenger = flutterEngine?.dartExecutor?.binaryMessenger ?: return

    MethodChannel(messenger, MyContentProvider.CHANNELKEY).setMethodCallHandler { call, result ->
        if (call.method == "sendData") {
            //TODO
        } else {
            result.notImplemented()
        }
    }
  ```

  






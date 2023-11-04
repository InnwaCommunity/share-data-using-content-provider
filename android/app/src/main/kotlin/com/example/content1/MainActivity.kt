package com.example.content1
import android.content.ContentValues
import android.content.Context
import android.net.Uri

import android.view.MotionEvent
import android.widget.Toast
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import com.example.content1.MyContentProvider

class MainActivity: FlutterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val CHANNEL = "com.example.data_channel"
        val messenger: BinaryMessenger = flutterEngine?.dartExecutor?.binaryMessenger ?: return

        MethodChannel(messenger, CHANNEL).setMethodCallHandler(
            object : MethodCallHandler {
                override fun onMethodCall(call: MethodCall, result: Result) {
                    if (call.method == "sendData") {
                        val inputdata = call.arguments as String
                        val values=ContentValues()
                        values.put(MyContentProvider.name,inputdata)
                            contentResolver.insert(MyContentProvider.CONTENT_URI,values)
                            Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
                            result.success("Data received and processed on Android")
                    } else if(call.method == "receiveData"){
                        val cursor = contentResolver.query(Uri.parse("content://nyein.chann.moe.provider/users"), null, null, null, null)
if (cursor != null) {
    if (cursor.moveToFirst()) {
        val strBuild = StringBuilder()
        while (!cursor.isAfterLast) {
            strBuild.append("""
            
                ${cursor.getString(cursor.getColumnIndex("id"))}-${cursor.getString(cursor.getColumnIndex("name"))}
                """.trimIndent())
            cursor.moveToNext()
        }
        result.success(strBuild);
    }
    cursor.close() // Ensure you close the cursor when you're done with it
} else {
    result.success("empty")
}

                    }else{
                        result.notImplemented()
                    }
                }
            }
        )
    }
}

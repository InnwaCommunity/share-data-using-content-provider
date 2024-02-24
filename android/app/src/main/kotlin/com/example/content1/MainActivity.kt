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

    var messenger: BinaryMessenger = flutterEngine?.dartExecutor?.binaryMessenger ?: return

    MethodChannel(messenger, MyContentProvider.CHANNELKEY).setMethodCallHandler { call, result ->
        if (call.method == "sendData") {

            var currentAppCursor = contentResolver.query(
                MyContentProvider.CURRENTAPP_URI,
                null,
                null,
                null,
                null
            )
            var otherAppCursor = contentResolver.query(
                MyContentProvider.OTHERAPP_URI,
                null,
                null,
                null,
                null
            )
            var username = call.arguments as String

            try {
                var contentValues = ContentValues()
                if (otherAppCursor != null && otherAppCursor.count > 0) {
                    while (otherAppCursor.moveToNext()) {
                        var otherAppUsername =
                            otherAppCursor.getString(otherAppCursor.getColumnIndex("name"))
                        contentValues.put(
                            MyContentProvider.name,
                            otherAppUsername
                        )
                        username = otherAppUsername
                        if (currentAppCursor != null) {
                            if (currentAppCursor.count > 0) {
                                contentResolver.update(
                                    MyContentProvider.CURRENTAPP_URI,
                                    contentValues,
                                    null,
                                    null
                                )
                            } else {
                                contentResolver.insert(
                                    MyContentProvider.CURRENTAPP_URI,
                                    contentValues
                                )
                            }
                        }
                    }
                } else {
                    if (currentAppCursor != null) {
                        if (currentAppCursor.count > 0) {
                            while (currentAppCursor.moveToNext()) {
                                var currentAppUsername = currentAppCursor.getString(
                                    currentAppCursor.getColumnIndex("name")
                                )
                                username = currentAppUsername
                            }
                        } else {
                            contentValues.put(MyContentProvider.name, username)
                            contentResolver.insert(
                                MyContentProvider.CURRENTAPP_URI,
                                contentValues
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                result.error("ERROR_CODE", "Error occurred: ${e.message}", null)
            } finally {
                currentAppCursor?.close()
                otherAppCursor?.close()
            }
            result.success(username)
        } else {
            result.notImplemented()
        }
    }
}
}

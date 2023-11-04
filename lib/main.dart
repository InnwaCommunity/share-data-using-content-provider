import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:developer';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Data Sender'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String text = '';
  TextEditingController datacontroller = TextEditingController();

  Future<void> _sendData(String data) async {
    const platform = MethodChannel('com.example.data_channel');
    try {
      await platform.invokeMethod('sendData', data);
    } on PlatformException catch (e) {
      log('Sending data error : ${e.toString()}');
    }
  }

  Future<void> _receive() async {
    const platform = MethodChannel('com.example.data_channel');
    try {
      dynamic result = await platform.invokeMethod('receiveData');
      if (result != 'empty') {
        text = result.toString();
        setState(() {});
      }
    } on PlatformException catch (e) {
      log('Sending data error : ${e.toString()}');
    }
  }

  @override
  void initState() {
    super.initState();
    _receive();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
  appBar: AppBar(
    backgroundColor: Theme.of(context).colorScheme.inversePrimary,
    title: Text(widget.title),
  ),
  body: Column(
    mainAxisAlignment: MainAxisAlignment.spaceBetween,
    children: <Widget>[
      Expanded(
        child: SingleChildScrollView(
          child: Column(
            children: [
              const Text(
                'Shared Data',
                style: TextStyle(fontSize: 20),
                textAlign: TextAlign.center,
              ),
              Text(
                text,
                style: Theme.of(context).textTheme.titleLarge,
              ),
            ],
          ),
        ),
      ),
      Container(
        alignment: Alignment.bottomCenter,
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 8.0),
              child: SizedBox(
                width: double.infinity,
                child: TextField(
                  controller: datacontroller,
                  decoration: InputDecoration(
                    border: const UnderlineInputBorder(),
                    labelText: 'Share Data',
                    labelStyle: TextStyle(
                      fontSize: 16,
                      color: Theme.of(context).colorScheme.secondaryContainer,
                    ),
                    hintText: 'Enter data that you want to share',
                    hintStyle: const TextStyle(
                      fontSize: 16,
                    ),
                  ),
                ),
              ),
            ),
            SizedBox(
              width: 130,
              child: ElevatedButton(
                onPressed: () {
                  if (datacontroller.text.isNotEmpty) {
                    _sendData(datacontroller.text).then((value){
                       _receive();
                       datacontroller.clear();
                    });
                  }
                },
                style: ElevatedButton.styleFrom(
                  minimumSize: const Size(130, 44),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(22.0),
                  ),
                ),
                child: const Text(
                  'Send',
                  style: TextStyle(
                    fontSize: 14,
                    fontFamily: 'Roboto',
                    fontWeight: FontWeight.w400,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    ],
  ),
);
  }
}

import 'dart:convert';

import 'package:encreq/entity.dart';
import 'package:encreq/random.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'encreq',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'encreq'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePage createState() => _MyHomePage();
}

class _MyHomePage extends State<MyHomePage> {
  _MyHomePage();

  String publicKey = "";
  String aesKey = "";
  String aesIv = "";
  String content = "";
  String gateway = "http://192.168.2.212:12333/wnc";

  _requestPk() async {
    FocusManager.instance.primaryFocus.unfocus();
    EntityManager entityManager = EntityManager.getInstance();
    entityManager.setupGateway(this.gateway);
    var r = await entityManager.loadKey();
    if (r.errorCode == 0) {
      String pk = r.data.toString();
      this.setState(() {
        this.publicKey = pk;
      });
    } else {
      if (r.errorMsg.isEmpty) {
        _showResult("获取秘钥出错");
      } else {
        _showResult(r.errorMsg);
      }
    }
  }

  _upload() async {
    FocusManager.instance.primaryFocus.unfocus();
    if (this.aesKey.isEmpty || this.aesIv.isEmpty) {
      _showResult("请先生成加密秘钥");
      return;
    }
    if (this.publicKey.isEmpty) {
      _showResult("请先获取公钥");
      return;
    }
    if (this.content.isEmpty) {
      _showResult("请输入需要加密的内容");
      return;
    }
    try {
      var entityManager = EntityManager.getInstance();
      entityManager.setupGateway(this.gateway);
      final key = entityManager.encryptRSA(this.publicKey, this.aesKey);
      final iv = entityManager.encryptRSA(this.publicKey, this.aesIv);
      final body =
          entityManager.encryptAES(this.aesKey, this.aesIv, this.content);
      var r = await entityManager.upload(key, iv, body);
      if (r.data != null) {
        _showResult(r.data.toString());
      } else {
        if (r.errorMsg.isNotEmpty) {
          _showResult(r.errorMsg);
        } else {
          _showResult("上传失败");
        }
      }
    } on Exception {
      _showResult("上传失败");
    }
  }

  _createKey() async {
    String iv = RandomUtils.random(16);
    String key = RandomUtils.random(32);
    this.setState(() {
      this.aesIv = iv;
      this.aesKey = key;
    });
    FocusManager.instance.primaryFocus.unfocus();
  }

  Future<int> _showResult(String result) async {
    return showDialog<int>(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text("结果"),
            contentPadding: EdgeInsets.all(8.0),
            content: SingleChildScrollView(
              child: Text(result),
            ),
            actions: [
              FlatButton(
                child: Text("确定"),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text('encreq'),
      ),
      body: GestureDetector(
        onTap: () {
          FocusManager.instance.primaryFocus.unfocus();
        },
        child: SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.all(8),
            child: Column(
              children: [
                Padding(
                  padding: EdgeInsets.all(10),
                ),
                TextField(
                  controller: TextEditingController(text: this.gateway),
                  keyboardType: TextInputType.text,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: '网关',
                  ),
                  onChanged: (val) {
                    setState(() {
                      this.gateway = val;
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.all(10),
                ),
                TextField(
                  readOnly: true,
                  controller: TextEditingController(text: this.aesKey),
                  keyboardType: TextInputType.text,
                  maxLength: 32,
                  maxLengthEnforced: true,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: 'AES秘钥',
                  ),
                  onChanged: (val) {
                    setState(() {
                      this.aesKey = val;
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.all(10),
                ),
                TextField(
                  readOnly: true,
                  controller: TextEditingController(text: this.aesIv),
                  keyboardType: TextInputType.text,
                  maxLength: 16,
                  maxLengthEnforced: true,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: 'AES向量',
                  ),
                  onChanged: (val) {
                    setState(() {
                      this.aesIv = val;
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.all(10),
                ),
                TextField(
                  controller: TextEditingController(text: this.publicKey),
                  readOnly: true,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: 'RSA公钥',
                  ),
                  onChanged: (val) {
                    setState(() {
                      this.publicKey = val;
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.all(10),
                ),
                TextField(
                  controller: TextEditingController(text: this.content),
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: '加密内容',
                  ),
                  onChanged: (val) {
                    setState(() {
                      this.content = val;
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.all(10),
                ),
                Row(
                  children: [
                    FlatButton(
                      child: Text("获取公钥"),
                      onPressed: _requestPk,
                    ),
                    FlatButton(
                      child: Text("生成秘钥"),
                      onPressed: _createKey,
                    ),
                    FlatButton(
                      child: Text("发送数据"),
                      onPressed: _upload,
                    )
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

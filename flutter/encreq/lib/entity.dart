import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';
import 'package:encrypt/encrypt.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:rsa_encrypt/rsa_encrypt.dart';

@JsonSerializable()
class ResultSet {
  int errorCode = -1;
  String errorMsg = "";
  dynamic data;

  ResultSet({this.errorCode, this.errorMsg, this.data});

  ResultSet.fromJson(Map<String, dynamic> m) {
    this.errorCode = m["errorCode"];
    this.errorMsg = m["errorMsg"];
    this.data = m["data"];
  }

  Map<String, dynamic> map() {
    Map<String, dynamic> m = Map<String, dynamic>();
    m["errorCode"] = errorCode;
    m["errorMsg"] = errorMsg;
    if (data != null) {
      m["data"] = data;
    }
    return m;
  }
}

class EntityManager {
  String _gateway = "";

  void setupGateway(String gateway) {
    this._gateway = gateway;
  }

  static EntityManager _entityManager = EntityManager();

  static EntityManager getInstance() {
    return _entityManager;
  }

  Future<ResultSet> upload(String key, String iv, String body) async {
    try {
      var json = await _upload(
          "/upload", Map.from({"key": key, "iv": iv, "body": body}));
      var map = jsonDecode(json);
      return ResultSet.fromJson(map);
    } on Exception {
      return ResultSet(errorMsg: "网络错误", errorCode: -1, data: null);
    }
  }

  String encryptRSA(String publicKey, String content) {
    try {
      final bytes = Uint8List.fromList(content.codeUnits);
      var helper = RsaKeyHelper();
      final key = helper.parsePublicKeyFromPem(publicKey);
      var rsa = RSA(publicKey: key);
      var encrypted = rsa.encrypt(bytes);
      return Base64Encoder().convert(encrypted.bytes);
    } on Exception {}
    return "";
  }

  String encryptAES(String key, String iv, String content) {
    try {
      final k = Key.fromUtf8(key);
      final i = IV.fromUtf8(iv);
      final encryptor = Encrypter(AES(k, mode: AESMode.cbc));
      final encrypted = encryptor.encrypt(content, iv: i);
      return Base64Encoder().convert(encrypted.bytes);
    } on Exception {}
    return "";
  }

  Future<ResultSet> loadKey() async {
    try {
      var json = await _loadString("/keys");
      var map = jsonDecode(json);
      return ResultSet.fromJson(map);
    } on Exception catch(e) {
      return ResultSet(errorMsg: e.toString(), errorCode: -1, data: null);
    }
  }

  Future<String> _loadString(String context) async {
    var string="";
    var httpClient = HttpClient();
    var request = await httpClient.getUrl(Uri.parse(_gateway + context));
    var resp = await request.close();
    if (resp.statusCode == HttpStatus.ok) {
      string = await resp.transform(utf8.decoder).join();
    }
    return string;
  }

  Future<String> _upload(String context, Map params) async {
    var string="";
    var httpClient = HttpClient();
    var request = await httpClient.postUrl(Uri.parse(_gateway + context));
    request.headers.set('content-type', 'application/json');
    request.add(utf8.encode(json.encode(params)));
    var resp = await request.close();
    if (resp.statusCode == HttpStatus.ok) {
      string = await resp.transform(utf8.decoder).join();
    }
    return string;
  }
}

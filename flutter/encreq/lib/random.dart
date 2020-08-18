import 'dart:math';

class RandomUtils {
  static String random(int length) {
    Random r = Random.secure();
    List<int> list = List();
    for (int index = 0; index < length; index++) {
      int min = 32;//空格
      int max = 127;//~
      int offset = max - min - 1;
      int c = min + r.nextInt(offset);
      list.add(c);
    }
    return String.fromCharCodes(list);
  }
}

package eu.koboo.fakeformat.test;

import eu.koboo.fakeformat.FakeArray;
import eu.koboo.fakeformat.FakeObject;
import eu.koboo.fakeformat.FakeParser;
import java.util.UUID;

public class SimpleTest {

  public static void main(String[] args) {
    FakeObject fakeObject = new FakeObject();

    fakeObject.set("test", 100);
    fakeObject.set("second", "This is another test");

    FakeObject innerObject = new FakeObject();
    innerObject.set("inner", 1);
    innerObject.set("second_inner", UUID.randomUUID());
    innerObject.set("null_check", null);

    fakeObject.set("inner_object", innerObject);

    FakeArray fakeArray = new FakeArray();
    fakeArray.add("This is a test");
    fakeArray.add(200);
    fakeArray.add(System.currentTimeMillis());
    fakeArray.add(UUID.randomUUID());

    fakeObject.set("test_array", fakeArray);

    String content = fakeObject.toString();

    System.out.println(content);

    FakeObject object2 = FakeParser.parse(content);

    for(String key : object2.getKeys()) {
      System.out.println("key: " + object2.get(key).toString());
    }
  }

}

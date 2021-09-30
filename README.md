# FakeFormat

This is a very small and simple framework which is based on the json format.

The file size of the complete framework is smaller than the file size of json-simple.

``12 kB`` (fake-format) < ``23kB`` (json-simple)

## Special thanks

A special thanks goes to [stleary](https://github.com/stleary), who wrote the parsing logic for his own [project](https://github.com/stleary/JSON-java).

## Usage

````java

public class SomeClass {

  public static void main(String[] args) {
    // Create a new FakeObject instance
    FakeObject fakeObject = new FakeObject();

    // Set objects by key - value
    fakeObject.set("key", "value");
    fakeObject.set("this_is_a_string", "This_can_be_anything_primitive");

    // Create a new inner FakeObject to stack into the root FakeObject 
    FakeObject innerObject = new FakeObject();
    innerObject.set("key", "This_is_a_inner_object");

    // You can also set FakeObjects into FakeObjects
    fakeObject.set("inner_object", innerObject);

    // Create a new FakeArray instance
    FakeArray fakeArray = new FakeArray();
    fakeArray.add("this_is_a_array");
    fakeArray.add("with_multiple_primitive_objects");
    fakeArray.add(200);
    fakeArray.add(System.currentTimeMillis());
    fakeArray.add(UUID.randomUUID());

    // You can set FakeArrays into FakeObjects
    fakeObject.set("key_of_array", fakeArray);

    // Get the whole object as JSON-String
    String objectAsJsonString = fakeObject.toString();

    // Parse any JSON-String back to a FakeObject
    FakeObject parsedFromString = FakeParser.parse(content);
  }

}

````
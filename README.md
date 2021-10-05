# FakeFormat

This is a very small, simple-to-understand and lightweight framework, which is based on the json format.

## Why FakeFormat and not another library?

* Very simple calls
* Small file-size (only ``12kB``)
* No dependencies
* Free-to-use
* RFC 8259 compatible

## Dependency

A dd the dependency to ``build.gradle`` 

```groovy
repositories {
  // Artifact repository  
  maven {
    url 'https://repo.koboo.eu/releases'
  }
}

dependencies {
  // Dependency declaration  
  implementation 'eu.koboo:fake-format:1.0.3'
}
```

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

    // Parse any JSON-String back to any FakeObject
    fakeObject = FakeParser.parse(content);
    
    // Get any value with get(String key)
    String value = fakeObject.get("key").getAsString();
    String thisCanBeAnyPrimitive = fakeObject.get("this_is_a_string").getAsString();
    
    // You can also get FakeObjects
    FakeObject setInnerObject = fakeObject.get("inner_object").getAsFakeObject();
    
    String innerValue = setInnerObject.get("key").getAsString();
    
    // Or get directly FakeArrays
    FakeArray setArray = fakeObject.get("key_of_array").getAsFakeArray();
    
    for(FakeElement element : setArray) {
      System.out.println(element.toString());
    }
  }

}

````
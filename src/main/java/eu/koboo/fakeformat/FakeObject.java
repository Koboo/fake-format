package eu.koboo.fakeformat;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class FakeObject extends FakeElement {

  private final Map<String, FakeElement> fakeElementMap;

  public FakeObject() {
    this.fakeElementMap = new HashMap<>();
  }

  public FakeObject(Map<String, FakeElement> fakeElementMap) {
    this.fakeElementMap = fakeElementMap;
  }

  public FakeObject set(String key, Object object) {
    FakeElement fakeElement = null;
    if(object instanceof FakeElement) {
      fakeElement = (FakeElement) object;
    } else
    if(Primitive.isPrimitive(object)) {
      fakeElement = new FakeElement(object);
    }
    this.fakeElementMap.put(key, fakeElement);
    return this;
  }

  public <T extends FakeElement> T get(String key) {
    return (T) this.fakeElementMap.get(key);
  }

  public <T extends FakeElement> T getOr(String key, T defaultValue) {
    T element = get(key);
    return element != null ? element : defaultValue;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(FakeFormat.getInstance().getOpenBracket());
    int size = this.fakeElementMap.size();
    for(Map.Entry<String, FakeElement> entry : this.fakeElementMap.entrySet()) {
      size -= 1;
      String key = entry.getKey();
      String objectString = entry.getValue().toString();
      if(objectString == null) {
        continue;
      }
      builder.append("\"").append(key).append("\"").append(FakeFormat.getInstance().getKeyValueSeparator()).append(objectString);
      if(size > 0) {
        builder.append(FakeFormat.getInstance().getEntrySeparator());
      }
    }
    builder.append(FakeFormat.getInstance().getCloseBracket());
    return builder.toString();
  }
}

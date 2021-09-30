package eu.koboo.fakeformat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    if (key == null) {
      throw new NullPointerException("The key is null!");
    }
    if (object == null) {
      throw new NullPointerException("The object with the key \"" + key + "\" is null");
    }
    FakeElement fakeElement = null;
    if (object instanceof FakeElement) {
      fakeElement = (FakeElement) object;
    } else if (FakeElement.isPrimitive(object)) {
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

  public boolean hasKey(String key) {
    return this.fakeElementMap.containsKey(key);
  }

  public boolean hasKeys(String... keys) {
    return hasKeys(Arrays.asList(keys));
  }

  public boolean hasKeys(List<String> keyList) {
    for (String key : keyList) {
      if (!this.fakeElementMap.containsKey(key)) {
        return false;
      }
    }
    return true;
  }

  public Set<String> getKeys() {
    return this.fakeElementMap.keySet();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    int size = this.fakeElementMap.size();
    for (Map.Entry<String, FakeElement> entry : this.fakeElementMap.entrySet()) {
      size -= 1;
      String key = entry.getKey();
      String objectString = entry.getValue().toString();
      if (objectString == null) {
        continue;
      }
      builder.append("\"").append(key).append("\"").append(":").append(objectString);
      if (size > 0) {
        builder.append(",");
      }
    }
    builder.append("}");
    return builder.toString();
  }
}

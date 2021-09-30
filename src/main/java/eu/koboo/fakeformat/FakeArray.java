package eu.koboo.fakeformat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FakeArray extends FakeElement implements Iterable<FakeElement> {

  private final List<FakeElement> fakeElementList;

  public FakeArray() {
    this.fakeElementList = new ArrayList<>();
  }

  public FakeArray(List<FakeElement> fakeElementList) {
    this.fakeElementList = fakeElementList;
  }

  public int size() {
    return this.fakeElementList.size();
  }

  public boolean isEmpty() {
    return this.fakeElementList.isEmpty();
  }

  public boolean contains(Object object) {
    return this.fakeElementList.contains(object);
  }

  @Override
  public Iterator<FakeElement> iterator() {
    return this.fakeElementList.iterator();
  }

  public void add(Object object) {
    if(object == null) {
      return;
    }
    FakeElement element = null;
    if(object instanceof FakeElement) {
      element = (FakeElement) object;
    } else if(Primitive.isPrimitive(object)) {
      element = new FakeElement(object);
    }
    if(element == null) {
      return;
    }
    this.fakeElementList.add(element);
  }

  public boolean remove(FakeElement fakeElement) {
    return this.fakeElementList.remove(fakeElement);
  }

  public boolean addAll(Collection<? extends FakeElement> c) {
    return this.fakeElementList.addAll(c);
  }

  public void clear() {
    this.fakeElementList.clear();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    int size = this.fakeElementList.size();
    for (FakeElement element : this.fakeElementList) {
      size -= 1;
      builder.append(element.toString());
      if (size > 0) {
        builder.append(",");
      }
    }
    builder.append("]");
    return builder.toString();
  }
}

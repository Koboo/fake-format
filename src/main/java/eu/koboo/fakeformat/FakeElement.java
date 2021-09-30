package eu.koboo.fakeformat;

import java.util.UUID;

public class FakeElement {

  private final Object object;

  protected FakeElement() {
    this.object = null;
  }

  protected FakeElement(Object object) {
    this.object = object;
  }

  private boolean isObjectNull() {
    return object == null;
  }

  public double getAsDouble() {
    return !isObjectNull() ? (double) object : 0;
  }

  public int getAsInt() {
    return !isObjectNull() ? (int) object : 0;
  }

  public float getAsFloat() {
    return !isObjectNull() ? (float) object : 0;
  }

  public short getAsShort() {
    return !isObjectNull() ? (short) object : 0;
  }

  public long getAsLong() {
    return !isObjectNull() ? (long) object : 0;
  }

  public boolean getAsBoolean() {
    return !isObjectNull() && (boolean) object;
  }

  public byte getAsByte() {
    return !isObjectNull() ? (byte) object : 0;
  }

  public char getAsChar() {
    return !isObjectNull() ? (char) object : 0;
  }

  public String getAsString() {
    return !isObjectNull() ? (String) object : null;
  }

  public UUID getAsUUID() {
    return !isObjectNull() ? (UUID) object : null;
  }

  @Override
  public String toString() {
    if(isObjectNull()) {
      return null;
    }
    if(object instanceof String || object instanceof UUID) {
      return "\"" + object + "\"";
    }
    return String.valueOf(object);
  }
}

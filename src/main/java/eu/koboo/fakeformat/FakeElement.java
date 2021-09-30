package eu.koboo.fakeformat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FakeElement {

  public static final FakeElement NULL = new FakeElement();

  private static final Set<Class<?>> WRAPPER_TYPES = new HashSet(Arrays.asList(
      Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
      Float.class, Double.class));

  public static boolean isPrimitive(Object object) {
    return object != null && (object.getClass().isPrimitive() || WRAPPER_TYPES.contains(object.getClass()));
  }

  private final Object object;

  protected FakeElement() {
    this.object = null;
  }

  protected FakeElement(Object object) {
    this.object = object;
  }

  private boolean hasValue() {
    return object != null;
  }

  public double getAsDouble() {
    return hasValue() ? (double) object : 0;
  }

  public int getAsInt() {
    return hasValue() ? (int) object : 0;
  }

  public float getAsFloat() {
    return hasValue() ? (float) object : 0;
  }

  public short getAsShort() {
    return hasValue() ? (short) object : 0;
  }

  public long getAsLong() {
    return hasValue() ? (long) object : 0;
  }

  public boolean getAsBoolean() {
    return hasValue() && (boolean) object;
  }

  public byte getAsByte() {
    return hasValue() ? (byte) object : 0;
  }

  public char getAsChar() {
    return hasValue() ? (char) object : 0;
  }

  public String getAsString() {
    return hasValue() ? (String) object : null;
  }

  public UUID getAsUUID() {
    return hasValue() ? (UUID) object : null;
  }

  @Override
  public String toString() {
    if (!hasValue()) {
      return null;
    }
    if (object instanceof String || object instanceof UUID) {
      return "\"" + object + "\"";
    }
    return String.valueOf(object);
  }
}

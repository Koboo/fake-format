package eu.koboo.fakeformat;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public enum Primitive {

  DOUBLE(Double.class, double.class),
  INTEGER(Integer.class, int.class),
  FLOAT(Float.class, float.class),
  SHORT(Short.class, short.class),
  LONG(Long.class, long.class),
  BOOLEAN(Boolean.class, boolean.class),
  BYTE(Byte.class, byte.class),
  CHAR(Character.class, char.class),
  STRING(String.class, String.class),
  UUID(UUID.class, UUID.class);

  private static final Primitive[] PRIMITIVES = Primitive.values();
  private static final Map<String, Primitive> PRIMITIVE_REGISTRY = new ConcurrentHashMap<>();

  static {
    for (Primitive primitive : PRIMITIVES) {
      PRIMITIVE_REGISTRY.put(primitive.primitiveClass.getSimpleName(), primitive);
      PRIMITIVE_REGISTRY.put(primitive.nativeClass.getSimpleName(), primitive);
    }
  }

  private final Class<?> primitiveClass;
  private final Class<?> nativeClass;

  Primitive(Class<?> primitiveClass, Class<?> nativeClass) {
    this.primitiveClass = primitiveClass;
    this.nativeClass = nativeClass;
  }

  public static boolean isPrimitive(Object object) {
    return PRIMITIVE_REGISTRY.get(object.getClass().getSimpleName()) != null;
  }

  public static Primitive of(Object object) {
    return PRIMITIVE_REGISTRY.get(object.getClass().getSimpleName());
  }

  public static Primitive[] getValues() {
    return PRIMITIVES;
  }
}

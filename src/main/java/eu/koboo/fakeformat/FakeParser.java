package eu.koboo.fakeformat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public class FakeParser {

  @SuppressWarnings("unchecked")
  public static <T extends FakeElement> T parse(String content) {
    return (T) parseObject(new FakeCursor(content));
  }

  protected static FakeObject parseObject(FakeCursor fakeCursor) {
    FakeObject fakeObject = new FakeObject();

    char currentChar;
    String key;

    if (fakeCursor.nextClean() != '{') {
      throw fakeCursor.syntaxError("Text needs to begin with '{'");
    }
    for (; ; ) {
      currentChar = fakeCursor.nextClean();
      switch (currentChar) {
        case 0:
          throw fakeCursor.syntaxError("Text needs to end with '}'");
        case '}':
          return fakeObject;
        default:
          fakeCursor.back();
          key = fakeCursor.nextValue().toString();
      }

      // The key is followed by ':'.

      currentChar = fakeCursor.nextClean();
      if (currentChar != ':') {
        throw fakeCursor.syntaxError("Expected a ':' after a key");
      }

      // Use syntaxError(..) to include error location

      if (key != null) {
        // Check if key exists
        if (fakeObject.has(key)) {
          // key already exists
          throw fakeCursor.syntaxError("Duplicate key \"" + key + "\"");
        }
        // Only add value if non-null
        Object value = fakeCursor.nextValue();
        if (value != null) {
          fakeObject.set(key, value);
        }
      }

      // Pairs are separated by ','.

      switch (fakeCursor.nextClean()) {
        case ';':
        case ',':
          if (fakeCursor.nextClean() == '}') {
            return fakeObject;
          }
          fakeCursor.back();
          break;
        case '}':
          return fakeObject;
        default:
          throw fakeCursor.syntaxError("Expected a ',' or '}'");
      }
    }
  }

  protected static FakeArray parseArray(FakeCursor cursor) {
    FakeArray fakeArray = new FakeArray();
    if (cursor.nextClean() != '[') {
      throw cursor.syntaxError("Text needs to start with '['");
    }

    char nextChar = cursor.nextClean();
    if (nextChar == 0) {
      // array is unclosed. No ']' found, instead EOF
      throw cursor.syntaxError("Expected a ',' or ']'");
    }
    if (nextChar != ']') {
      cursor.back();
      for (;;) {
        if (cursor.nextClean() == ',') {
          cursor.back();
          fakeArray.add(FakeElement.NULL);
        } else {
          cursor.back();
          fakeArray.add(cursor.nextValue());
        }
        switch (cursor.nextClean()) {
          case 0:
            throw cursor.syntaxError("Expected a ',' or ']'");
          case ',':
            nextChar = cursor.nextClean();
            if (nextChar == 0) {
              throw cursor.syntaxError("Expected a ',' or ']'");
            }
            if (nextChar == ']') {
              return fakeArray;
            }
            cursor.back();
            break;
          case ']':
            return fakeArray;
          default:
            throw cursor.syntaxError("Expected a ',' or ']'");
        }
      }
    }
    return fakeArray;
  }

  protected static FakeElement parseElement(String elementString) {
    if (elementString == null || elementString.equalsIgnoreCase("")
        || elementString.equalsIgnoreCase("null")) {
      return FakeElement.NULL;
    }
    if (elementString.equalsIgnoreCase("true")) {
      return new FakeElement(true);
    }
    if (elementString.equalsIgnoreCase("false")) {
      return new FakeElement(false);
    }
    char initialChar = elementString.charAt(0);
    if ((initialChar >= '0' && initialChar <= '9') || initialChar == '-') {
      try {
        return new FakeElement(parseNumber(elementString));
      } catch (NumberFormatException ignore) {
      }
    }
    if (elementString.contains("-") && elementString.length() == 36) {
      try {
        return new FakeElement(UUID.fromString(elementString));
      } catch (IllegalArgumentException ignore) {
      }
    }
    return new FakeElement(elementString);
  }

  private static Number parseNumber(final String val) throws NumberFormatException {
    char initial = val.charAt(0);
    if ((initial >= '0' && initial <= '9') || initial == '-') {
      if (val.indexOf('.') > -1 || val.indexOf('e') > -1
          || val.indexOf('E') > -1 || "-0".equals(val)) {
        try {
          BigDecimal bd = new BigDecimal(val);
          if (initial == '-' && BigDecimal.ZERO.compareTo(bd) == 0) {
            return -0.0;
          }
          return bd;
        } catch (NumberFormatException retryAsDouble) {
          try {
            Double d = Double.valueOf(val);
            if (d.isNaN() || d.isInfinite()) {
              throw new NumberFormatException("val [" + val + "] is not a valid number.");
            }
            return d;
          } catch (NumberFormatException ignore) {
            throw new NumberFormatException("val [" + val + "] is not a valid number.");
          }
        }
      }
      if (initial == '0' && val.length() > 1) {
        char at1 = val.charAt(1);
        if (at1 >= '0' && at1 <= '9') {
          throw new NumberFormatException("val [" + val + "] is not a valid number.");
        }
      } else if (initial == '-' && val.length() > 2) {
        char at1 = val.charAt(1);
        char at2 = val.charAt(2);
        if (at1 == '0' && at2 >= '0' && at2 <= '9') {
          throw new NumberFormatException("val [" + val + "] is not a valid number.");
        }
      }
      BigInteger bi = new BigInteger(val);
      if (bi.bitLength() <= 31) {
        return bi.intValue();
      }
      if (bi.bitLength() <= 63) {
        return bi.longValue();
      }
      return bi;
    }
    throw new NumberFormatException("val [" + val + "] is not a valid number.");
  }
}

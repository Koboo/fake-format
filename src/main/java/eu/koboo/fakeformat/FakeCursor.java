package eu.koboo.fakeformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class FakeCursor {

  private long character;
  private boolean eof;
  private long index;
  private long line;
  private char previous;
  private final Reader reader;
  private boolean usePrevious;
  private long characterPreviousLine;

  protected FakeCursor(String string) {
    StringReader stringReader = new StringReader(string);
    this.reader = stringReader.markSupported()
        ? stringReader
        : new BufferedReader(stringReader);
    this.eof = false;
    this.usePrevious = false;
    this.previous = 0;
    this.index = 0;
    this.character = 1;
    this.characterPreviousLine = 0;
    this.line = 1;
  }

  public void back() {
    if (this.usePrevious || this.index <= 0) {
      throw new IllegalStateException("Stepping back two steps is not supported");
    }
    this.decrementIndexes();
    this.usePrevious = true;
    this.eof = false;
  }

  private void decrementIndexes() {
    this.index--;
    if (this.previous == '\r' || this.previous == '\n') {
      this.line--;
      this.character = this.characterPreviousLine;
    } else if (this.character > 0) {
      this.character--;
    }
  }

  public boolean end() {
    return this.eof && !this.usePrevious;
  }

  public char next() {
    int c;
    if (this.usePrevious) {
      this.usePrevious = false;
      c = this.previous;
    } else {
      try {
        c = this.reader.read();
      } catch (IOException exception) {
        throw new IllegalArgumentException(exception);
      }
    }
    if (c <= 0) {
      this.eof = true;
      return 0;
    }
    this.incrementIndexes(c);
    this.previous = (char) c;
    return this.previous;
  }

  private void incrementIndexes(int c) {
    if (c > 0) {
      this.index++;
      if (c == '\r') {
        this.line++;
        this.characterPreviousLine = this.character;
        this.character = 0;
      } else if (c == '\n') {
        if (this.previous != '\r') {
          this.line++;
          this.characterPreviousLine = this.character;
        }
        this.character = 0;
      } else {
        this.character++;
      }
    }
  }

  public String next(int n) {
    if (n == 0) {
      return "";
    }

    char[] chars = new char[n];
    int pos = 0;

    while (pos < n) {
      chars[pos] = this.next();
      if (this.end()) {
        throw this.syntaxError("Substring bounds error");
      }
      pos += 1;
    }
    return new String(chars);
  }

  public char nextClean() {
    for (; ; ) {
      char c = this.next();
      if (c == 0 || c > ' ') {
        return c;
      }
    }
  }

  public String nextString(char quote) {
    char c;
    StringBuilder sb = new StringBuilder();
    for (; ; ) {
      c = this.next();
      switch (c) {
        case 0:
        case '\n':
        case '\r':
          throw this.syntaxError("Unterminated string");
        case '\\':
          c = this.next();
          switch (c) {
            case 'b':
              sb.append('\b');
              break;
            case 't':
              sb.append('\t');
              break;
            case 'n':
              sb.append('\n');
              break;
            case 'f':
              sb.append('\f');
              break;
            case 'r':
              sb.append('\r');
              break;
            case 'u':
              try {
                sb.append((char) Integer.parseInt(this.next(4), 16));
              } catch (NumberFormatException e) {
                throw this.syntaxError("Illegal escape.", e);
              }
              break;
            case '"':
            case '\'':
            case '\\':
            case '/':
              sb.append(c);
              break;
            default:
              throw this.syntaxError("Illegal escape.");
          }
          break;
        default:
          if (c == quote) {
            return sb.toString();
          }
          sb.append(c);
      }
    }
  }

  public Object nextValue() {
    char c = this.nextClean();
    String string;

    switch (c) {
      case '"':
      case '\'':
        return this.nextString(c);
      case '{':
        this.back();
        return FakeParser.parseObject(this);
      case '[':
        this.back();
        return FakeParser.parseArray(this);
    }

    StringBuilder sb = new StringBuilder();
    while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
      sb.append(c);
      c = this.next();
    }
    if (!this.eof) {
      this.back();
    }

    string = sb.toString().trim();
    if ("".equals(string)) {
      throw this.syntaxError("Missing value");
    }
    return FakeParser.parseElement(string);
  }

  public IllegalArgumentException syntaxError(String message) {
    return new IllegalArgumentException(message + this);
  }

  public IllegalArgumentException syntaxError(String message, Throwable causedBy) {
    return new IllegalArgumentException(message + this, causedBy);
  }

  @Override
  public String toString() {
    return " at " + this.index + " [character " + this.character + " line " +
        this.line + "]";
  }
}
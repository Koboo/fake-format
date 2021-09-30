package eu.koboo.fakeformat;

public class FakeFormat {

  private static final FakeFormat FAKE_FORMAT = new FakeFormat();

  public static FakeFormat getInstance() {
    return FAKE_FORMAT;
  }

  private final char openBracket;
  private final char closeBracket;
  private final char keyValueSeparator;
  private final char openArray;
  private final char closeArray;
  private final char entrySeparator;

  private FakeFormat() {
    this.openBracket = '{';
    this.closeBracket = '}';
    this.keyValueSeparator = ':';
    this.openArray = '[';
    this.closeArray = ']';
    this.entrySeparator = ',';
  }

  public char getOpenBracket() {
    return openBracket;
  }

  public char getCloseBracket() {
    return closeBracket;
  }

  public char getKeyValueSeparator() {
    return keyValueSeparator;
  }

  public char getOpenArray() {
    return openArray;
  }

  public char getCloseArray() {
    return closeArray;
  }

  public char getEntrySeparator() {
    return entrySeparator;
  }
}

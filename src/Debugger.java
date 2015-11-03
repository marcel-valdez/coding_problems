package debug;

public final class Debugger {
  public static final Debugger DEBUG = new Debugger();

  private boolean enabled = false;

  private Debugger() { }

  public <T> T println(T obj) {
    if(enabled) {
      System.out.println(obj + "");
    }

    return obj;
  }

  public <T> T print(T obj) {
    if(enabled) {
      System.out.print(obj + "");
    }

    return obj;
  }

  public String println(String msg) {
    if(enabled) {
      System.out.println(msg);
    }

    return msg;
  }

  public String print(String msg) {
    if(enabled) {
      System.out.print(msg);
    }

    return msg;
  }

  public void enable() { this.enabled = true; }
  public void disable() { this.enabled = false; }
}

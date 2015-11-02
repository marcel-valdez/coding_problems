package debug;

public final class Debugger {
  public static final Debugger DEBUG = new Debugger();

  private boolean enabled = false;

  private Debugger() { }

  public void println(Object obj) { if(enabled) System.out.println(obj + ""); }
  public void print(Object obj) { if(enabled) System.out.print(obj + ""); }

  public void println(String msg) { if(enabled) System.out.println(msg); }
  public void print(String msg) { if(enabled) System.out.print(msg); }

  public void enable() { this.enabled = true; }
  public void disable() { this.enabled = false; }
}

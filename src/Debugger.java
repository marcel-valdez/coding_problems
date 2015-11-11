package debug;

public final class Debugger {
  public static final Debugger DEBUG = new Debugger();

  private volatile boolean enabled = false;

  private Debugger() { }

  public <T> T println(T obj) {
    if(enabled) {
      System.out.println(obj + "");
    }

    return obj;
  }

  public void println(String format, Object arg) {
    if(!enabled) { return; }

    println(format, new Object[] { arg });
  }

  public void println(String format, Object arg1, Object arg2) {
    if(!enabled) { return; }

    println(format,  new Object[] { arg1, arg2 });
  }

  public void println(String format, Object arg1, Object arg2, Object arg3) {
    if(!enabled) { return; }

    println(format,  new Object[] { arg1, arg2, arg3 });
  }

  public void println(String format, Object arg1, Object arg2, Object arg3, Object arg4) {
    if(!enabled) { return; }

    println(format,  new Object[] { arg1, arg2, arg3, arg4 });
  }

  public void println(String format, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
    if(!enabled) { return; }

    println(format,  new Object[] { arg1, arg2, arg3, arg4, arg5 });
  }

  public void println(String format, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
    if(!enabled) { return; }

    println(format,  new Object[] { arg1, arg2, arg3, arg4, arg5, arg6 });
  }

  public void println(String format, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
    if(!enabled) { return; }

    println(format,  new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7 });
  }


  public void println(String format, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object ... args) {
    if(!enabled) { return; }

    if(args == null) {
      args = new Object[] { null };
    }

    Object[] allArgs = new Object[7 + args.length];
    allArgs[0] = arg1;
    allArgs[1] = arg2;
    allArgs[2] = arg3;
    allArgs[3] = arg4;
    allArgs[4] = arg5;
    allArgs[5] = arg6;
    allArgs[6] = arg7;

    if(args.length > 0) {
      System.arraycopy(args, 0, allArgs, 7, args.length);
    }

    println(format,  allArgs);
  }

  private void println(String format, Object ... args) {
    if(enabled) {
      System.out.println(String.format(format, args));
    }
  }

  public <T> T print(T obj) {
    if(enabled) {
      System.out.print(obj + "");
    }

    return obj;
  }

  public void print(String format, Object ... args) {
    if(enabled) {
      System.out.print(String.format(format, args));
    }
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

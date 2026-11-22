package net.minecraft.util.debug;

import net.minecraft.server.level.ServerLevel;

public interface DebugValueSource {
  void registerDebugValues(ServerLevel paramServerLevel, Registration paramRegistration);
  
  public static interface ValueGetter<T> {
    T get();
  }
  
  public static interface Registration {
    <T> void register(DebugSubscription<T> param1DebugSubscription, DebugValueSource.ValueGetter<T> param1ValueGetter);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugValueSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.util.debug;

public interface Registration {
  <T> void register(DebugSubscription<T> paramDebugSubscription, DebugValueSource.ValueGetter<T> paramValueGetter);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugValueSource$Registration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
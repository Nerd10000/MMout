package net.minecraft.util.parsing.packrat;

public class Nop<S> implements ErrorCollector<S> {
  public void store(int cursor, SuggestionSupplier<S> suggestions, Object reason) {}
  
  public void finish(int finalCursor) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\ErrorCollector$Nop.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
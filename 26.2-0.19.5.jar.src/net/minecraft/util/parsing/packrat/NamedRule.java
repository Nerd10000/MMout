package net.minecraft.util.parsing.packrat;

public interface NamedRule<S, T> {
  Atom<T> name();
  
  Rule<S, T> value();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\NamedRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
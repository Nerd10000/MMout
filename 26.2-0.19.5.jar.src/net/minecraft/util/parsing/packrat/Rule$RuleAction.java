package net.minecraft.util.parsing.packrat;

@FunctionalInterface
public interface RuleAction<S, T> {
  T run(ParseState<S> paramParseState);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Rule$RuleAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
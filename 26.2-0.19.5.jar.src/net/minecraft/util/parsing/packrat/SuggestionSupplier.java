/*   */ package net.minecraft.util.parsing.packrat;
/*   */ 
/*   */ import java.util.stream.Stream;
/*   */ 
/*   */ public interface SuggestionSupplier<S> {
/*   */   Stream<String> possibleValues(ParseState<S> paramParseState);
/*   */   
/*   */   static <S> SuggestionSupplier<S> empty() {
/* 9 */     return state -> Stream.empty();
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\SuggestionSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
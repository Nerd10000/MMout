/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ 
/*    */ public interface ResourceSuggestion extends SuggestionSupplier<StringReader> {
/*    */   Stream<Identifier> possibleResources();
/*    */   
/*    */   default Stream<String> possibleValues(ParseState<StringReader> state) {
/* 15 */     return possibleResources().map(Identifier::toString);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\ResourceSuggestion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ public class IdentifierParseRule implements Rule<StringReader, Identifier> {
/* 11 */   public static final Rule<StringReader, Identifier> INSTANCE = new IdentifierParseRule();
/*    */   
/*    */   public Identifier parse(ParseState<StringReader> state) {
/* 17 */     ((StringReader)state.input()).skipWhitespace();
/*    */     try {
/* 19 */       return Identifier.readNonEmpty((StringReader)state.input());
/* 20 */     } catch (CommandSyntaxException e) {
/* 22 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\IdentifierParseRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
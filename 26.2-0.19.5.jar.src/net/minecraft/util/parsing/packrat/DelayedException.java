/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*    */ 
/*    */ public interface DelayedException<T extends Exception> {
/*    */   static DelayedException<CommandSyntaxException> create(SimpleCommandExceptionType type) {
/* 12 */     return (contents, position) -> type.createWithContext((ImmutableStringReader)StringReaderTerms.createReader(contents, position));
/*    */   }
/*    */   
/*    */   static DelayedException<CommandSyntaxException> create(DynamicCommandExceptionType type, String argument) {
/* 16 */     return (contents, position) -> type.createWithContext((ImmutableStringReader)StringReaderTerms.createReader(contents, position), argument);
/*    */   }
/*    */   
/*    */   T create(String paramString, int paramInt);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\DelayedException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import net.minecraft.CharPredicate;
/*    */ 
/*    */ public class ParserUtils {
/*    */   public static String readWhile(StringReader reader, CharPredicate predicate) {
/*  8 */     int start = reader.getCursor();
/*  9 */     while (reader.canRead() && predicate.test(reader.peek()))
/* 10 */       reader.skip(); 
/* 12 */     return reader.getString().substring(start, reader.getCursor());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\ParserUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import net.minecraft.util.parsing.packrat.CachedParseState;
/*    */ import net.minecraft.util.parsing.packrat.ErrorCollector;
/*    */ 
/*    */ public class StringReaderParserState extends CachedParseState<StringReader> {
/*    */   private final StringReader input;
/*    */   
/*    */   public StringReaderParserState(ErrorCollector<StringReader> errorCollector, StringReader input) {
/* 11 */     super(errorCollector);
/* 12 */     this.input = input;
/*    */   }
/*    */   
/*    */   public StringReader input() {
/* 17 */     return this.input;
/*    */   }
/*    */   
/*    */   public int mark() {
/* 22 */     return this.input.getCursor();
/*    */   }
/*    */   
/*    */   public void restore(int mark) {
/* 27 */     this.input.setCursor(mark);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderParserState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
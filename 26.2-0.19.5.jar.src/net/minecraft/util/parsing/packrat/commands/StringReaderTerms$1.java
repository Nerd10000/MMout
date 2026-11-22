/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.chars.CharList;
/*    */ 
/*    */ class null extends StringReaderTerms.TerminalCharacters {
/*    */   null(CharList values) {
/* 75 */     super(values);
/*    */   }
/*    */   
/*    */   protected boolean isAccepted(char v) {
/* 78 */     return (value == v);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderTerms$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
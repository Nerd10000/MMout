/*    */ package net.minecraft.commands.arguments.selector.options;
/*    */ 
/*    */ public class SetOnceOptionState {
/*    */   private boolean hasValue;
/*    */   
/*    */   public boolean canParse() {
/*  8 */     return !this.hasValue;
/*    */   }
/*    */   
/*    */   public void markParsed() {
/* 12 */     this.hasValue = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\options\SetOnceOptionState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
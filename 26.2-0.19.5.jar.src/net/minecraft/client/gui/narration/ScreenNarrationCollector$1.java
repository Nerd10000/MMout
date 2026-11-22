/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ class null implements Consumer<String> {
/*    */   private boolean firstEntry = true;
/*    */   
/*    */   public void accept(String s) {
/* 44 */     if (!this.firstEntry)
/* 45 */       result.append(". "); 
/* 47 */     this.firstEntry = false;
/* 48 */     result.append(s);
/*    */   }
/*    */   
/*    */   null(ScreenNarrationCollector this$0) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\narration\ScreenNarrationCollector$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
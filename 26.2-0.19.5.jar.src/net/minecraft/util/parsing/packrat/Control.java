/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ public interface Control {
/*  4 */   public static final Control UNBOUND = new Control() {
/*    */       public void cut() {}
/*    */       
/*    */       public boolean hasCut() {
/* 11 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   void cut();
/*    */   
/*    */   boolean hasCut();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Control.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
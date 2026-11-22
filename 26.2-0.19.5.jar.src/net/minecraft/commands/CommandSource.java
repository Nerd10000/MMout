/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface CommandSource {
/*  6 */   public static final CommandSource NULL = new CommandSource() {
/*    */       public void sendSystemMessage(Component message) {}
/*    */       
/*    */       public boolean acceptsSuccess() {
/* 13 */         return false;
/*    */       }
/*    */       
/*    */       public boolean acceptsFailure() {
/* 18 */         return false;
/*    */       }
/*    */       
/*    */       public boolean shouldInformAdmins() {
/* 23 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   void sendSystemMessage(Component paramComponent);
/*    */   
/*    */   boolean acceptsSuccess();
/*    */   
/*    */   boolean acceptsFailure();
/*    */   
/*    */   boolean shouldInformAdmins();
/*    */   
/*    */   default boolean alwaysAccepts() {
/* 36 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\CommandSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
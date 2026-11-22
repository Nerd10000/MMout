/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class LevelStorageException extends RuntimeException {
/*    */   private final Component messageComponent;
/*    */   
/*    */   public LevelStorageException(Component message) {
/*  9 */     super(message.getString());
/* 10 */     this.messageComponent = message;
/*    */   }
/*    */   
/*    */   public LevelStorageException(Component message, Exception e) {
/* 14 */     super(message.getString(), e);
/* 15 */     this.messageComponent = message;
/*    */   }
/*    */   
/*    */   public Component getMessageComponent() {
/* 19 */     return this.messageComponent;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelStorageException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
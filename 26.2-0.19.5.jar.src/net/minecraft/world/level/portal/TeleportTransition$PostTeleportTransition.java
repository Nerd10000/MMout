/*    */ package net.minecraft.world.level.portal;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PostTeleportTransition {
/*    */   void onTransition(Entity paramEntity);
/*    */   
/*    */   default PostTeleportTransition then(PostTeleportTransition postTeleportTransition) {
/* 22 */     return entity -> {
/*    */         onTransition(entity);
/*    */         postTeleportTransition.onTransition(entity);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\portal\TeleportTransition$PostTeleportTransition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
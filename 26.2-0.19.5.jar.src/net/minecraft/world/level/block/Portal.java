/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.portal.TeleportTransition;
/*    */ 
/*    */ public interface Portal {
/*    */   public enum Transition {
/* 11 */     CONFUSION, NONE;
/*    */   }
/*    */   
/*    */   default int getPortalTransitionTime(ServerLevel level, Entity entity) {
/* 16 */     return 0;
/*    */   }
/*    */   
/*    */   TeleportTransition getPortalDestination(ServerLevel paramServerLevel, Entity paramEntity, BlockPos paramBlockPos);
/*    */   
/*    */   default Transition getLocalTransition() {
/* 22 */     return Transition.NONE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\Portal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
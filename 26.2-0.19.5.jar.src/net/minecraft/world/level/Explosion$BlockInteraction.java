/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public enum BlockInteraction {
/* 43 */   KEEP(false),
/* 44 */   DESTROY(true),
/* 45 */   DESTROY_WITH_DECAY(true),
/* 46 */   TRIGGER_BLOCK(false);
/*    */   
/*    */   private final boolean shouldAffectBlocklikeEntities;
/*    */   
/*    */   BlockInteraction(boolean shouldAffectBlocklikeEntities) {
/* 51 */     this.shouldAffectBlocklikeEntities = shouldAffectBlocklikeEntities;
/*    */   }
/*    */   
/*    */   public boolean shouldAffectBlocklikeEntities() {
/* 55 */     return this.shouldAffectBlocklikeEntities;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\Explosion$BlockInteraction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
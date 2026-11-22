/*   */ package net.minecraft.world.level.entity;
/*   */ 
/*   */ import net.minecraft.world.entity.Entity;
/*   */ 
/*   */ public interface EntityInLevelCallback {
/* 6 */   public static final EntityInLevelCallback NULL = new EntityInLevelCallback() {
/*   */       public void onMove() {}
/*   */       
/*   */       public void onRemove(Entity.RemovalReason reason) {}
/*   */     };
/*   */   
/*   */   void onMove();
/*   */   
/*   */   void onRemove(Entity.RemovalReason paramRemovalReason);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\EntityInLevelCallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
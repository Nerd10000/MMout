/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class null extends BaseSpawner {
/*    */   public void broadcastEvent(Level level, BlockPos pos, int id) {
/* 20 */     level.broadcastEntityEvent((Entity)MinecartSpawner.this, (byte)id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartSpawner$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
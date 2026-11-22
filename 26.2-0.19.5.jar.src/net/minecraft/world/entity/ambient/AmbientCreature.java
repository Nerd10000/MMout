/*    */ package net.minecraft.world.entity.ambient;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AmbientCreature extends Mob {
/*    */   protected AmbientCreature(EntityType<? extends AmbientCreature> type, Level level) {
/*  9 */     super(type, level);
/*    */   }
/*    */   
/*    */   public boolean canBeLeashed() {
/* 14 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ambient\AmbientCreature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ 
/*    */ public enum InsideBlockEffectType {
/* 14 */   CLEAR_FREEZE(Entity::clearFreeze),
/* 15 */   FIRE_IGNITE(BaseFireBlock::fireIgnite),
/* 16 */   LAVA_IGNITE(Entity::lavaIgnite),
/* 17 */   EXTINGUISH(Entity::clearFire),
/* 17 */   FREEZE(Entity::clearFire);
/*    */   
/*    */   static {
/*    */     FREEZE = new InsideBlockEffectType("FREEZE", 0, entity -> {
/*    */           entity.setIsInPowderSnow(true);
/*    */           if (entity.canFreeze())
/*    */             entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze(), entity.getTicksFrozen() + 1)); 
/*    */         });
/*    */   }
/*    */   
/*    */   private final Consumer<Entity> effect;
/*    */   
/*    */   InsideBlockEffectType(Consumer<Entity> effect) {
/* 23 */     this.effect = effect;
/*    */   }
/*    */   
/*    */   public Consumer<Entity> effect() {
/* 27 */     return this.effect;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\InsideBlockEffectType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
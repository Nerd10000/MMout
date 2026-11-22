/*    */ package net.minecraft.world.entity.animal.axolotl;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class PlayDead extends Behavior<Axolotl> {
/*    */   public PlayDead() {
/* 15 */     super((Map)ImmutableMap.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT), 200);
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Axolotl body) {
/* 24 */     return body.isInWater();
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Axolotl body, long timestamp) {
/* 29 */     return (body.isInWater() && body.getBrain().hasMemoryValue(MemoryModuleType.PLAY_DEAD_TICKS));
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, Axolotl body, long timestamp) {
/* 34 */     Brain<Axolotl> brain = body.getBrain();
/* 36 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 37 */     brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 39 */     body.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\PlayDead.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
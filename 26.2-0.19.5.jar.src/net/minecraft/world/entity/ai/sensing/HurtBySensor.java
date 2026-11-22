/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class HurtBySensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 19 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 24 */     Brain<?> brain = body.getBrain();
/* 25 */     DamageSource damageSource = body.getLastDamageSource();
/* 26 */     if (damageSource != null) {
/* 27 */       brain.setMemory(MemoryModuleType.HURT_BY, body.getLastDamageSource());
/* 28 */       Entity entitySource = damageSource.getEntity();
/* 29 */       if (entitySource instanceof LivingEntity) {
/* 29 */         LivingEntity livingEntity = (LivingEntity)entitySource;
/* 30 */         brain.setMemory(MemoryModuleType.HURT_BY_ENTITY, livingEntity);
/*    */       } 
/*    */     } else {
/* 33 */       brain.eraseMemory(MemoryModuleType.HURT_BY);
/*    */     } 
/* 39 */     brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent(hurtByEntity -> {
/*    */           if (!hurtByEntity.isAlive() || hurtByEntity.level() != level)
/*    */             brain.eraseMemory(MemoryModuleType.HURT_BY_ENTITY); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\HurtBySensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
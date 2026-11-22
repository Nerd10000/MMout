/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class NearestLivingEntitySensor<T extends LivingEntity> extends Sensor<T> {
/*    */   protected void doTick(ServerLevel level, T body) {
/* 21 */     double followRange = body.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 22 */     AABB boundingBox = body.getBoundingBox().inflate(followRange, followRange, followRange);
/* 23 */     List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, boundingBox, mob -> (mob != body && mob.isAlive()));
/* 24 */     Objects.requireNonNull(body);
/* 24 */     livingEntities.sort(Comparator.comparingDouble(body::distanceToSqr));
/* 26 */     Brain<?> brain = body.getBrain();
/* 27 */     brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, livingEntities);
/* 28 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(level, (LivingEntity)body, livingEntities));
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 33 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\NearestLivingEntitySensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
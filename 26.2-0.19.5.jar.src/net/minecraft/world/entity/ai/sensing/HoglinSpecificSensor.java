/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*    */ 
/*    */ public class HoglinSpecificSensor extends Sensor<Hoglin> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 24 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, (Object[])new MemoryModuleType[0]);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, Hoglin body) {
/* 39 */     Brain<?> brain = body.getBrain();
/* 41 */     brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, body));
/* 43 */     Optional<Piglin> adultPiglin = Optional.empty();
/* 44 */     int adultPiglinCount = 0;
/* 46 */     List<Hoglin> adultHoglins = Lists.newArrayList();
/* 49 */     NearestVisibleLivingEntities visibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/* 50 */     for (LivingEntity entity : visibleLivingEntities.findAll(entity -> (!entity.isBaby() && (entity instanceof Piglin || entity instanceof Hoglin)))) {
/* 51 */       if (entity instanceof Piglin) {
/* 51 */         Piglin piglin = (Piglin)entity;
/* 52 */         adultPiglinCount++;
/* 53 */         if (adultPiglin.isEmpty())
/* 54 */           adultPiglin = Optional.of(piglin); 
/*    */       } 
/* 58 */       if (entity instanceof Hoglin) {
/* 58 */         Hoglin hoglin = (Hoglin)entity;
/* 59 */         adultHoglins.add(hoglin);
/*    */       } 
/*    */     } 
/* 63 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, adultPiglin);
/* 64 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, adultHoglins);
/* 65 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, Integer.valueOf(adultPiglinCount));
/* 66 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, Integer.valueOf(adultHoglins.size()));
/*    */   }
/*    */   
/*    */   private Optional<BlockPos> findNearestRepellent(ServerLevel level, Hoglin body) {
/* 70 */     return BlockPos.findClosestMatch(body
/* 71 */         .blockPosition(), 8, 4, pos -> level.getBlockState(pos).is(BlockTags.HOGLIN_REPELLENTS));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\HoglinSpecificSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*    */ 
/*    */ public class PiglinBruteSpecificSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 22 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 34 */     Brain<?> brain = body.getBrain();
/* 37 */     NearestVisibleLivingEntities visibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/* 38 */     Objects.requireNonNull(Mob.class);
/* 38 */     Optional<Mob> nemesis = visibleLivingEntities.findClosest(entity -> (entity instanceof net.minecraft.world.entity.monster.skeleton.WitherSkeleton || entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss)).map(Mob.class::cast);
/* 40 */     List<AbstractPiglin> adultPiglins = PiglinAi.findNearbyAdultPiglins(brain);
/* 42 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, nemesis);
/* 43 */     brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, adultPiglins);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\PiglinBruteSpecificSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
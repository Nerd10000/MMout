/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class VillagerBabiesSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 19 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 24 */     body.getBrain().setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, getNearestVillagerBabies(body));
/*    */   }
/*    */   
/*    */   private List<LivingEntity> getNearestVillagerBabies(LivingEntity myBody) {
/* 28 */     return (List<LivingEntity>)ImmutableList.copyOf(getVisibleEntities(myBody).findAll(this::isVillagerBaby));
/*    */   }
/*    */   
/*    */   private boolean isVillagerBaby(LivingEntity entity) {
/* 32 */     return (entity.is(EntityTypes.VILLAGER) && entity.isBaby());
/*    */   }
/*    */   
/*    */   private NearestVisibleLivingEntities getVisibleEntities(LivingEntity myBody) {
/* 36 */     return myBody.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
/* 37 */       .orElse(NearestVisibleLivingEntities.empty());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\VillagerBabiesSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
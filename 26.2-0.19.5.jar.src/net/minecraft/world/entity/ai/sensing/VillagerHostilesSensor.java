/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class VillagerHostilesSensor extends NearestVisibleLivingEntitySensor {
/* 13 */   private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
/* 14 */     .put(EntityTypes.DROWNED, Float.valueOf(8.0F))
/* 15 */     .put(EntityTypes.EVOKER, Float.valueOf(12.0F))
/* 16 */     .put(EntityTypes.HUSK, Float.valueOf(8.0F))
/* 17 */     .put(EntityTypes.ILLUSIONER, Float.valueOf(12.0F))
/* 18 */     .put(EntityTypes.PILLAGER, Float.valueOf(15.0F))
/* 19 */     .put(EntityTypes.RAVAGER, Float.valueOf(12.0F))
/* 20 */     .put(EntityTypes.VEX, Float.valueOf(8.0F))
/* 21 */     .put(EntityTypes.VINDICATOR, Float.valueOf(10.0F))
/* 22 */     .put(EntityTypes.ZOGLIN, Float.valueOf(10.0F))
/* 23 */     .put(EntityTypes.ZOMBIE, Float.valueOf(8.0F))
/* 24 */     .put(EntityTypes.ZOMBIE_VILLAGER, Float.valueOf(8.0F))
/* 25 */     .build();
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
/* 29 */     return (isHostile(mob) && isClose(body, mob));
/*    */   }
/*    */   
/*    */   private boolean isClose(LivingEntity body, LivingEntity mob) {
/* 33 */     float distThreshold = ((Float)ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(mob.getType())).floatValue();
/* 34 */     return (mob.distanceToSqr((Entity)body) <= (distThreshold * distThreshold));
/*    */   }
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemoryToSet() {
/* 39 */     return MemoryModuleType.NEAREST_HOSTILE;
/*    */   }
/*    */   
/*    */   private boolean isHostile(LivingEntity entity) {
/* 43 */     return ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(entity.getType());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\VillagerHostilesSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
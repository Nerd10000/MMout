/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class AdultSensorAnyType extends AdultSensor {
/*    */   protected void setNearestVisibleAdult(LivingEntity body, NearestVisibleLivingEntities visibleLivingEntities) {
/* 13 */     Optional<LivingEntity> adult = visibleLivingEntities.findClosest(entity -> (entity.is(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS) && !entity.isBaby()));
/* 14 */     body.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, adult);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\AdultSensorAnyType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
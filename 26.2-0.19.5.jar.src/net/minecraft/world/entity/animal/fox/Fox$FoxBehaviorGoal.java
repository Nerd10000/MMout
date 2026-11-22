/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ 
/*      */ abstract class FoxBehaviorGoal extends Goal {
/* 1103 */   private final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(12.0D).ignoreLineOfSight().selector(new Fox.FoxAlertableEntitiesSelector(Fox.this));
/*      */   
/*      */   protected boolean hasShelter() {
/* 1106 */     BlockPos foxPos = BlockPos.containing(Fox.this.getX(), (Fox.this.getBoundingBox()).maxY, Fox.this.getZ());
/* 1107 */     return (!Fox.this.level().canSeeSky(foxPos) && Fox.this.getWalkTargetValue(foxPos) >= 0.0F);
/*      */   }
/*      */   
/*      */   protected boolean alertable() {
/* 1111 */     return !getServerLevel(Fox.this.level()).getNearbyEntities(LivingEntity.class, this.alertableTargeting, (LivingEntity)Fox.this, Fox.this.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)).isEmpty();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxBehaviorGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
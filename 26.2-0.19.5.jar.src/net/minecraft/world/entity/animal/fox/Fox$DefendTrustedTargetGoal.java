/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.level.Level;
/*      */ 
/*      */ class DefendTrustedTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*      */   private LivingEntity trustedLastHurtBy;
/*      */   
/*      */   private LivingEntity trustedLastHurt;
/*      */   
/*      */   private int timestamp;
/*      */   
/*      */   public DefendTrustedTargetGoal(Class<LivingEntity> targetType, boolean mustSee, boolean mustReach, TargetingConditions.Selector subselector) {
/*  991 */     super((Mob)paramFox, targetType, 10, mustSee, mustReach, subselector);
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/*  996 */     if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0)
/*  997 */       return false; 
/* 1000 */     ServerLevel level = getServerLevel(Fox.this.level());
/* 1001 */     for (EntityReference<LivingEntity> trustedReference : Fox.this.getTrustedEntities().toList()) {
/* 1002 */       LivingEntity trustedEntity = (LivingEntity)trustedReference.getEntity((Level)level, LivingEntity.class);
/* 1003 */       if (trustedEntity != null) {
/* 1004 */         this.trustedLastHurt = trustedEntity;
/* 1005 */         this.trustedLastHurtBy = trustedEntity.getLastHurtByMob();
/* 1006 */         int timestamp = trustedEntity.getLastHurtByMobTimestamp();
/* 1007 */         return (timestamp != this.timestamp && canAttack(this.trustedLastHurtBy, this.targetConditions));
/*      */       } 
/*      */     } 
/* 1010 */     return false;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1015 */     setTarget(this.trustedLastHurtBy);
/* 1016 */     this.target = this.trustedLastHurtBy;
/* 1018 */     if (this.trustedLastHurt != null)
/* 1019 */       this.timestamp = this.trustedLastHurt.getLastHurtByMobTimestamp(); 
/* 1022 */     Fox.this.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
/* 1024 */     Fox.this.setDefending(true);
/* 1027 */     Fox.this.wakeUp();
/* 1029 */     super.start();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$DefendTrustedTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
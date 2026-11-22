/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.TamableAnimal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ 
/*      */ public class FoxAlertableEntitiesSelector implements TargetingConditions.Selector {
/*      */   public boolean test(LivingEntity target, ServerLevel level) {
/* 1073 */     if (target instanceof Fox)
/* 1074 */       return false; 
/* 1078 */     if (target instanceof net.minecraft.world.entity.animal.chicken.Chicken || target instanceof net.minecraft.world.entity.animal.rabbit.Rabbit || target instanceof net.minecraft.world.entity.monster.Monster)
/* 1079 */       return true; 
/* 1083 */     if (target instanceof TamableAnimal) {
/* 1083 */       TamableAnimal tamableAnimal = (TamableAnimal)target;
/* 1084 */       return !tamableAnimal.isTame();
/*      */     } 
/* 1088 */     if (target instanceof Player) {
/* 1088 */       Player player = (Player)target;
/* 1088 */       if (player.isSpectator() || player.isCreative())
/* 1089 */         return false; 
/*      */     } 
/* 1093 */     if (Fox.this.trusts(target))
/* 1094 */       return false; 
/* 1098 */     return (!target.isSleeping() && !target.isDiscrete());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxAlertableEntitiesSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class InteractGoal extends LookAtPlayerGoal {
/*    */   public InteractGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
/* 10 */     super(mob, lookAtType, lookDistance);
/* 11 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
/*    */   }
/*    */   
/*    */   public InteractGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, float probability) {
/* 15 */     super(mob, lookAtType, lookDistance, probability);
/* 16 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\InteractGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
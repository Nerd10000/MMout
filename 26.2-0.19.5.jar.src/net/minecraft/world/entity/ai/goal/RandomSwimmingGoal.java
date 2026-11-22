/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RandomSwimmingGoal extends RandomStrollGoal {
/*    */   public RandomSwimmingGoal(PathfinderMob mob, double speedModifier, int interval) {
/* 10 */     super(mob, speedModifier, interval);
/*    */   }
/*    */   
/*    */   protected Vec3 getPosition() {
/* 15 */     return BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\RandomSwimmingGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
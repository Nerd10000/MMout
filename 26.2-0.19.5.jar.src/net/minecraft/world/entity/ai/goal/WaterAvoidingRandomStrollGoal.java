/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterAvoidingRandomStrollGoal extends RandomStrollGoal {
/*    */   public static final float PROBABILITY = 0.001F;
/*    */   
/*    */   protected final float probability;
/*    */   
/*    */   public WaterAvoidingRandomStrollGoal(PathfinderMob mob, double speedModifier) {
/* 14 */     this(mob, speedModifier, 0.001F);
/*    */   }
/*    */   
/*    */   public WaterAvoidingRandomStrollGoal(PathfinderMob mob, double speedModifier, float probability) {
/* 18 */     super(mob, speedModifier);
/* 19 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   protected Vec3 getPosition() {
/* 24 */     if (this.mob.isInWater()) {
/* 26 */       Vec3 pos = LandRandomPos.getPos(this.mob, 15, 7);
/* 27 */       return (pos == null) ? super.getPosition() : pos;
/*    */     } 
/* 29 */     if (this.mob.getRandom().nextFloat() >= this.probability)
/* 30 */       return LandRandomPos.getPos(this.mob, 10, 7); 
/* 32 */     return super.getPosition();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\WaterAvoidingRandomStrollGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
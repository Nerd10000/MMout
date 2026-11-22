/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SpearUseState {
/* 142 */   private int engageTime = -1;
/*     */   
/* 143 */   private int fleeingTime = -1;
/*     */   
/*     */   private Vec3 awayPos;
/*     */   
/*     */   private boolean done = false;
/*     */   
/*     */   public boolean notEngagedYet() {
/* 148 */     return (this.engageTime < 0);
/*     */   }
/*     */   
/*     */   public void startEngagement(int spearDownTime) {
/* 152 */     this.engageTime = spearDownTime;
/*     */   }
/*     */   
/*     */   public boolean tickAndCheckEngagement() {
/* 156 */     if (this.engageTime > 0) {
/* 157 */       this.engageTime--;
/* 158 */       if (this.engageTime == 0)
/* 159 */         return true; 
/*     */     } 
/* 162 */     return false;
/*     */   }
/*     */   
/*     */   public boolean tickAndCheckFleeing() {
/* 166 */     if (this.fleeingTime > 0) {
/* 167 */       this.fleeingTime++;
/* 168 */       if (this.fleeingTime > SpearUseGoal.MAX_FLEEING_TIME) {
/* 169 */         this.done = true;
/* 170 */         return true;
/*     */       } 
/*     */     } 
/* 173 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\SpearUseGoal$SpearUseState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
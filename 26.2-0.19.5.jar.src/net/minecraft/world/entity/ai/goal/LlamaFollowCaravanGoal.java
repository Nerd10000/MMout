/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LlamaFollowCaravanGoal extends Goal {
/*     */   public final Llama llama;
/*     */   
/*     */   private double speedModifier;
/*     */   
/*     */   private static final int CARAVAN_LIMIT = 8;
/*     */   
/*     */   private int distCheckCounter;
/*     */   
/*     */   public LlamaFollowCaravanGoal(Llama llama, double speedModifier) {
/*  21 */     this.llama = llama;
/*  22 */     this.speedModifier = speedModifier;
/*  23 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/*  28 */     if (this.llama.isLeashed() || this.llama.inCaravan())
/*  29 */       return false; 
/*  32 */     List<Entity> llamas = this.llama.level().getEntities((Entity)this.llama, this.llama.getBoundingBox().inflate(9.0D, 4.0D, 9.0D), e -> 
/*  33 */         (e.is(EntityTypes.LLAMA) || e.is(EntityTypes.TRADER_LLAMA)));
/*  36 */     Llama closest = null;
/*  37 */     double closestDistSquare = Double.MAX_VALUE;
/*  38 */     for (Entity entity : llamas) {
/*  39 */       Llama candidate = (Llama)entity;
/*  41 */       if (!candidate.inCaravan() || candidate.hasCaravanTail())
/*     */         continue; 
/*  45 */       double distSquare = this.llama.distanceToSqr((Entity)candidate);
/*  46 */       if (distSquare > closestDistSquare)
/*     */         continue; 
/*  50 */       closestDistSquare = distSquare;
/*  51 */       closest = candidate;
/*     */     } 
/*  54 */     if (closest == null)
/*  56 */       for (Entity entity : llamas) {
/*  57 */         Llama candidate = (Llama)entity;
/*  59 */         if (!candidate.isLeashed())
/*     */           continue; 
/*  63 */         if (candidate.hasCaravanTail())
/*     */           continue; 
/*  67 */         double distSquare = this.llama.distanceToSqr((Entity)candidate);
/*  68 */         if (distSquare > closestDistSquare)
/*     */           continue; 
/*  72 */         closestDistSquare = distSquare;
/*  73 */         closest = candidate;
/*     */       }  
/*  77 */     if (closest == null)
/*  78 */       return false; 
/*  80 */     if (closestDistSquare < 4.0D)
/*  81 */       return false; 
/*  84 */     if (!closest.isLeashed() && !firstIsLeashed(closest, 1))
/*  85 */       return false; 
/*  88 */     this.llama.joinCaravan(closest);
/*  90 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/*  95 */     if (!this.llama.inCaravan() || !this.llama.getCaravanHead().isAlive() || !firstIsLeashed(this.llama, 0))
/*  96 */       return false; 
/*  99 */     double distSqr = this.llama.distanceToSqr((Entity)this.llama.getCaravanHead());
/* 100 */     if (distSqr > 676.0D) {
/* 101 */       if (this.speedModifier <= 3.0D) {
/* 102 */         this.speedModifier *= 1.2D;
/* 103 */         this.distCheckCounter = reducedTickDelay(40);
/* 104 */         return true;
/*     */       } 
/* 107 */       if (this.distCheckCounter == 0)
/* 108 */         return false; 
/*     */     } 
/* 111 */     if (this.distCheckCounter > 0)
/* 112 */       this.distCheckCounter--; 
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 119 */     this.llama.leaveCaravan();
/* 120 */     this.speedModifier = 2.1D;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 125 */     if (!this.llama.inCaravan())
/*     */       return; 
/* 129 */     if (this.llama.getLeashHolder() instanceof net.minecraft.world.entity.decoration.LeashFenceKnotEntity)
/*     */       return; 
/* 133 */     Llama follows = this.llama.getCaravanHead();
/* 134 */     double distanceTo = this.llama.distanceTo((Entity)follows);
/* 136 */     float wantedDistance = 2.0F;
/* 137 */     Vec3 delta = (new Vec3(follows.getX() - this.llama.getX(), follows.getY() - this.llama.getY(), follows.getZ() - this.llama.getZ())).normalize().scale(Math.max(distanceTo - 2.0D, 0.0D));
/* 138 */     this.llama.getNavigation().moveTo(this.llama.getX() + delta.x, this.llama.getY() + delta.y, this.llama.getZ() + delta.z, this.speedModifier);
/*     */   }
/*     */   
/*     */   private boolean firstIsLeashed(Llama currentMob, int counter) {
/* 142 */     if (counter > 8)
/* 143 */       return false; 
/* 146 */     if (currentMob.inCaravan()) {
/* 147 */       if (currentMob.getCaravanHead().isLeashed())
/* 148 */         return true; 
/* 150 */       return firstIsLeashed(currentMob.getCaravanHead(), ++counter);
/*     */     } 
/* 152 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\LlamaFollowCaravanGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
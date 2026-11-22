/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*     */ 
/*     */ public class TraderLlamaDefendWanderingTraderGoal extends TargetGoal {
/*     */   private final Llama llama;
/*     */   
/*     */   private LivingEntity ownerLastHurtBy;
/*     */   
/*     */   private int timestamp;
/*     */   
/*     */   public TraderLlamaDefendWanderingTraderGoal(Llama tameAnimal) {
/* 144 */     super((Mob)tameAnimal, false);
/* 145 */     this.llama = tameAnimal;
/* 146 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/*     */     WanderingTrader owner;
/* 151 */     if (!this.llama.isLeashed())
/* 152 */       return false; 
/* 154 */     Entity leashHolder = this.llama.getLeashHolder();
/* 155 */     if (leashHolder instanceof WanderingTrader) {
/* 155 */       owner = (WanderingTrader)leashHolder;
/*     */     } else {
/* 156 */       return false;
/*     */     } 
/* 159 */     this.ownerLastHurtBy = owner.getLastHurtByMob();
/* 160 */     int timeStamp = owner.getLastHurtByMobTimestamp();
/* 161 */     return (timeStamp != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT));
/*     */   }
/*     */   
/*     */   public void start() {
/* 166 */     this.mob.setTarget(this.ownerLastHurtBy);
/* 168 */     Entity leashHolder = this.llama.getLeashHolder();
/* 169 */     if (leashHolder instanceof WanderingTrader) {
/* 169 */       WanderingTrader wanderingTrader = (WanderingTrader)leashHolder;
/* 170 */       this.timestamp = wanderingTrader.getLastHurtByMobTimestamp();
/*     */     } 
/* 173 */     super.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\TraderLlama$TraderLlamaDefendWanderingTraderGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
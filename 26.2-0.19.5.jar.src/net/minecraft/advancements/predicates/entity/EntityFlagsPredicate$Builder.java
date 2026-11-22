/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ 
/*     */ public class Builder {
/*  87 */   private Optional<Boolean> isOnGround = Optional.empty();
/*     */   
/*  88 */   private Optional<Boolean> isOnFire = Optional.empty();
/*     */   
/*  89 */   private Optional<Boolean> isCrouching = Optional.empty();
/*     */   
/*  90 */   private Optional<Boolean> isSprinting = Optional.empty();
/*     */   
/*  91 */   private Optional<Boolean> isSwimming = Optional.empty();
/*     */   
/*  92 */   private Optional<Boolean> isFlying = Optional.empty();
/*     */   
/*  93 */   private Optional<Boolean> isBaby = Optional.empty();
/*     */   
/*  94 */   private Optional<Boolean> isInWater = Optional.empty();
/*     */   
/*  95 */   private Optional<Boolean> isFallFlying = Optional.empty();
/*     */   
/*     */   public static Builder flags() {
/*  98 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder setOnGround(Boolean onGround) {
/* 102 */     this.isOnGround = Optional.of(onGround);
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setOnFire(Boolean onFire) {
/* 107 */     this.isOnFire = Optional.of(onFire);
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCrouching(Boolean crouching) {
/* 112 */     this.isCrouching = Optional.of(crouching);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSprinting(Boolean sprinting) {
/* 117 */     this.isSprinting = Optional.of(sprinting);
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSwimming(Boolean swimming) {
/* 122 */     this.isSwimming = Optional.of(swimming);
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsFlying(Boolean flying) {
/* 127 */     this.isFlying = Optional.of(flying);
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsBaby(Boolean baby) {
/* 132 */     this.isBaby = Optional.of(baby);
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsInWater(Boolean inWater) {
/* 137 */     this.isInWater = Optional.of(inWater);
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsFallFlying(Boolean fallFlying) {
/* 142 */     this.isFallFlying = Optional.of(fallFlying);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public EntityFlagsPredicate build() {
/* 147 */     return new EntityFlagsPredicate(this.isOnGround, this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isFlying, this.isBaby, this.isInWater, this.isFallFlying);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityFlagsPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
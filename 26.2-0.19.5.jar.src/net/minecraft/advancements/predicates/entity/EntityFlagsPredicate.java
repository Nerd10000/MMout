/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class EntityFlagsPredicate extends Record implements EntitySubPredicate {
/*     */   private final Optional<Boolean> isOnGround;
/*     */   
/*     */   private final Optional<Boolean> isOnFire;
/*     */   
/*     */   private final Optional<Boolean> isCrouching;
/*     */   
/*     */   private final Optional<Boolean> isSprinting;
/*     */   
/*     */   private final Optional<Boolean> isSwimming;
/*     */   
/*     */   private final Optional<Boolean> isFlying;
/*     */   
/*     */   private final Optional<Boolean> isBaby;
/*     */   
/*     */   private final Optional<Boolean> isInWater;
/*     */   
/*     */   private final Optional<Boolean> isFallFlying;
/*     */   
/*     */   public static final Codec<EntityFlagsPredicate> CODEC;
/*     */   
/*     */   public EntityFlagsPredicate(Optional<Boolean> isOnGround, Optional<Boolean> isOnFire, Optional<Boolean> isCrouching, Optional<Boolean> isSprinting, Optional<Boolean> isSwimming, Optional<Boolean> isFlying, Optional<Boolean> isBaby, Optional<Boolean> isInWater, Optional<Boolean> isFallFlying) {
/*  14 */     this.isOnGround = isOnGround;
/*  14 */     this.isOnFire = isOnFire;
/*  14 */     this.isCrouching = isCrouching;
/*  14 */     this.isSprinting = isSprinting;
/*  14 */     this.isSwimming = isSwimming;
/*  14 */     this.isFlying = isFlying;
/*  14 */     this.isBaby = isBaby;
/*  14 */     this.isInWater = isInWater;
/*  14 */     this.isFallFlying = isFallFlying;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isOnGround() {
/*  14 */     return this.isOnGround;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isOnFire() {
/*  14 */     return this.isOnFire;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isCrouching() {
/*  14 */     return this.isCrouching;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isSprinting() {
/*  14 */     return this.isSprinting;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isSwimming() {
/*  14 */     return this.isSwimming;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isFlying() {
/*  14 */     return this.isFlying;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isBaby() {
/*  14 */     return this.isBaby;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isInWater() {
/*  14 */     return this.isInWater;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> isFallFlying() {
/*  14 */     return this.isFallFlying;
/*     */   }
/*     */   
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("is_on_ground").forGetter(EntityFlagsPredicate::isOnGround), (App)Codec.BOOL.optionalFieldOf("is_on_fire").forGetter(EntityFlagsPredicate::isOnFire), (App)Codec.BOOL.optionalFieldOf("is_sneaking").forGetter(EntityFlagsPredicate::isCrouching), (App)Codec.BOOL.optionalFieldOf("is_sprinting").forGetter(EntityFlagsPredicate::isSprinting), (App)Codec.BOOL.optionalFieldOf("is_swimming").forGetter(EntityFlagsPredicate::isSwimming), (App)Codec.BOOL.optionalFieldOf("is_flying").forGetter(EntityFlagsPredicate::isFlying), (App)Codec.BOOL.optionalFieldOf("is_baby").forGetter(EntityFlagsPredicate::isBaby), (App)Codec.BOOL.optionalFieldOf("is_in_water").forGetter(EntityFlagsPredicate::isInWater), (App)Codec.BOOL.optionalFieldOf("is_fall_flying").forGetter(EntityFlagsPredicate::isFallFlying)).apply((Applicative)i, EntityFlagsPredicate::new));
/*     */   }
/*     */   
/*     */   public boolean matches(Entity entity) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield isOnGround : Ljava/util/Optional;
/*     */     //   4: invokevirtual isPresent : ()Z
/*     */     //   7: ifeq -> 32
/*     */     //   10: aload_1
/*     */     //   11: invokevirtual onGround : ()Z
/*     */     //   14: aload_0
/*     */     //   15: getfield isOnGround : Ljava/util/Optional;
/*     */     //   18: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   21: checkcast java/lang/Boolean
/*     */     //   24: invokevirtual booleanValue : ()Z
/*     */     //   27: if_icmpeq -> 32
/*     */     //   30: iconst_0
/*     */     //   31: ireturn
/*     */     //   32: aload_0
/*     */     //   33: getfield isOnFire : Ljava/util/Optional;
/*     */     //   36: invokevirtual isPresent : ()Z
/*     */     //   39: ifeq -> 64
/*     */     //   42: aload_1
/*     */     //   43: invokevirtual isOnFire : ()Z
/*     */     //   46: aload_0
/*     */     //   47: getfield isOnFire : Ljava/util/Optional;
/*     */     //   50: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   53: checkcast java/lang/Boolean
/*     */     //   56: invokevirtual booleanValue : ()Z
/*     */     //   59: if_icmpeq -> 64
/*     */     //   62: iconst_0
/*     */     //   63: ireturn
/*     */     //   64: aload_0
/*     */     //   65: getfield isCrouching : Ljava/util/Optional;
/*     */     //   68: invokevirtual isPresent : ()Z
/*     */     //   71: ifeq -> 96
/*     */     //   74: aload_1
/*     */     //   75: invokevirtual isCrouching : ()Z
/*     */     //   78: aload_0
/*     */     //   79: getfield isCrouching : Ljava/util/Optional;
/*     */     //   82: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   85: checkcast java/lang/Boolean
/*     */     //   88: invokevirtual booleanValue : ()Z
/*     */     //   91: if_icmpeq -> 96
/*     */     //   94: iconst_0
/*     */     //   95: ireturn
/*     */     //   96: aload_0
/*     */     //   97: getfield isSprinting : Ljava/util/Optional;
/*     */     //   100: invokevirtual isPresent : ()Z
/*     */     //   103: ifeq -> 128
/*     */     //   106: aload_1
/*     */     //   107: invokevirtual isSprinting : ()Z
/*     */     //   110: aload_0
/*     */     //   111: getfield isSprinting : Ljava/util/Optional;
/*     */     //   114: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   117: checkcast java/lang/Boolean
/*     */     //   120: invokevirtual booleanValue : ()Z
/*     */     //   123: if_icmpeq -> 128
/*     */     //   126: iconst_0
/*     */     //   127: ireturn
/*     */     //   128: aload_0
/*     */     //   129: getfield isSwimming : Ljava/util/Optional;
/*     */     //   132: invokevirtual isPresent : ()Z
/*     */     //   135: ifeq -> 160
/*     */     //   138: aload_1
/*     */     //   139: invokevirtual isSwimming : ()Z
/*     */     //   142: aload_0
/*     */     //   143: getfield isSwimming : Ljava/util/Optional;
/*     */     //   146: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   149: checkcast java/lang/Boolean
/*     */     //   152: invokevirtual booleanValue : ()Z
/*     */     //   155: if_icmpeq -> 160
/*     */     //   158: iconst_0
/*     */     //   159: ireturn
/*     */     //   160: aload_0
/*     */     //   161: getfield isFlying : Ljava/util/Optional;
/*     */     //   164: invokevirtual isPresent : ()Z
/*     */     //   167: ifeq -> 240
/*     */     //   170: aload_1
/*     */     //   171: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   174: ifeq -> 219
/*     */     //   177: aload_1
/*     */     //   178: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   181: astore #4
/*     */     //   183: aload #4
/*     */     //   185: invokevirtual isFallFlying : ()Z
/*     */     //   188: ifne -> 215
/*     */     //   191: aload #4
/*     */     //   193: instanceof net/minecraft/world/entity/player/Player
/*     */     //   196: ifeq -> 219
/*     */     //   199: aload #4
/*     */     //   201: checkcast net/minecraft/world/entity/player/Player
/*     */     //   204: astore_3
/*     */     //   205: aload_3
/*     */     //   206: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*     */     //   209: getfield flying : Z
/*     */     //   212: ifeq -> 219
/*     */     //   215: iconst_1
/*     */     //   216: goto -> 220
/*     */     //   219: iconst_0
/*     */     //   220: istore_2
/*     */     //   221: iload_2
/*     */     //   222: aload_0
/*     */     //   223: getfield isFlying : Ljava/util/Optional;
/*     */     //   226: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   229: checkcast java/lang/Boolean
/*     */     //   232: invokevirtual booleanValue : ()Z
/*     */     //   235: if_icmpeq -> 240
/*     */     //   238: iconst_0
/*     */     //   239: ireturn
/*     */     //   240: aload_0
/*     */     //   241: getfield isInWater : Ljava/util/Optional;
/*     */     //   244: invokevirtual isPresent : ()Z
/*     */     //   247: ifeq -> 272
/*     */     //   250: aload_1
/*     */     //   251: invokevirtual isInWater : ()Z
/*     */     //   254: aload_0
/*     */     //   255: getfield isInWater : Ljava/util/Optional;
/*     */     //   258: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   261: checkcast java/lang/Boolean
/*     */     //   264: invokevirtual booleanValue : ()Z
/*     */     //   267: if_icmpeq -> 272
/*     */     //   270: iconst_0
/*     */     //   271: ireturn
/*     */     //   272: aload_0
/*     */     //   273: getfield isFallFlying : Ljava/util/Optional;
/*     */     //   276: invokevirtual isPresent : ()Z
/*     */     //   279: ifeq -> 316
/*     */     //   282: aload_1
/*     */     //   283: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   286: ifeq -> 316
/*     */     //   289: aload_1
/*     */     //   290: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   293: astore_2
/*     */     //   294: aload_2
/*     */     //   295: invokevirtual isFallFlying : ()Z
/*     */     //   298: aload_0
/*     */     //   299: getfield isFallFlying : Ljava/util/Optional;
/*     */     //   302: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   305: checkcast java/lang/Boolean
/*     */     //   308: invokevirtual booleanValue : ()Z
/*     */     //   311: if_icmpeq -> 316
/*     */     //   314: iconst_0
/*     */     //   315: ireturn
/*     */     //   316: aload_0
/*     */     //   317: getfield isBaby : Ljava/util/Optional;
/*     */     //   320: invokevirtual isPresent : ()Z
/*     */     //   323: ifeq -> 360
/*     */     //   326: aload_1
/*     */     //   327: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   330: ifeq -> 360
/*     */     //   333: aload_1
/*     */     //   334: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   337: astore_2
/*     */     //   338: aload_2
/*     */     //   339: invokevirtual isBaby : ()Z
/*     */     //   342: aload_0
/*     */     //   343: getfield isBaby : Ljava/util/Optional;
/*     */     //   346: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   349: checkcast java/lang/Boolean
/*     */     //   352: invokevirtual booleanValue : ()Z
/*     */     //   355: if_icmpeq -> 360
/*     */     //   358: iconst_0
/*     */     //   359: ireturn
/*     */     //   360: iconst_1
/*     */     //   361: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #39	-> 0
/*     */     //   #40	-> 30
/*     */     //   #43	-> 32
/*     */     //   #44	-> 62
/*     */     //   #47	-> 64
/*     */     //   #48	-> 94
/*     */     //   #51	-> 96
/*     */     //   #52	-> 126
/*     */     //   #55	-> 128
/*     */     //   #56	-> 158
/*     */     //   #59	-> 160
/*     */     //   #60	-> 170
/*     */     //   #61	-> 221
/*     */     //   #62	-> 238
/*     */     //   #66	-> 240
/*     */     //   #67	-> 270
/*     */     //   #70	-> 272
/*     */     //   #71	-> 314
/*     */     //   #74	-> 316
/*     */     //   #75	-> 358
/*     */     //   #78	-> 360
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   205	10	3	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   183	36	4	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   221	19	2	entityIsFlying	Z
/*     */     //   294	22	2	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   338	22	2	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	362	0	this	Lnet/minecraft/advancements/predicates/entity/EntityFlagsPredicate;
/*     */     //   0	362	1	entity	Lnet/minecraft/world/entity/Entity;
/*     */   }
/*     */   
/*     */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/*  83 */     return matches(entity);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  87 */     private Optional<Boolean> isOnGround = Optional.empty();
/*     */     
/*  88 */     private Optional<Boolean> isOnFire = Optional.empty();
/*     */     
/*  89 */     private Optional<Boolean> isCrouching = Optional.empty();
/*     */     
/*  90 */     private Optional<Boolean> isSprinting = Optional.empty();
/*     */     
/*  91 */     private Optional<Boolean> isSwimming = Optional.empty();
/*     */     
/*  92 */     private Optional<Boolean> isFlying = Optional.empty();
/*     */     
/*  93 */     private Optional<Boolean> isBaby = Optional.empty();
/*     */     
/*  94 */     private Optional<Boolean> isInWater = Optional.empty();
/*     */     
/*  95 */     private Optional<Boolean> isFallFlying = Optional.empty();
/*     */     
/*     */     public static Builder flags() {
/*  98 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder setOnGround(Boolean onGround) {
/* 102 */       this.isOnGround = Optional.of(onGround);
/* 103 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setOnFire(Boolean onFire) {
/* 107 */       this.isOnFire = Optional.of(onFire);
/* 108 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCrouching(Boolean crouching) {
/* 112 */       this.isCrouching = Optional.of(crouching);
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSprinting(Boolean sprinting) {
/* 117 */       this.isSprinting = Optional.of(sprinting);
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSwimming(Boolean swimming) {
/* 122 */       this.isSwimming = Optional.of(swimming);
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsFlying(Boolean flying) {
/* 127 */       this.isFlying = Optional.of(flying);
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsBaby(Boolean baby) {
/* 132 */       this.isBaby = Optional.of(baby);
/* 133 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsInWater(Boolean inWater) {
/* 137 */       this.isInWater = Optional.of(inWater);
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsFallFlying(Boolean fallFlying) {
/* 142 */       this.isFallFlying = Optional.of(fallFlying);
/* 143 */       return this;
/*     */     }
/*     */     
/*     */     public EntityFlagsPredicate build() {
/* 147 */       return new EntityFlagsPredicate(this.isOnGround, this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isFlying, this.isBaby, this.isInWater, this.isFallFlying);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityFlagsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
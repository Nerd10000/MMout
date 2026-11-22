/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ final class YConditionSource extends Record implements SurfaceRules.ConditionSource {
/*     */   private final VerticalAnchor anchor;
/*     */   
/*     */   private final int surfaceDepthMultiplier;
/*     */   
/*     */   private final boolean addStoneDepth;
/*     */   
/*     */   private static final MapCodec<YConditionSource> CODEC;
/*     */   
/*     */   private YConditionSource(VerticalAnchor anchor, int surfaceDepthMultiplier, boolean addStoneDepth) {
/* 594 */     this.anchor = anchor;
/* 594 */     this.surfaceDepthMultiplier = surfaceDepthMultiplier;
/* 594 */     this.addStoneDepth = addStoneDepth;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #594	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #594	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #594	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public VerticalAnchor anchor() {
/* 594 */     return this.anchor;
/*     */   }
/*     */   
/*     */   public int surfaceDepthMultiplier() {
/* 594 */     return this.surfaceDepthMultiplier;
/*     */   }
/*     */   
/*     */   public boolean addStoneDepth() {
/* 594 */     return this.addStoneDepth;
/*     */   }
/*     */   
/*     */   static {
/* 595 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("anchor").forGetter(YConditionSource::anchor), (App)Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(YConditionSource::surfaceDepthMultiplier), (App)Codec.BOOL.fieldOf("add_stone_depth").forGetter(YConditionSource::addStoneDepth)).apply((Applicative)i, YConditionSource::new));
/*     */   }
/*     */   
/*     */   public MapCodec<YConditionSource> codec() {
/* 603 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */     class YCondition extends SurfaceRules.LazyYCondition {
/*     */       private YCondition() {
/* 610 */         super(param2Context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 615 */         return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier);
/*     */       }
/*     */     };
/* 619 */     return new YCondition();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$YConditionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
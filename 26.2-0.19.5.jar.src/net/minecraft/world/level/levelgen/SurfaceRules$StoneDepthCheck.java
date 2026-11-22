/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ 
/*     */ final class StoneDepthCheck extends Record implements SurfaceRules.ConditionSource {
/*     */   private final int offset;
/*     */   
/*     */   private final boolean addSurfaceDepth;
/*     */   
/*     */   private final int secondaryDepthRange;
/*     */   
/*     */   private final CaveSurface surfaceType;
/*     */   
/*     */   private static final MapCodec<StoneDepthCheck> CODEC;
/*     */   
/*     */   private StoneDepthCheck(int offset, boolean addSurfaceDepth, int secondaryDepthRange, CaveSurface surfaceType) {
/* 527 */     this.offset = offset;
/* 527 */     this.addSurfaceDepth = addSurfaceDepth;
/* 527 */     this.secondaryDepthRange = secondaryDepthRange;
/* 527 */     this.surfaceType = surfaceType;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #527	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #527	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #527	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int offset() {
/* 527 */     return this.offset;
/*     */   }
/*     */   
/*     */   public boolean addSurfaceDepth() {
/* 527 */     return this.addSurfaceDepth;
/*     */   }
/*     */   
/*     */   public int secondaryDepthRange() {
/* 527 */     return this.secondaryDepthRange;
/*     */   }
/*     */   
/*     */   public CaveSurface surfaceType() {
/* 527 */     return this.surfaceType;
/*     */   }
/*     */   
/*     */   static {
/* 528 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("offset").forGetter(StoneDepthCheck::offset), (App)Codec.BOOL.fieldOf("add_surface_depth").forGetter(StoneDepthCheck::addSurfaceDepth), (App)Codec.INT.fieldOf("secondary_depth_range").forGetter(StoneDepthCheck::secondaryDepthRange), (App)CaveSurface.CODEC.fieldOf("surface_type").forGetter(StoneDepthCheck::surfaceType)).apply((Applicative)i, StoneDepthCheck::new));
/*     */   }
/*     */   
/*     */   public MapCodec<StoneDepthCheck> codec() {
/* 538 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 543 */     final boolean ceiling = (this.surfaceType == CaveSurface.CEILING);
/*     */     class StoneDepthCondition extends SurfaceRules.LazyYCondition {
/*     */       private StoneDepthCondition() {
/* 547 */         super(param2Context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 552 */         int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove;
/* 553 */         int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/* 554 */         int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/* 556 */         return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth);
/*     */       }
/*     */     };
/* 560 */     return new StoneDepthCondition();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$StoneDepthCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
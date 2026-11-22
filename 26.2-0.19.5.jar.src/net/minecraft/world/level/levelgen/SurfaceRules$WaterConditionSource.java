/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ final class WaterConditionSource extends Record implements SurfaceRules.ConditionSource {
/*     */   private final int offset;
/*     */   
/*     */   private final int surfaceDepthMultiplier;
/*     */   
/*     */   private final boolean addStoneDepth;
/*     */   
/*     */   private static final MapCodec<WaterConditionSource> CODEC;
/*     */   
/*     */   private WaterConditionSource(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) {
/* 623 */     this.offset = offset;
/* 623 */     this.surfaceDepthMultiplier = surfaceDepthMultiplier;
/* 623 */     this.addStoneDepth = addStoneDepth;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #623	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #623	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #623	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int offset() {
/* 623 */     return this.offset;
/*     */   }
/*     */   
/*     */   public int surfaceDepthMultiplier() {
/* 623 */     return this.surfaceDepthMultiplier;
/*     */   }
/*     */   
/*     */   public boolean addStoneDepth() {
/* 623 */     return this.addStoneDepth;
/*     */   }
/*     */   
/*     */   static {
/* 624 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("offset").forGetter(WaterConditionSource::offset), (App)Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(WaterConditionSource::surfaceDepthMultiplier), (App)Codec.BOOL.fieldOf("add_stone_depth").forGetter(WaterConditionSource::addStoneDepth)).apply((Applicative)i, WaterConditionSource::new));
/*     */   }
/*     */   
/*     */   public MapCodec<WaterConditionSource> codec() {
/* 632 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */     class WaterCondition extends SurfaceRules.LazyYCondition {
/*     */       private WaterCondition() {
/* 639 */         super(param2Context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 644 */         return (this.context.waterHeight == Integer.MIN_VALUE || this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier);
/*     */       }
/*     */     };
/* 648 */     return new WaterCondition();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$WaterConditionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
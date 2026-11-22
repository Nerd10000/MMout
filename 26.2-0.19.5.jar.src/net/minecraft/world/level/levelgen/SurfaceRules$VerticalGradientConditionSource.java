/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ final class VerticalGradientConditionSource extends Record implements SurfaceRules.ConditionSource {
/*     */   private final Identifier randomName;
/*     */   
/*     */   private final VerticalAnchor trueAtAndBelow;
/*     */   
/*     */   private final VerticalAnchor falseAtAndAbove;
/*     */   
/*     */   private static final MapCodec<VerticalGradientConditionSource> CODEC;
/*     */   
/*     */   private VerticalGradientConditionSource(Identifier randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) {
/* 741 */     this.randomName = randomName;
/* 741 */     this.trueAtAndBelow = trueAtAndBelow;
/* 741 */     this.falseAtAndAbove = falseAtAndAbove;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #741	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #741	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #741	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier randomName() {
/* 741 */     return this.randomName;
/*     */   }
/*     */   
/*     */   public VerticalAnchor trueAtAndBelow() {
/* 741 */     return this.trueAtAndBelow;
/*     */   }
/*     */   
/*     */   public VerticalAnchor falseAtAndAbove() {
/* 741 */     return this.falseAtAndAbove;
/*     */   }
/*     */   
/*     */   static {
/* 742 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("random_name").forGetter(VerticalGradientConditionSource::randomName), (App)VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(VerticalGradientConditionSource::trueAtAndBelow), (App)VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(VerticalGradientConditionSource::falseAtAndAbove)).apply((Applicative)i, VerticalGradientConditionSource::new));
/*     */   }
/*     */   
/*     */   public MapCodec<VerticalGradientConditionSource> codec() {
/* 750 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 755 */     final int trueAtAndBelow = trueAtAndBelow().resolveY(ruleContext.context);
/* 756 */     final int falseAtAndAbove = falseAtAndAbove().resolveY(ruleContext.context);
/* 757 */     final PositionalRandomFactory randomFactory = ruleContext.randomState.getOrCreateRandomFactory(randomName());
/*     */     class VerticalGradientCondition extends SurfaceRules.LazyYCondition {
/*     */       private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) {
/* 761 */         super(param2Context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 766 */         int blockY = this.context.blockY;
/* 767 */         if (blockY <= trueAtAndBelow)
/* 768 */           return true; 
/* 770 */         if (blockY >= falseAtAndAbove)
/* 771 */           return false; 
/* 773 */         double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D);
/* 774 */         RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
/* 775 */         return (random.nextFloat() < probability);
/*     */       }
/*     */     };
/* 778 */     return new VerticalGradientCondition(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$VerticalGradientConditionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
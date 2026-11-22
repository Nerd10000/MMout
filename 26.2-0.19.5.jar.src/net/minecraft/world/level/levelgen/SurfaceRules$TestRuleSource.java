/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ final class TestRuleSource extends Record implements SurfaceRules.RuleSource {
/*     */   private final SurfaceRules.ConditionSource ifTrue;
/*     */   
/*     */   private final SurfaceRules.RuleSource thenRun;
/*     */   
/*     */   private static final MapCodec<TestRuleSource> CODEC;
/*     */   
/*     */   private TestRuleSource(SurfaceRules.ConditionSource ifTrue, SurfaceRules.RuleSource thenRun) {
/* 830 */     this.ifTrue = ifTrue;
/* 830 */     this.thenRun = thenRun;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #830	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #830	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #830	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SurfaceRules.ConditionSource ifTrue() {
/* 830 */     return this.ifTrue;
/*     */   }
/*     */   
/*     */   public SurfaceRules.RuleSource thenRun() {
/* 830 */     return this.thenRun;
/*     */   }
/*     */   
/*     */   static {
/* 831 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SurfaceRules.ConditionSource.CODEC.fieldOf("if_true").forGetter(TestRuleSource::ifTrue), (App)SurfaceRules.RuleSource.CODEC.fieldOf("then_run").forGetter(TestRuleSource::thenRun)).apply((Applicative)i, TestRuleSource::new));
/*     */   }
/*     */   
/*     */   public MapCodec<TestRuleSource> codec() {
/* 838 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 843 */     return new SurfaceRules.TestRule(this.ifTrue.apply(context), this.thenRun.apply(context));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$TestRuleSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ final class NotConditionSource extends Record implements SurfaceRules.ConditionSource {
/*     */   private final SurfaceRules.ConditionSource target;
/*     */   
/*     */   private NotConditionSource(SurfaceRules.ConditionSource target) {
/* 513 */     this.target = target;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #513	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #513	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #513	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SurfaceRules.ConditionSource target() {
/* 513 */     return this.target;
/*     */   }
/*     */   
/* 514 */   private static final MapCodec<NotConditionSource> CODEC = SurfaceRules.ConditionSource.CODEC.xmap(NotConditionSource::new, NotConditionSource::target).fieldOf("invert");
/*     */   
/*     */   public MapCodec<NotConditionSource> codec() {
/* 518 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 523 */     return new SurfaceRules.NotCondition(this.target.apply(context));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$NotConditionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
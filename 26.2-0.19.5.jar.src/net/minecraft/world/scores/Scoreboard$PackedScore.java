/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ public final class PackedScore extends Record {
/*     */   private final String owner;
/*     */   
/*     */   private final String objective;
/*     */   
/*     */   private final Score.Packed score;
/*     */   
/*     */   public static final Codec<PackedScore> CODEC;
/*     */   
/*     */   public PackedScore(String owner, String objective, Score.Packed score) {
/* 419 */     this.owner = owner;
/* 419 */     this.objective = objective;
/* 419 */     this.score = score;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Scoreboard$PackedScore;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #419	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/Scoreboard$PackedScore;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Scoreboard$PackedScore;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #419	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/Scoreboard$PackedScore;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Scoreboard$PackedScore;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #419	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/scores/Scoreboard$PackedScore;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String owner() {
/* 419 */     return this.owner;
/*     */   }
/*     */   
/*     */   public String objective() {
/* 419 */     return this.objective;
/*     */   }
/*     */   
/*     */   public Score.Packed score() {
/* 419 */     return this.score;
/*     */   }
/*     */   
/*     */   static {
/* 420 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("Name").forGetter(PackedScore::owner), (App)Codec.STRING.fieldOf("Objective").forGetter(PackedScore::objective), (App)Score.Packed.MAP_CODEC.forGetter(PackedScore::score)).apply((Applicative)i, PackedScore::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\Scoreboard$PackedScore.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
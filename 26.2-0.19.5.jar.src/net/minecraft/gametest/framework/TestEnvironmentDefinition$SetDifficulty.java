/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ 
/*     */ public final class SetDifficulty extends Record implements TestEnvironmentDefinition<Difficulty> {
/*     */   private final Difficulty difficulty;
/*     */   
/*     */   public static final MapCodec<SetDifficulty> CODEC;
/*     */   
/*     */   public SetDifficulty(Difficulty difficulty) {
/* 299 */     this.difficulty = difficulty;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #299	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #299	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #299	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetDifficulty;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Difficulty difficulty() {
/* 299 */     return this.difficulty;
/*     */   }
/*     */   
/*     */   static {
/* 300 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Difficulty.CODEC.fieldOf("difficulty").forGetter(SetDifficulty::difficulty)).apply((Applicative)i, SetDifficulty::new));
/*     */   }
/*     */   
/*     */   public Difficulty setup(ServerLevel level) {
/* 306 */     Difficulty oldDifficulty = level.getDifficulty();
/* 307 */     level.getServer().setDifficulty(this.difficulty, true);
/* 308 */     return oldDifficulty;
/*     */   }
/*     */   
/*     */   public void teardown(ServerLevel level, Difficulty saveData) {
/* 313 */     level.getServer().setDifficulty(saveData, true);
/*     */   }
/*     */   
/*     */   public MapCodec<SetDifficulty> codec() {
/* 318 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$SetDifficulty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
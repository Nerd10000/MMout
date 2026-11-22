/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public final class DifficultySettings extends Record {
/*    */   private final Difficulty difficulty;
/*    */   
/*    */   private final boolean hardcore;
/*    */   
/*    */   private final boolean locked;
/*    */   
/*    */   public DifficultySettings(Difficulty difficulty, boolean hardcore, boolean locked) {
/* 50 */     this.difficulty = difficulty;
/* 50 */     this.hardcore = hardcore;
/* 50 */     this.locked = locked;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/LevelSettings$DifficultySettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/LevelSettings$DifficultySettings;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/LevelSettings$DifficultySettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/LevelSettings$DifficultySettings;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/LevelSettings$DifficultySettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/LevelSettings$DifficultySettings;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Difficulty difficulty() {
/* 50 */     return this.difficulty;
/*    */   }
/*    */   
/*    */   public boolean hardcore() {
/* 50 */     return this.hardcore;
/*    */   }
/*    */   
/*    */   public boolean locked() {
/* 50 */     return this.locked;
/*    */   }
/*    */   
/* 51 */   public static final DifficultySettings DEFAULT = new DifficultySettings(Difficulty.NORMAL, false, false);
/*    */   
/*    */   public static final Codec<DifficultySettings> CODEC;
/*    */   
/*    */   static {
/* 52 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Difficulty.CODEC.fieldOf("difficulty").forGetter(DifficultySettings::difficulty), (App)Codec.BOOL.fieldOf("hardcore").forGetter(DifficultySettings::hardcore), (App)Codec.BOOL.fieldOf("locked").forGetter(DifficultySettings::locked)).apply((Applicative)i, DifficultySettings::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\LevelSettings$DifficultySettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ final class UniformBonusCount extends Record implements ApplyBonusCount.Formula {
/*    */   private final int bonusMultiplier;
/*    */   
/*    */   public static final Codec<UniformBonusCount> CODEC;
/*    */   
/*    */   private UniformBonusCount(int bonusMultiplier) {
/* 61 */     this.bonusMultiplier = bonusMultiplier;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int bonusMultiplier() {
/* 61 */     return this.bonusMultiplier;
/*    */   }
/*    */   
/*    */   static {
/* 62 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("bonusMultiplier").forGetter(UniformBonusCount::bonusMultiplier)).apply((Applicative)i, UniformBonusCount::new));
/*    */   }
/*    */   
/* 66 */   public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("uniform_bonus_count"), (Codec)CODEC);
/*    */   
/*    */   public int calculateNewCount(RandomSource random, int count, int level) {
/* 70 */     return count + random.nextInt(this.bonusMultiplier * level + 1);
/*    */   }
/*    */   
/*    */   public ApplyBonusCount.FormulaType getType() {
/* 75 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$UniformBonusCount.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
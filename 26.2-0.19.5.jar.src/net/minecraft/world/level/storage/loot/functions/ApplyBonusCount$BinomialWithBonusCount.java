/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ final class BinomialWithBonusCount extends Record implements ApplyBonusCount.Formula {
/*    */   private final int extraRounds;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   private static final Codec<BinomialWithBonusCount> CODEC;
/*    */   
/*    */   private BinomialWithBonusCount(int extraRounds, float probability) {
/* 37 */     this.extraRounds = extraRounds;
/* 37 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int extraRounds() {
/* 37 */     return this.extraRounds;
/*    */   }
/*    */   
/*    */   public float probability() {
/* 37 */     return this.probability;
/*    */   }
/*    */   
/*    */   static {
/* 38 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("extra").forGetter(BinomialWithBonusCount::extraRounds), (App)Codec.FLOAT.fieldOf("probability").forGetter(BinomialWithBonusCount::probability)).apply((Applicative)i, BinomialWithBonusCount::new));
/*    */   }
/*    */   
/* 43 */   public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("binomial_with_bonus_count"), (Codec)CODEC);
/*    */   
/*    */   public int calculateNewCount(RandomSource random, int count, int level) {
/* 47 */     for (int i = 0; i < level + this.extraRounds; i++) {
/* 48 */       if (random.nextFloat() < this.probability)
/* 49 */         count++; 
/*    */     } 
/* 52 */     return count;
/*    */   }
/*    */   
/*    */   public ApplyBonusCount.FormulaType getType() {
/* 57 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$BinomialWithBonusCount.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
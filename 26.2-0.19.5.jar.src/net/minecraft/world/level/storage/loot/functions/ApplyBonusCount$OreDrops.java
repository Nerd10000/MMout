/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ final class OreDrops extends Record implements ApplyBonusCount.Formula {
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #79	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #79	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #79	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*  80 */   public static final OreDrops INSTANCE = new OreDrops();
/*     */   
/*  82 */   public static final Codec<OreDrops> CODEC = MapCodec.unitCodec(INSTANCE);
/*     */   
/*  83 */   public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("ore_drops"), (Codec)CODEC);
/*     */   
/*     */   public int calculateNewCount(RandomSource random, int count, int level) {
/*  87 */     if (level > 0) {
/*  88 */       int bonus = random.nextInt(level + 2) - 1;
/*  89 */       if (bonus < 0)
/*  90 */         bonus = 0; 
/*  92 */       return count * (bonus + 1);
/*     */     } 
/*  95 */     return count;
/*     */   }
/*     */   
/*     */   public ApplyBonusCount.FormulaType getType() {
/* 100 */     return TYPE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$OreDrops.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
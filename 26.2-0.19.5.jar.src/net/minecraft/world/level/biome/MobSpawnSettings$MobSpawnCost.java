/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class MobSpawnCost extends Record {
/*    */   private final double energyBudget;
/*    */   
/*    */   private final double charge;
/*    */   
/*    */   public static final Codec<MobSpawnCost> CODEC;
/*    */   
/*    */   public MobSpawnCost(double energyBudget, double charge) {
/* 89 */     this.energyBudget = energyBudget;
/* 89 */     this.charge = charge;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public double energyBudget() {
/* 89 */     return this.energyBudget;
/*    */   }
/*    */   
/*    */   public double charge() {
/* 89 */     return this.charge;
/*    */   }
/*    */   
/*    */   static {
/* 90 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.DOUBLE.fieldOf("energy_budget").forGetter(()), (App)Codec.DOUBLE.fieldOf("charge").forGetter(())).apply((Applicative)i, MobSpawnCost::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\MobSpawnSettings$MobSpawnCost.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
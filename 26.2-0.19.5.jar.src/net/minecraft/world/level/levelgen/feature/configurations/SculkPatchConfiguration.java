/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import com.mojang.datafixers.util.Function7;
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*   */ import net.minecraft.util.valueproviders.IntProvider;
/*   */ import net.minecraft.util.valueproviders.IntProviders;
/*   */ 
/*   */ public final class SculkPatchConfiguration extends Record implements FeatureConfiguration {
/*   */   private final int chargeCount;
/*   */   
/*   */   private final int amountPerCharge;
/*   */   
/*   */   private final int spreadAttempts;
/*   */   
/*   */   private final int growthRounds;
/*   */   
/*   */   private final int spreadRounds;
/*   */   
/*   */   private final IntProvider extraRareGrowths;
/*   */   
/*   */   private final float catalystChance;
/*   */   
/*   */   public static final Codec<SculkPatchConfiguration> CODEC;
/*   */   
/*   */   public SculkPatchConfiguration(int chargeCount, int amountPerCharge, int spreadAttempts, int growthRounds, int spreadRounds, IntProvider extraRareGrowths, float catalystChance) {
/* 8 */     this.chargeCount = chargeCount;
/* 8 */     this.amountPerCharge = amountPerCharge;
/* 8 */     this.spreadAttempts = spreadAttempts;
/* 8 */     this.growthRounds = growthRounds;
/* 8 */     this.spreadRounds = spreadRounds;
/* 8 */     this.extraRareGrowths = extraRareGrowths;
/* 8 */     this.catalystChance = catalystChance;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public int chargeCount() {
/* 8 */     return this.chargeCount;
/*   */   }
/*   */   
/*   */   public int amountPerCharge() {
/* 8 */     return this.amountPerCharge;
/*   */   }
/*   */   
/*   */   public int spreadAttempts() {
/* 8 */     return this.spreadAttempts;
/*   */   }
/*   */   
/*   */   public int growthRounds() {
/* 8 */     return this.growthRounds;
/*   */   }
/*   */   
/*   */   public int spreadRounds() {
/* 8 */     return this.spreadRounds;
/*   */   }
/*   */   
/*   */   public IntProvider extraRareGrowths() {
/* 8 */     return this.extraRareGrowths;
/*   */   }
/*   */   
/*   */   public float catalystChance() {
/* 8 */     return this.catalystChance;
/*   */   }
/*   */   
/*   */   static {
/* 9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(1, 32).fieldOf("charge_count").forGetter(SculkPatchConfiguration::chargeCount), (App)Codec.intRange(1, 500).fieldOf("amount_per_charge").forGetter(SculkPatchConfiguration::amountPerCharge), (App)Codec.intRange(1, 64).fieldOf("spread_attempts").forGetter(SculkPatchConfiguration::spreadAttempts), (App)Codec.intRange(0, 8).fieldOf("growth_rounds").forGetter(SculkPatchConfiguration::growthRounds), (App)Codec.intRange(0, 8).fieldOf("spread_rounds").forGetter(SculkPatchConfiguration::spreadRounds), (App)IntProviders.CODEC.fieldOf("extra_rare_growths").forGetter(SculkPatchConfiguration::extraRareGrowths), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("catalyst_chance").forGetter(SculkPatchConfiguration::catalystChance)).apply((Applicative)i, SculkPatchConfiguration::new));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\SculkPatchConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
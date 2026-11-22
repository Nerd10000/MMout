/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalLong;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class WorldOptions {
/*    */   public static final MapCodec<WorldOptions> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.LONG.fieldOf("seed").stable().forGetter(WorldOptions::seed), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "generate_structures", Boolean.valueOf(true)).stable().forGetter(WorldOptions::generateStructures), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "bonus_chest", Boolean.valueOf(false)).stable().forGetter(WorldOptions::generateBonusChest), (App)Codec.STRING.lenientOptionalFieldOf("legacy_custom_options").stable().forGetter(())).apply((Applicative)i, i.stable(WorldOptions::new)));
/*    */   }
/*    */   
/* 21 */   public static final WorldOptions DEMO_OPTIONS = new WorldOptions("North Carolina".hashCode(), true, true);
/*    */   
/*    */   private final long seed;
/*    */   
/*    */   private final boolean generateStructures;
/*    */   
/*    */   private final boolean generateBonusChest;
/*    */   
/*    */   private final Optional<String> legacyCustomOptions;
/*    */   
/*    */   public WorldOptions(long seed, boolean generateStructures, boolean generateBonusChest) {
/* 30 */     this(seed, generateStructures, generateBonusChest, Optional.empty());
/*    */   }
/*    */   
/*    */   public static WorldOptions defaultWithRandomSeed() {
/* 34 */     return new WorldOptions(randomSeed(), true, false);
/*    */   }
/*    */   
/*    */   public static WorldOptions testWorldWithRandomSeed() {
/* 38 */     return new WorldOptions(randomSeed(), false, false);
/*    */   }
/*    */   
/*    */   private WorldOptions(long seed, boolean generateStructures, boolean generateBonusChest, Optional<String> legacyCustomOptions) {
/* 42 */     this.seed = seed;
/* 43 */     this.generateStructures = generateStructures;
/* 44 */     this.generateBonusChest = generateBonusChest;
/* 45 */     this.legacyCustomOptions = legacyCustomOptions;
/*    */   }
/*    */   
/*    */   public long seed() {
/* 49 */     return this.seed;
/*    */   }
/*    */   
/*    */   public boolean generateStructures() {
/* 53 */     return this.generateStructures;
/*    */   }
/*    */   
/*    */   public boolean generateBonusChest() {
/* 57 */     return this.generateBonusChest;
/*    */   }
/*    */   
/*    */   public boolean isOldCustomizedWorld() {
/* 61 */     return this.legacyCustomOptions.isPresent();
/*    */   }
/*    */   
/*    */   public WorldOptions withBonusChest(boolean generateBonusChest) {
/* 65 */     return new WorldOptions(this.seed, this.generateStructures, generateBonusChest, this.legacyCustomOptions);
/*    */   }
/*    */   
/*    */   public WorldOptions withStructures(boolean generateStructures) {
/* 69 */     return new WorldOptions(this.seed, generateStructures, this.generateBonusChest, this.legacyCustomOptions);
/*    */   }
/*    */   
/*    */   public WorldOptions withSeed(OptionalLong seed) {
/* 73 */     return new WorldOptions(seed.orElse(randomSeed()), this.generateStructures, this.generateBonusChest, this.legacyCustomOptions);
/*    */   }
/*    */   
/*    */   public static OptionalLong parseSeed(String seedString) {
/* 77 */     seedString = seedString.trim();
/* 79 */     if (StringUtils.isEmpty(seedString))
/* 80 */       return OptionalLong.empty(); 
/*    */     try {
/* 84 */       return OptionalLong.of(Long.parseLong(seedString));
/* 85 */     } catch (NumberFormatException e) {
/* 87 */       return OptionalLong.of(seedString.hashCode());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static long randomSeed() {
/* 92 */     return RandomSource.create().nextLong();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\WorldOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
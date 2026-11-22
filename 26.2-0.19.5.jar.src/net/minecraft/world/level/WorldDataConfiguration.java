/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ 
/*    */ public final class WorldDataConfiguration extends Record {
/*    */   private final DataPackConfig dataPacks;
/*    */   
/*    */   private final FeatureFlagSet enabledFeatures;
/*    */   
/*    */   public static final String ENABLED_FEATURES_ID = "enabled_features";
/*    */   
/*    */   public static final MapCodec<WorldDataConfiguration> MAP_CODEC;
/*    */   
/*    */   public WorldDataConfiguration(DataPackConfig dataPacks, FeatureFlagSet enabledFeatures) {
/*  9 */     this.dataPacks = dataPacks;
/*  9 */     this.enabledFeatures = enabledFeatures;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/WorldDataConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/WorldDataConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/WorldDataConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/WorldDataConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/WorldDataConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/WorldDataConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public DataPackConfig dataPacks() {
/*  9 */     return this.dataPacks;
/*    */   }
/*    */   
/*    */   public FeatureFlagSet enabledFeatures() {
/*  9 */     return this.enabledFeatures;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DataPackConfig.CODEC.lenientOptionalFieldOf("DataPacks", DataPackConfig.DEFAULT).forGetter(WorldDataConfiguration::dataPacks), (App)FeatureFlags.CODEC.lenientOptionalFieldOf("enabled_features", FeatureFlags.DEFAULT_FLAGS).forGetter(WorldDataConfiguration::enabledFeatures)).apply((Applicative)i, WorldDataConfiguration::new));
/*    */   }
/*    */   
/* 16 */   public static final Codec<WorldDataConfiguration> CODEC = MAP_CODEC.codec();
/*    */   
/* 18 */   public static final WorldDataConfiguration DEFAULT = new WorldDataConfiguration(DataPackConfig.DEFAULT, FeatureFlags.DEFAULT_FLAGS);
/*    */   
/*    */   public WorldDataConfiguration expandFeatures(FeatureFlagSet newEnabledFeatures) {
/* 24 */     return new WorldDataConfiguration(this.dataPacks, this.enabledFeatures.join(newEnabledFeatures));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\WorldDataConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
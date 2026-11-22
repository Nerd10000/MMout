/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function10;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class LargeDripstoneConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<LargeDripstoneConfiguration> CODEC;
/*    */   
/*    */   public final HolderSet<Block> replaceableBlocks;
/*    */   
/*    */   public final int floorToCeilingSearchRange;
/*    */   
/*    */   public final IntProvider columnRadius;
/*    */   
/*    */   public final FloatProvider heightScale;
/*    */   
/*    */   public final float maxColumnRadiusToCaveHeightRatio;
/*    */   
/*    */   public final FloatProvider stalactiteBluntness;
/*    */   
/*    */   public final FloatProvider stalagmiteBluntness;
/*    */   
/*    */   public final FloatProvider windSpeed;
/*    */   
/*    */   public final int minRadiusForWind;
/*    */   
/*    */   public final float minBluntnessForWind;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(()), (App)Codec.intRange(1, 512).optionalFieldOf("floor_to_ceiling_search_range", Integer.valueOf(30)).forGetter(()), (App)IntProviders.codec(1, 16).fieldOf("column_radius").forGetter(()), (App)FloatProviders.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(()), (App)Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(()), (App)FloatProviders.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter(()), (App)FloatProviders.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter(()), (App)FloatProviders.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(()), (App)Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(()), (App)Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(())).apply((Applicative)i, LargeDripstoneConfiguration::new));
/*    */   }
/*    */   
/*    */   public LargeDripstoneConfiguration(HolderSet<Block> replaceableBlocks, int floorToCeilingSearchRange, IntProvider columnRadius, FloatProvider heightScale, float maxColumnRadiusToCaveHeightRatio, FloatProvider stalactiteBluntness, FloatProvider stalagmiteBluntness, FloatProvider windSpeed, int minRadiusForWind, float minBluntnessForWind) {
/* 53 */     this.replaceableBlocks = replaceableBlocks;
/* 54 */     this.floorToCeilingSearchRange = floorToCeilingSearchRange;
/* 55 */     this.columnRadius = columnRadius;
/* 56 */     this.heightScale = heightScale;
/* 57 */     this.maxColumnRadiusToCaveHeightRatio = maxColumnRadiusToCaveHeightRatio;
/* 58 */     this.stalactiteBluntness = stalactiteBluntness;
/* 59 */     this.stalagmiteBluntness = stalagmiteBluntness;
/* 60 */     this.windSpeed = windSpeed;
/* 61 */     this.minRadiusForWind = minRadiusForWind;
/* 62 */     this.minBluntnessForWind = minBluntnessForWind;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\LargeDripstoneConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
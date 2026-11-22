/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class UnderwaterMagmaConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<UnderwaterMagmaConfiguration> CODEC;
/*    */   
/*    */   public final int floorSearchRange;
/*    */   
/*    */   public final int placementRadiusAroundFloor;
/*    */   
/*    */   public final float placementProbabilityPerValidPosition;
/*    */   
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, 512).fieldOf("floor_search_range").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("placement_radius_around_floor").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("placement_probability_per_valid_position").forGetter(())).apply((Applicative)i, UnderwaterMagmaConfiguration::new));
/*    */   }
/*    */   
/*    */   public UnderwaterMagmaConfiguration(int floorSearchRange, int placementRadiusAroundFloor, float placementProbabilityPerValidPosition) {
/* 18 */     this.floorSearchRange = floorSearchRange;
/* 19 */     this.placementRadiusAroundFloor = placementRadiusAroundFloor;
/* 20 */     this.placementProbabilityPerValidPosition = placementProbabilityPerValidPosition;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\UnderwaterMagmaConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
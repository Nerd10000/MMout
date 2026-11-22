/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class SurfaceRelativeThresholdFilter extends PlacementFilter {
/*    */   public static final MapCodec<SurfaceRelativeThresholdFilter> CODEC;
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */   
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(()), (App)Codec.INT.optionalFieldOf("min_inclusive", Integer.valueOf(-2147483648)).forGetter(()), (App)Codec.INT.optionalFieldOf("max_inclusive", Integer.valueOf(2147483647)).forGetter(())).apply((Applicative)i, SurfaceRelativeThresholdFilter::new));
/*    */   }
/*    */   
/*    */   private SurfaceRelativeThresholdFilter(Heightmap.Types heightmap, int minInclusive, int maxInclusive) {
/* 24 */     this.heightmap = heightmap;
/* 25 */     this.minInclusive = minInclusive;
/* 26 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public static SurfaceRelativeThresholdFilter of(Heightmap.Types heightmap, int minInclusive, int maxInclusive) {
/* 30 */     return new SurfaceRelativeThresholdFilter(heightmap, minInclusive, maxInclusive);
/*    */   }
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 35 */     long surfaceY = context.getHeight(this.heightmap, origin.getX(), origin.getZ());
/* 37 */     long minY = surfaceY + this.minInclusive;
/* 38 */     long maxY = surfaceY + this.maxInclusive;
/* 40 */     return (minY <= origin.getY() && origin.getY() <= maxY);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 45 */     return PlacementModifierType.SURFACE_RELATIVE_THRESHOLD_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\SurfaceRelativeThresholdFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
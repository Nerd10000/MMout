/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class SurfaceWaterDepthFilter extends PlacementFilter {
/*    */   public static final MapCodec<SurfaceWaterDepthFilter> CODEC;
/*    */   
/*    */   private final int maxWaterDepth;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("max_water_depth").forGetter(())).apply((Applicative)i, SurfaceWaterDepthFilter::new));
/*    */   }
/*    */   
/*    */   private SurfaceWaterDepthFilter(int maxWaterDepth) {
/* 21 */     this.maxWaterDepth = maxWaterDepth;
/*    */   }
/*    */   
/*    */   public static SurfaceWaterDepthFilter forMaxDepth(int maxWaterDepth) {
/* 25 */     return new SurfaceWaterDepthFilter(maxWaterDepth);
/*    */   }
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 30 */     int yOceanFloor = context.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX(), origin.getZ());
/* 31 */     int ySurfaceFloor = context.getHeight(Heightmap.Types.WORLD_SURFACE, origin.getX(), origin.getZ());
/* 33 */     return (ySurfaceFloor - yOceanFloor <= this.maxWaterDepth);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 38 */     return PlacementModifierType.SURFACE_WATER_DEPTH_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\SurfaceWaterDepthFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
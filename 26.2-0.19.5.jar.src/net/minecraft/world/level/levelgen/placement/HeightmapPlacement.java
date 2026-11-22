/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class HeightmapPlacement extends PlacementModifier {
/*    */   public static final MapCodec<HeightmapPlacement> CODEC;
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(())).apply((Applicative)i, HeightmapPlacement::new));
/*    */   }
/*    */   
/*    */   private HeightmapPlacement(Heightmap.Types heightmap) {
/* 20 */     this.heightmap = heightmap;
/*    */   }
/*    */   
/*    */   public static HeightmapPlacement onHeightmap(Heightmap.Types heightmap) {
/* 24 */     return new HeightmapPlacement(heightmap);
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 29 */     int x = origin.getX();
/* 30 */     int z = origin.getZ();
/* 31 */     int height = context.getHeight(this.heightmap, x, z);
/* 32 */     if (height > context.getMinY())
/* 33 */       return Stream.of(new BlockPos(x, height, z)); 
/* 35 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 40 */     return PlacementModifierType.HEIGHTMAP;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\HeightmapPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
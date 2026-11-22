/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FixedPlacement extends PlacementModifier {
/*    */   public static final MapCodec<FixedPlacement> CODEC;
/*    */   
/*    */   private final List<BlockPos> positions;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPos.CODEC.listOf().fieldOf("positions").forGetter(())).apply((Applicative)i, FixedPlacement::new));
/*    */   }
/*    */   
/*    */   public static FixedPlacement of(BlockPos... pos) {
/* 21 */     return new FixedPlacement(List.of(pos));
/*    */   }
/*    */   
/*    */   private FixedPlacement(List<BlockPos> positions) {
/* 25 */     this.positions = positions;
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 30 */     int chunkX = SectionPos.blockToSectionCoord(origin.getX());
/* 31 */     int chunkZ = SectionPos.blockToSectionCoord(origin.getZ());
/* 32 */     boolean hasPositions = false;
/* 33 */     for (BlockPos position : this.positions) {
/* 34 */       if (isSameChunk(chunkX, chunkZ, position)) {
/* 35 */         hasPositions = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 39 */     if (!hasPositions)
/* 40 */       return Stream.empty(); 
/* 42 */     return this.positions.stream().filter(pos -> isSameChunk(chunkX, chunkZ, pos));
/*    */   }
/*    */   
/*    */   private static boolean isSameChunk(int chunkX, int chunkZ, BlockPos position) {
/* 46 */     return (chunkX == SectionPos.blockToSectionCoord(position.getX()) && chunkZ == SectionPos.blockToSectionCoord(position.getZ()));
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 51 */     return PlacementModifierType.FIXED_PLACEMENT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\FixedPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
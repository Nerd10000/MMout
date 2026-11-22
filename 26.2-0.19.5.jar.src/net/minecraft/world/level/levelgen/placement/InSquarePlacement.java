/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class InSquarePlacement extends PlacementModifier {
/* 14 */   private static final InSquarePlacement INSTANCE = new InSquarePlacement();
/*    */   
/* 16 */   public static final MapCodec<InSquarePlacement> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*    */   public static InSquarePlacement spread() {
/* 19 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 24 */     int x = random.nextInt(16) + origin.getX();
/* 25 */     int z = random.nextInt(16) + origin.getZ();
/* 27 */     return Stream.of(new BlockPos(x, origin.getY(), z));
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 32 */     return PlacementModifierType.IN_SQUARE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\InSquarePlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
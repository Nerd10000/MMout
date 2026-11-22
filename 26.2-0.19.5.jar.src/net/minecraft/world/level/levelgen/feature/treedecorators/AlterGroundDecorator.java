/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AlterGroundDecorator extends TreeDecorator {
/*    */   public static final MapCodec<AlterGroundDecorator> CODEC;
/*    */   
/*    */   private final BlockStateProvider provider;
/*    */   
/*    */   static {
/* 12 */     CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundDecorator::new, d -> d.provider);
/*    */   }
/*    */   
/*    */   public AlterGroundDecorator(BlockStateProvider provider) {
/* 17 */     this.provider = provider;
/*    */   }
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 22 */     return TreeDecoratorType.ALTER_GROUND;
/*    */   }
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 27 */     List<BlockPos> blockPositions = TreeFeature.getLowestTrunkOrRootOfTree(context);
/* 29 */     if (blockPositions.isEmpty())
/*    */       return; 
/* 33 */     int minY = ((BlockPos)blockPositions.getFirst()).getY();
/* 34 */     blockPositions.stream().filter(pos -> (pos.getY() == minY)).forEach(pos -> {
/*    */           placeCircle(context, pos.west().north());
/*    */           placeCircle(context, pos.east(2).north());
/*    */           placeCircle(context, pos.west().south(2));
/*    */           placeCircle(context, pos.east(2).south(2));
/*    */           for (int i = 0; i < 5; i++) {
/*    */             int placement = context.random().nextInt(64);
/*    */             int xx = placement % 8;
/*    */             int zz = placement / 8;
/*    */             if (xx == 0 || xx == 7 || zz == 0 || zz == 7)
/*    */               placeCircle(context, pos.offset(-3 + xx, 0, -3 + zz)); 
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private void placeCircle(TreeDecorator.Context context, BlockPos pos) {
/* 52 */     for (int xx = -2; xx <= 2; xx++) {
/* 53 */       for (int zz = -2; zz <= 2; zz++) {
/* 54 */         if (Math.abs(xx) != 2 || Math.abs(zz) != 2)
/* 55 */           placeBlockAt(context, pos.offset(xx, 0, zz)); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void placeBlockAt(TreeDecorator.Context context, BlockPos pos) {
/* 62 */     for (int dy = 2; dy >= -3; dy--) {
/* 63 */       BlockPos cursor = pos.above(dy);
/* 64 */       BlockState replaceWith = this.provider.getOptionalState(context.level(), context.random(), cursor);
/* 65 */       if (replaceWith != null) {
/* 66 */         context.setBlock(cursor, replaceWith);
/*    */         break;
/*    */       } 
/* 68 */       if (!context.isAir(cursor) && dy < 0)
/*    */         break; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AlterGroundDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
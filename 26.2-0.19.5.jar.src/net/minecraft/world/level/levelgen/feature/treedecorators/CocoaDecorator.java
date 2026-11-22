/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.CocoaBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CocoaDecorator extends TreeDecorator {
/*    */   public static final MapCodec<CocoaDecorator> CODEC;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   static {
/* 14 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CocoaDecorator::new, d -> Float.valueOf(d.probability));
/*    */   }
/*    */   
/*    */   public CocoaDecorator(float probability) {
/* 19 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 24 */     return TreeDecoratorType.COCOA;
/*    */   }
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 29 */     RandomSource random = context.random();
/* 30 */     if (random.nextFloat() >= this.probability)
/*    */       return; 
/* 34 */     ObjectArrayList<BlockPos> objectArrayList = context.logs();
/* 35 */     if (objectArrayList.isEmpty())
/*    */       return; 
/* 39 */     int treeY = ((BlockPos)objectArrayList.getFirst()).getY();
/* 40 */     objectArrayList.stream()
/* 41 */       .filter(pos -> (pos.getY() - treeY <= 2))
/* 42 */       .forEach(pos -> {
/*    */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/*    */             if (random.nextFloat() <= 0.25F) {
/*    */               Direction opposite = direction.getOpposite();
/*    */               BlockPos cocoaPos = pos.offset(opposite.getStepX(), 0, opposite.getStepZ());
/*    */               if (context.isAir(cocoaPos))
/*    */                 context.setBlock(cocoaPos, (BlockState)((BlockState)Blocks.COCOA.defaultBlockState().setValue((Property)CocoaBlock.AGE, Integer.valueOf(random.nextInt(3)))).setValue((Property)CocoaBlock.FACING, (Comparable)direction)); 
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\CocoaDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
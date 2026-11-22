/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ 
/*    */ public class LeaveVineDecorator extends TreeDecorator {
/*    */   public static final MapCodec<LeaveVineDecorator> CODEC;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 13 */     return TreeDecoratorType.LEAVE_VINE;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(LeaveVineDecorator::new, d -> Float.valueOf(d.probability));
/*    */   }
/*    */   
/*    */   public LeaveVineDecorator(float probability) {
/* 21 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 26 */     RandomSource random = context.random();
/* 27 */     context.leaves().forEach(pos -> {
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos west = pos.west();
/*    */             if (context.isAir(west))
/*    */               addHangingVine(west, VineBlock.EAST, context); 
/*    */           } 
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos east = pos.east();
/*    */             if (context.isAir(east))
/*    */               addHangingVine(east, VineBlock.WEST, context); 
/*    */           } 
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos north = pos.north();
/*    */             if (context.isAir(north))
/*    */               addHangingVine(north, VineBlock.SOUTH, context); 
/*    */           } 
/*    */           if (random.nextFloat() < this.probability) {
/*    */             BlockPos south = pos.south();
/*    */             if (context.isAir(south))
/*    */               addHangingVine(south, VineBlock.NORTH, context); 
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void addHangingVine(BlockPos pos, BooleanProperty direction, TreeDecorator.Context context) {
/* 59 */     context.placeVine(pos, direction);
/* 60 */     int maxDir = 4;
/* 62 */     pos = pos.below();
/* 63 */     while (context.isAir(pos) && maxDir > 0) {
/* 64 */       context.placeVine(pos, direction);
/* 65 */       pos = pos.below();
/* 66 */       maxDir--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\LeaveVineDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
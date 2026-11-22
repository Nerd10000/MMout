/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.BeehiveBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BeehiveDecorator extends TreeDecorator {
/*    */   public static final MapCodec<BeehiveDecorator> CODEC;
/*    */   
/*    */   static {
/* 20 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(BeehiveDecorator::new, d -> Float.valueOf(d.probability));
/*    */   }
/*    */   
/* 22 */   private static final Direction WORLDGEN_FACING = Direction.SOUTH;
/*    */   
/*    */   private static final Direction[] SPAWN_DIRECTIONS;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   static {
/* 23 */     SPAWN_DIRECTIONS = (Direction[])Direction.Plane.HORIZONTAL.stream().filter(dir -> (dir != WORLDGEN_FACING.getOpposite())).toArray(x$0 -> new Direction[x$0]);
/*    */   }
/*    */   
/*    */   public BeehiveDecorator(float probability) {
/* 28 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 33 */     return TreeDecoratorType.BEEHIVE;
/*    */   }
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 41 */     ObjectArrayList<BlockPos> objectArrayList1 = context.leaves();
/* 42 */     ObjectArrayList<BlockPos> objectArrayList2 = context.logs();
/* 44 */     if (objectArrayList2.isEmpty())
/*    */       return; 
/* 48 */     RandomSource random = context.random();
/* 49 */     if (random.nextFloat() >= this.probability)
/*    */       return; 
/* 53 */     int hiveY = !objectArrayList1.isEmpty() ? Math.max(((BlockPos)objectArrayList1.getFirst()).getY() - 1, ((BlockPos)objectArrayList2.getFirst()).getY() + 1) : Math.min(((BlockPos)objectArrayList2.getFirst()).getY() + 1 + random.nextInt(3), ((BlockPos)objectArrayList2.getLast()).getY());
/* 58 */     List<BlockPos> hivePlacements = (List<BlockPos>)objectArrayList2.stream().filter(pos -> (pos.getY() == hiveY)).flatMap(pos -> {
/*    */           Objects.requireNonNull(pos);
/*    */           return Stream.<Direction>of(SPAWN_DIRECTIONS).map(pos::relative);
/* 58 */         }).collect(Collectors.toList());
/* 59 */     if (hivePlacements.isEmpty())
/*    */       return; 
/* 62 */     Util.shuffle(hivePlacements, random);
/* 65 */     Optional<BlockPos> hivePos = hivePlacements.stream().filter(pos -> (context.isAir(pos) && context.isAir(pos.relative(WORLDGEN_FACING)))).findFirst();
/* 66 */     if (hivePos.isEmpty())
/*    */       return; 
/* 70 */     context.setBlock(hivePos.get(), (BlockState)Blocks.BEE_NEST.defaultBlockState().setValue((Property)BeehiveBlock.FACING, (Comparable)WORLDGEN_FACING));
/* 71 */     context.level().getBlockEntity(hivePos.get(), BlockEntityTypes.BEEHIVE).ifPresent(beehive -> {
/*    */           int numBees = 2 + random.nextInt(2);
/*    */           for (int count = 0; count < numBees; count++)
/*    */             beehive.storeBee(BeehiveBlockEntity.Occupant.create(random.nextInt(599))); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\BeehiveDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
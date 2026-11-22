/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AttachedToLogsDecorator extends TreeDecorator {
/*    */   public static final MapCodec<AttachedToLogsDecorator> CODEC;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   private final BlockStateProvider blockProvider;
/*    */   
/*    */   private final List<Direction> directions;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(()), (App)ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(())).apply((Applicative)i, AttachedToLogsDecorator::new));
/*    */   }
/*    */   
/*    */   public AttachedToLogsDecorator(float probability, BlockStateProvider blockProvider, List<Direction> directions) {
/* 27 */     this.probability = probability;
/* 28 */     this.blockProvider = blockProvider;
/* 29 */     this.directions = directions;
/*    */   }
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 34 */     RandomSource random = context.random();
/* 35 */     for (BlockPos logsPos : Util.shuffledCopy(context.logs(), random)) {
/* 36 */       Direction direction = (Direction)Util.getRandom(this.directions, random);
/* 37 */       BlockPos placementPos = logsPos.relative(direction);
/* 38 */       if (random.nextFloat() <= this.probability && context.isAir(placementPos))
/* 39 */         context.setBlock(placementPos, this.blockProvider.getState(context.level(), random, placementPos)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 46 */     return TreeDecoratorType.ATTACHED_TO_LOGS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AttachedToLogsDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.RandomizableContainer;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class BonusChestFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public BonusChestFeature(Codec<NoneFeatureConfiguration> codec) {
/* 23 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 28 */     RandomSource random = context.random();
/* 29 */     WorldGenLevel level = context.level();
/* 30 */     ChunkPos chunkPos = ChunkPos.containing(context.origin());
/* 31 */     IntArrayList xPoses = Util.toShuffledList(IntStream.rangeClosed(chunkPos.getMinBlockX(), chunkPos.getMaxBlockX()), random);
/* 32 */     IntArrayList zPoses = Util.toShuffledList(IntStream.rangeClosed(chunkPos.getMinBlockZ(), chunkPos.getMaxBlockZ()), random);
/* 33 */     BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
/* 35 */     for (IntListIterator<Integer> intListIterator = xPoses.iterator(); intListIterator.hasNext(); ) {
/* 35 */       Integer x = intListIterator.next();
/* 36 */       for (IntListIterator<Integer> intListIterator1 = zPoses.iterator(); intListIterator1.hasNext(); ) {
/* 36 */         Integer z = intListIterator1.next();
/* 37 */         mutPos.set(x.intValue(), 0, z.intValue());
/* 38 */         BlockPos chestPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)mutPos);
/* 40 */         if (level.isEmptyBlock(chestPos) || level.getBlockState(chestPos).getCollisionShape((BlockGetter)level, chestPos).isEmpty()) {
/* 41 */           level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 2);
/* 43 */           RandomizableContainer.setBlockEntityLootTable((BlockGetter)level, random, chestPos, BuiltInLootTables.SPAWN_BONUS_CHEST);
/* 45 */           BlockState torch = Blocks.TORCH.defaultBlockState();
/* 47 */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 48 */             BlockPos torchPos = chestPos.relative(direction);
/* 49 */             if (torch.canSurvive((LevelReader)level, torchPos))
/* 50 */               level.setBlock(torchPos, torch, 2); 
/*    */           } 
/* 53 */           return true;
/*    */         } 
/*    */       } 
/*    */     } 
/* 58 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\BonusChestFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
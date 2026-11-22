/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public class CappedProcessor implements StructureProcessor {
/*    */   public static final MapCodec<CappedProcessor> MAP_CODEC;
/*    */   
/*    */   private final StructureProcessor delegate;
/*    */   
/*    */   private final IntProvider limit;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)StructureProcessorType.SINGLE_CODEC.fieldOf("delegate").forGetter(()), (App)IntProviders.POSITIVE_CODEC.fieldOf("limit").forGetter(())).apply((Applicative)i, CappedProcessor::new));
/*    */   }
/*    */   
/*    */   public CappedProcessor(StructureProcessor delegate, IntProvider limit) {
/* 27 */     this.delegate = delegate;
/* 28 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public MapCodec<CappedProcessor> codec() {
/* 33 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public boolean evaluatesEntirePieceState() {
/* 40 */     return true;
/*    */   }
/*    */   
/*    */   public final List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, List<StructureTemplate.StructureBlockInfo> originalBlockInfoList, List<StructureTemplate.StructureBlockInfo> processedBlockInfoList, StructurePlaceSettings settings) {
/* 45 */     if (this.limit.maxInclusive() == 0 || processedBlockInfoList.isEmpty())
/* 46 */       return processedBlockInfoList; 
/* 49 */     if (originalBlockInfoList.size() != processedBlockInfoList.size()) {
/* 50 */       Util.logAndPauseIfInIde("Original block info list not in sync with processed list, skipping processing. Original size: " + originalBlockInfoList.size() + ", Processed size: " + processedBlockInfoList.size());
/* 51 */       return processedBlockInfoList;
/*    */     } 
/* 54 */     RandomSource random = RandomSource.createThreadLocalInstance(level.getLevel().getSeed()).forkPositional().at(position);
/* 56 */     int maxToReplace = Math.min(this.limit.sample(random), processedBlockInfoList.size());
/* 58 */     if (maxToReplace < 1)
/* 59 */       return processedBlockInfoList; 
/* 62 */     IntArrayList indices = Util.toShuffledList(IntStream.range(0, processedBlockInfoList.size()), random);
/* 64 */     IntIterator indexIterator = indices.intIterator();
/* 65 */     int replaced = 0;
/* 67 */     while (indexIterator.hasNext() && replaced < maxToReplace) {
/* 68 */       int index = indexIterator.nextInt();
/* 69 */       StructureTemplate.StructureBlockInfo originalBlockInfo = originalBlockInfoList.get(index);
/* 70 */       StructureTemplate.StructureBlockInfo processedBlockInfo = processedBlockInfoList.get(index);
/* 72 */       StructureTemplate.StructureBlockInfo maybeAltered = this.delegate.processBlock((LevelReader)level, position, referencePos, originalBlockInfo.pos(), processedBlockInfo, settings);
/* 74 */       if (maybeAltered != null && !processedBlockInfo.equals(maybeAltered)) {
/* 75 */         replaced++;
/* 76 */         processedBlockInfoList.set(index, maybeAltered);
/*    */       } 
/*    */     } 
/* 80 */     return processedBlockInfoList;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\CappedProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
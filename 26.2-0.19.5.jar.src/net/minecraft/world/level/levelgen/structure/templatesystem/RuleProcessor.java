/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class RuleProcessor implements StructureProcessor {
/*    */   public static final MapCodec<RuleProcessor> MAP_CODEC;
/*    */   
/*    */   private final ImmutableList<ProcessorRule> rules;
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ProcessorRule.CODEC.listOf().fieldOf("rules").forGetter(())).apply((Applicative)i, RuleProcessor::new));
/*    */   }
/*    */   
/*    */   public RuleProcessor(List<? extends ProcessorRule> rules) {
/* 23 */     this.rules = ImmutableList.copyOf(rules);
/*    */   }
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 28 */     RandomSource random = RandomSource.create(Mth.getSeed((Vec3i)processedBlockInfo.pos()));
/* 29 */     for (UnmodifiableIterator<ProcessorRule> unmodifiableIterator = this.rules.iterator(); unmodifiableIterator.hasNext(); ) {
/* 29 */       ProcessorRule rule = unmodifiableIterator.next();
/* 30 */       if (rule.test(level, processedBlockInfo.state(), templateRelativePos, processedBlockInfo.pos(), referencePos, random))
/* 31 */         return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), rule.getOutputState(), rule.getOutputTag(random, processedBlockInfo.nbt())); 
/*    */     } 
/* 34 */     return processedBlockInfo;
/*    */   }
/*    */   
/*    */   public MapCodec<RuleProcessor> codec() {
/* 39 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
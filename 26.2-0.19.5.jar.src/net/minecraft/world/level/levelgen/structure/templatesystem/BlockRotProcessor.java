/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class BlockRotProcessor implements StructureProcessor {
/*    */   public static final MapCodec<BlockRotProcessor> MAP_CODEC;
/*    */   
/*    */   private final Optional<HolderSet<Block>> rottableBlocks;
/*    */   
/*    */   private final float integrity;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("rottable_blocks").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("integrity").forGetter(())).apply((Applicative)i, BlockRotProcessor::new));
/*    */   }
/*    */   
/*    */   public BlockRotProcessor(HolderSet<Block> tag, float integrity) {
/* 26 */     this(Optional.of(tag), integrity);
/*    */   }
/*    */   
/*    */   public BlockRotProcessor(float integrity) {
/* 30 */     this(Optional.empty(), integrity);
/*    */   }
/*    */   
/*    */   private BlockRotProcessor(Optional<HolderSet<Block>> blockTagKey, float integrity) {
/* 34 */     this.integrity = integrity;
/* 35 */     this.rottableBlocks = blockTagKey;
/*    */   }
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 40 */     RandomSource random = settings.getRandom(processedBlockInfo.pos());
/* 42 */     if ((this.rottableBlocks.isPresent() && !processedBlockInfo.state().is(this.rottableBlocks.get())) || random.nextFloat() <= this.integrity)
/* 43 */       return processedBlockInfo; 
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   public MapCodec<BlockRotProcessor> codec() {
/* 50 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockRotProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
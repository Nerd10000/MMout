/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SulfurSpikeBlock extends SpeleothemBlock {
/*  8 */   private static int MAX_GROWING_LENGTH = 2;
/*    */   
/*    */   public static final MapCodec<SulfurSpikeBlock> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockState.CODEC.fieldOf("block_to_grow_on").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, SulfurSpikeBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<SulfurSpikeBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SulfurSpikeBlock(BlockState blockToGrowOn, BlockBehaviour.Properties properties) {
/* 20 */     super(blockToGrowOn, properties);
/*    */   }
/*    */   
/*    */   protected int getStalactiteLandingSound() {
/* 25 */     return 1052;
/*    */   }
/*    */   
/*    */   protected int getMaxGrowthLength() {
/* 30 */     return MAX_GROWING_LENGTH;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SulfurSpikeBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
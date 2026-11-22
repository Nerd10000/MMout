/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherRootsBlock extends VegetationBlock {
/*    */   public static final MapCodec<NetherRootsBlock> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TagKey.codec(Registries.BLOCK).fieldOf("support_blocks").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, NetherRootsBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<NetherRootsBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/* 24 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*    */   
/*    */   private final TagKey<Block> supportBlocks;
/*    */   
/*    */   protected NetherRootsBlock(TagKey<Block> supportBlocks, BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     this.supportBlocks = supportBlocks;
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 35 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 40 */     return state.is(this.supportBlocks);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\NetherRootsBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
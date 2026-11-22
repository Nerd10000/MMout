/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class LilyPadBlock extends VegetationBlock {
/* 20 */   public static final MapCodec<LilyPadBlock> CODEC = simpleCodec(LilyPadBlock::new);
/*    */   
/*    */   public MapCodec<LilyPadBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 1.5D);
/*    */   
/*    */   protected LilyPadBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/*    */   }
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 35 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/* 37 */     if (level instanceof net.minecraft.server.level.ServerLevel && entity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat)
/* 38 */       level.destroyBlock(new BlockPos((Vec3i)pos), true, entity); 
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 44 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 49 */     FluidState fluidState = level.getFluidState(pos);
/* 50 */     FluidState fluidAbove = level.getFluidState(pos.above());
/* 51 */     return ((fluidState.is(FluidTags.SUPPORTS_LILY_PAD) || state.is(BlockTags.SUPPORTS_LILY_PAD)) && fluidAbove.is(Fluids.EMPTY));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\LilyPadBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
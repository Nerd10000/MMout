/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.SmokerBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SmokerBlock extends AbstractFurnaceBlock {
/* 22 */   public static final MapCodec<SmokerBlock> CODEC = simpleCodec(SmokerBlock::new);
/*    */   
/*    */   public MapCodec<SmokerBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SmokerBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 35 */     return (BlockEntity)new SmokerBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 40 */     return createFurnaceTicker(level, type, BlockEntityTypes.SMOKER);
/*    */   }
/*    */   
/*    */   protected void openContainer(Level level, BlockPos pos, Player player) {
/* 45 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 46 */     if (blockEntity instanceof SmokerBlockEntity) {
/* 47 */       player.openMenu((MenuProvider)blockEntity);
/* 48 */       player.awardStat(Stats.INTERACT_WITH_SMOKER);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 54 */     if (!((Boolean)state.getValue((Property)LIT)).booleanValue())
/*    */       return; 
/* 58 */     double x = pos.getX() + 0.5D;
/* 59 */     double y = pos.getY();
/* 60 */     double z = pos.getZ() + 0.5D;
/* 62 */     if (random.nextDouble() < 0.1D)
/* 63 */       level.playLocalSound(x, y, z, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false); 
/* 66 */     level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x, y + 1.1D, z, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SmokerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
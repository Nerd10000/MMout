/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlastFurnaceBlock extends AbstractFurnaceBlock {
/* 23 */   public static final MapCodec<BlastFurnaceBlock> CODEC = simpleCodec(BlastFurnaceBlock::new);
/*    */   
/*    */   public MapCodec<BlastFurnaceBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected BlastFurnaceBlock(BlockBehaviour.Properties properties) {
/* 31 */     super(properties);
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 36 */     return (BlockEntity)new BlastFurnaceBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 41 */     return createFurnaceTicker(level, type, BlockEntityTypes.BLAST_FURNACE);
/*    */   }
/*    */   
/*    */   protected void openContainer(Level level, BlockPos pos, Player player) {
/* 46 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 47 */     if (blockEntity instanceof BlastFurnaceBlockEntity) {
/* 48 */       player.openMenu((MenuProvider)blockEntity);
/* 49 */       player.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 55 */     if (!((Boolean)state.getValue((Property)LIT)).booleanValue())
/*    */       return; 
/* 59 */     double x = pos.getX() + 0.5D;
/* 60 */     double y = pos.getY();
/* 61 */     double z = pos.getZ() + 0.5D;
/* 63 */     if (random.nextDouble() < 0.1D)
/* 64 */       level.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false); 
/* 67 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 68 */     Direction.Axis axis = direction.getAxis();
/* 70 */     double r = 0.52D;
/* 71 */     double ss = random.nextDouble() * 0.6D - 0.3D;
/* 73 */     double dx = (axis == Direction.Axis.X) ? (direction.getStepX() * 0.52D) : ss;
/* 74 */     double dy = random.nextDouble() * 9.0D / 16.0D;
/* 75 */     double dz = (axis == Direction.Axis.Z) ? (direction.getStepZ() * 0.52D) : ss;
/* 77 */     level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BlastFurnaceBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
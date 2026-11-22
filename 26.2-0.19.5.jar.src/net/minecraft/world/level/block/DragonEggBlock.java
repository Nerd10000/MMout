/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DragonEggBlock extends FallingBlock {
/* 21 */   public static final MapCodec<DragonEggBlock> CODEC = simpleCodec(DragonEggBlock::new);
/*    */   
/*    */   public MapCodec<DragonEggBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/* 28 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/*    */   public DragonEggBlock(BlockBehaviour.Properties properties) {
/* 31 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 36 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 41 */     teleport(state, level, pos);
/* 42 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */   
/*    */   protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
/* 47 */     teleport(state, level, pos);
/*    */   }
/*    */   
/*    */   private void teleport(BlockState state, Level level, BlockPos pos) {
/* 51 */     WorldBorder worldBorder = level.getWorldBorder();
/* 52 */     RandomSource random = level.getRandom();
/* 53 */     for (int i = 0; i < 1000; i++) {
/* 54 */       BlockPos testPos = pos.offset(random
/* 55 */           .nextInt(16) - random.nextInt(16), random
/* 56 */           .nextInt(8) - random.nextInt(8), random
/* 57 */           .nextInt(16) - random.nextInt(16));
/* 59 */       if (level.getBlockState(testPos).isAir() && !level.getBlockState(testPos.below()).isAir() && worldBorder.isWithinBounds(testPos) && level.isInsideBuildHeight(testPos)) {
/* 60 */         if (level.isClientSide()) {
/* 61 */           for (int j = 0; j < 128; j++) {
/* 62 */             double d = random.nextDouble();
/* 63 */             float xa = (random.nextFloat() - 0.5F) * 0.2F;
/* 64 */             float ya = (random.nextFloat() - 0.5F) * 0.2F;
/* 65 */             float za = (random.nextFloat() - 0.5F) * 0.2F;
/* 67 */             double x = Mth.lerp(d, testPos.getX(), pos.getX()) + random.nextDouble() - 0.5D + 0.5D;
/* 68 */             double y = Mth.lerp(d, testPos.getY(), pos.getY()) + random.nextDouble() - 0.5D;
/* 69 */             double z = Mth.lerp(d, testPos.getZ(), pos.getZ()) + random.nextDouble() - 0.5D + 0.5D;
/* 70 */             level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*    */           } 
/*    */         } else {
/* 73 */           level.setBlock(testPos, state, 2);
/* 74 */           level.removeBlock(pos, false);
/*    */         } 
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getDelayAfterPlace() {
/* 83 */     return 5;
/*    */   }
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 88 */     return false;
/*    */   }
/*    */   
/*    */   public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
/* 93 */     return -16777216;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DragonEggBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
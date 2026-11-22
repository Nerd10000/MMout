/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SnifferEggBlock extends Block {
/*  28 */   public static final MapCodec<SnifferEggBlock> CODEC = simpleCodec(SnifferEggBlock::new);
/*     */   
/*     */   public static final int MAX_HATCH_LEVEL = 2;
/*     */   
/*     */   public MapCodec<SnifferEggBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */   
/*  36 */   public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
/*     */   
/*     */   private static final int REGULAR_HATCH_TIME_TICKS = 24000;
/*     */   
/*     */   private static final int BOOSTED_HATCH_TIME_TICKS = 12000;
/*     */   
/*     */   private static final int RANDOM_HATCH_OFFSET_TICKS = 300;
/*     */   
/*  42 */   private static final VoxelShape SHAPE = Block.column(14.0D, 12.0D, 0.0D, 16.0D);
/*     */   
/*     */   public SnifferEggBlock(BlockBehaviour.Properties properties) {
/*  45 */     super(properties);
/*  46 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HATCH, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  51 */     builder.add(new Property[] { (Property)HATCH });
/*     */   }
/*     */   
/*     */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  56 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public int getHatchLevel(BlockState state) {
/*  60 */     return ((Integer)state.getValue((Property)HATCH)).intValue();
/*     */   }
/*     */   
/*     */   private boolean isReadyToHatch(BlockState state) {
/*  64 */     return (getHatchLevel(state) == 2);
/*     */   }
/*     */   
/*     */   public void tick(BlockState state, ServerLevel level, BlockPos position, RandomSource random) {
/*  69 */     if (!isReadyToHatch(state)) {
/*  70 */       level.playSound(null, position, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
/*  71 */       level.setBlock(position, (BlockState)state.setValue((Property)HATCH, Integer.valueOf(getHatchLevel(state) + 1)), 2);
/*     */       return;
/*     */     } 
/*  76 */     level.playSound(null, position, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
/*  77 */     level.destroyBlock(position, false);
/*  79 */     Sniffer sniffer = (Sniffer)EntityTypes.SNIFFER.create((Level)level, EntitySpawnReason.BREEDING);
/*  80 */     if (sniffer != null) {
/*  81 */       Vec3 spawnAt = Vec3.atCenterOf((Vec3i)position);
/*  83 */       sniffer.setBaby(true);
/*  84 */       sniffer.snapTo(spawnAt.x(), spawnAt.y(), spawnAt.z(), Mth.wrapDegrees(level.getRandom().nextFloat() * 360.0F), 0.0F);
/*  86 */       level.addFreshEntity((Entity)sniffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  92 */     boolean boosted = hatchBoost((BlockGetter)level, pos);
/*  94 */     if (!level.isClientSide() && boosted)
/*  95 */       level.levelEvent(3009, pos, 0); 
/*  98 */     int hatchTime = boosted ? 12000 : 24000;
/*  99 */     int progressionTickDelay = hatchTime / 3;
/* 101 */     level.gameEvent((Holder)GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(state));
/* 102 */     level.scheduleTick(pos, this, progressionTickDelay + level.getRandom().nextInt(300));
/*     */   }
/*     */   
/*     */   public boolean isPathfindable(BlockState state, PathComputationType type) {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean hatchBoost(BlockGetter level, BlockPos pos) {
/* 111 */     return level.getBlockState(pos.below()).is(BlockTags.SNIFFER_EGG_HATCH_BOOST);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SnifferEggBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
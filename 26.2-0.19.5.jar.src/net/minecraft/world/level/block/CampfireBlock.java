/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.crafting.CampfireCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.CampfireBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CampfireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*     */   public static final MapCodec<CampfireBlock> CODEC;
/*     */   
/*     */   static {
/*  57 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("spawn_particles").forGetter(()), (App)Codec.intRange(0, 1000).fieldOf("fire_damage").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, CampfireBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<CampfireBlock> codec() {
/*  65 */     return CODEC;
/*     */   }
/*     */   
/*  68 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*     */   
/*  69 */   public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
/*     */   
/*  70 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  71 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*     */   
/*  73 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 7.0D);
/*     */   
/*  75 */   private static final VoxelShape SHAPE_VIRTUAL_POST = Block.column(4.0D, 0.0D, 16.0D);
/*     */   
/*     */   private static final int SMOKE_DISTANCE = 5;
/*     */   
/*     */   private final boolean spawnParticles;
/*     */   
/*     */   private final int fireDamage;
/*     */   
/*     */   public CampfireBlock(boolean spawnParticles, int fireDamage, BlockBehaviour.Properties properties) {
/*  82 */     super(properties);
/*  83 */     this.spawnParticles = spawnParticles;
/*  84 */     this.fireDamage = fireDamage;
/*  85 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, Boolean.valueOf(true))).setValue((Property)SIGNAL_FIRE, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  90 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  91 */     if (blockEntity instanceof CampfireBlockEntity) {
/*  91 */       CampfireBlockEntity campfire = (CampfireBlockEntity)blockEntity;
/*  92 */       ItemStack itemInHand = player.getItemInHand(hand);
/*  93 */       if (level.recipeAccess().propertySet(RecipePropertySet.CAMPFIRE_INPUT).test(itemInHand)) {
/*  94 */         if (level instanceof ServerLevel) {
/*  94 */           ServerLevel serverLevel = (ServerLevel)level;
/*  94 */           if (campfire.placeFood(serverLevel, (LivingEntity)player, itemInHand)) {
/*  95 */             player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
/*  96 */             return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */           } 
/*     */         } 
/*  98 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       } 
/*     */     } 
/* 102 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 107 */     if (((Boolean)state.getValue((Property)LIT)).booleanValue() && entity instanceof LivingEntity)
/* 108 */       entity.hurt(level.damageSources().campfire(), this.fireDamage); 
/* 111 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 116 */     Level level = context.getLevel();
/* 117 */     BlockPos pos = context.getClickedPos();
/* 118 */     boolean replacedWater = level.getFluidState(pos).is(Fluids.WATER);
/* 119 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 120 */       .setValue((Property)WATERLOGGED, Boolean.valueOf(replacedWater)))
/* 121 */       .setValue((Property)SIGNAL_FIRE, Boolean.valueOf(isSmokeSource(level.getBlockState(pos.below())))))
/* 122 */       .setValue((Property)LIT, Boolean.valueOf(!replacedWater)))
/* 123 */       .setValue((Property)FACING, (Comparable)context.getHorizontalDirection());
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 128 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 129 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 132 */     if (directionToNeighbour == Direction.DOWN)
/* 133 */       return (BlockState)state.setValue((Property)SIGNAL_FIRE, Boolean.valueOf(isSmokeSource(neighbourState))); 
/* 135 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private boolean isSmokeSource(BlockState blockState) {
/* 139 */     return blockState.is(Blocks.HAY_BLOCK);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 144 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 149 */     if (!((Boolean)state.getValue((Property)LIT)).booleanValue())
/*     */       return; 
/* 153 */     if (random.nextInt(10) == 0)
/* 154 */       level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false); 
/* 157 */     if (this.spawnParticles && random.nextInt(5) == 0)
/* 158 */       for (int i = 0; i < random.nextInt(1) + 1; i++)
/* 159 */         level.addParticle((ParticleOptions)ParticleTypes.LAVA, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, (random.nextFloat() / 2.0F), 5.0E-5D, (random.nextFloat() / 2.0F));  
/*     */   }
/*     */   
/*     */   public static void dowse(Entity source, LevelAccessor level, BlockPos pos, BlockState state) {
/* 165 */     if (level.isClientSide())
/* 166 */       for (int j = 0; j < 20; j++)
/* 167 */         makeParticles((Level)level, pos, ((Boolean)state.getValue((Property)SIGNAL_FIRE)).booleanValue(), true);  
/* 170 */     level.gameEvent(source, (Holder)GameEvent.BLOCK_CHANGE, pos);
/*     */   }
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 175 */     if (!((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && fluidState.is(Fluids.WATER)) {
/* 176 */       boolean isLit = ((Boolean)state.getValue((Property)LIT)).booleanValue();
/* 177 */       if (isLit) {
/* 178 */         if (!level.isClientSide())
/* 179 */           level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F); 
/* 182 */         dowse((Entity)null, level, pos, state);
/*     */       } 
/* 185 */       level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)WATERLOGGED, Boolean.valueOf(true))).setValue((Property)LIT, Boolean.valueOf(false)), 3);
/* 186 */       level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay((LevelReader)level));
/* 187 */       return true;
/*     */     } 
/* 189 */     return false;
/*     */   }
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 194 */     BlockPos pos = blockHit.getBlockPos();
/* 195 */     if (level instanceof ServerLevel) {
/* 195 */       ServerLevel serverLevel = (ServerLevel)level;
/* 195 */       if (projectile.isOnFire() && projectile.mayInteract(serverLevel, pos) && !((Boolean)state.getValue((Property)LIT)).booleanValue() && !((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 196 */         level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)), 11); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void makeParticles(Level level, BlockPos pos, boolean isSignalFire, boolean smoking) {
/* 201 */     RandomSource random = level.getRandom();
/* 202 */     SimpleParticleType smokeParticle = isSignalFire ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
/* 203 */     level.addAlwaysVisibleParticle((ParticleOptions)smokeParticle, true, pos
/*     */         
/* 205 */         .getX() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? true : -1), pos
/* 206 */         .getY() + random.nextDouble() + random.nextDouble(), pos
/* 207 */         .getZ() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? true : -1), 0.0D, 0.07D, 0.0D);
/* 210 */     if (smoking)
/* 211 */       level.addParticle((ParticleOptions)ParticleTypes.SMOKE, pos
/* 212 */           .getX() + 0.5D + random.nextDouble() / 4.0D * (random.nextBoolean() ? true : -1), pos
/* 213 */           .getY() + 0.4D, pos
/* 214 */           .getZ() + 0.5D + random.nextDouble() / 4.0D * (random.nextBoolean() ? true : -1), 0.0D, 0.005D, 0.0D); 
/*     */   }
/*     */   
/*     */   public static boolean isSmokeyPos(Level level, BlockPos pos) {
/* 225 */     for (int i = 1; i <= 5; i++) {
/* 226 */       BlockPos posToCheck = pos.below(i);
/* 227 */       BlockState blockState = level.getBlockState(posToCheck);
/* 228 */       if (isLitCampfire(blockState))
/* 229 */         return true; 
/* 232 */       boolean smokeBlocked = Shapes.joinIsNotEmpty(SHAPE_VIRTUAL_POST, blockState.getCollisionShape((BlockGetter)level, pos, CollisionContext.empty()), BooleanOp.AND);
/* 233 */       if (smokeBlocked) {
/* 236 */         BlockState belowState = level.getBlockState(posToCheck.below());
/* 237 */         return isLitCampfire(belowState);
/*     */       } 
/*     */     } 
/* 240 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isLitCampfire(BlockState blockState) {
/* 245 */     return (blockState.hasProperty((Property)LIT) && blockState.is(BlockTags.CAMPFIRES) && ((Boolean)blockState.getValue((Property)LIT)).booleanValue());
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 250 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 251 */       return Fluids.WATER.getSource(false); 
/* 253 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 258 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 263 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 268 */     builder.add(new Property[] { (Property)LIT, (Property)SIGNAL_FIRE, (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 273 */     return (BlockEntity)new CampfireBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 278 */     if (level instanceof ServerLevel) {
/* 278 */       ServerLevel serverLevel = (ServerLevel)level;
/* 279 */       if (((Boolean)blockState.getValue((Property)LIT)).booleanValue()) {
/* 280 */         RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
/* 281 */         return createTickerHelper(type, BlockEntityTypes.CAMPFIRE, (innerLevel, pos, state, entity) -> CampfireBlockEntity.cookTick(serverLevel, pos, state, entity, quickCheck));
/*     */       } 
/* 283 */       return createTickerHelper(type, BlockEntityTypes.CAMPFIRE, CampfireBlockEntity::cooldownTick);
/*     */     } 
/* 286 */     if (((Boolean)blockState.getValue((Property)LIT)).booleanValue())
/* 287 */       return createTickerHelper(type, BlockEntityTypes.CAMPFIRE, CampfireBlockEntity::particleTick); 
/* 290 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 295 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState state) {
/* 299 */     return (state.is(BlockTags.CAMPFIRES, s -> (s.hasProperty((Property)WATERLOGGED) && s.hasProperty((Property)LIT))) && !((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue() && !((Boolean)state.getValue((Property)LIT)).booleanValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CampfireBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
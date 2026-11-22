/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public abstract class LavaFluid extends FlowingFluid {
/*     */   public static final int LIGHT_EMISSION = 15;
/*     */   
/*     */   public static final float MIN_LEVEL_CUTOFF = 0.44444445F;
/*     */   
/*     */   public Fluid getFlowing() {
/*  42 */     return Fluids.FLOWING_LAVA;
/*     */   }
/*     */   
/*     */   public Fluid getSource() {
/*  47 */     return Fluids.LAVA;
/*     */   }
/*     */   
/*     */   public Item getBucket() {
/*  52 */     return Items.LAVA_BUCKET;
/*     */   }
/*     */   
/*     */   public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  57 */     BlockPos above = pos.above();
/*  58 */     if (level.getBlockState(above).isAir() && !level.getBlockState(above).isSolidRender()) {
/*  59 */       if (random.nextInt(100) == 0) {
/*  60 */         double xx = pos.getX() + random.nextDouble();
/*  62 */         double yy = pos.getY() + 1.0D;
/*  63 */         double zz = pos.getZ() + random.nextDouble();
/*  64 */         level.addParticle((ParticleOptions)ParticleTypes.LAVA, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*  65 */         level.playLocalSound(xx, yy, zz, SoundEvents.LAVA_POP, SoundSource.AMBIENT, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
/*     */       } 
/*  67 */       if (random.nextInt(200) == 0)
/*  68 */         level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.AMBIENT, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void randomTick(ServerLevel level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  75 */     if (!level.canSpreadFireAround(pos))
/*     */       return; 
/*  80 */     int passes = random.nextInt(3);
/*  81 */     if (passes > 0) {
/*  82 */       BlockPos testPos = pos;
/*  84 */       for (int pass = 0; pass < passes; pass++) {
/*  85 */         testPos = testPos.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
/*  86 */         if (!level.isLoaded(testPos))
/*     */           return; 
/*  89 */         BlockState blockState = level.getBlockState(testPos);
/*  90 */         if (blockState.isAir()) {
/*  91 */           if (hasFlammableNeighbours((LevelReader)level, testPos)) {
/*  92 */             level.setBlockAndUpdate(testPos, BaseFireBlock.getState((BlockGetter)level, testPos));
/*     */             return;
/*     */           } 
/*  95 */         } else if (blockState.blocksMotion()) {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 100 */       for (int i = 0; i < 3; i++) {
/* 101 */         BlockPos testPos = pos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
/* 102 */         if (!level.isLoaded(testPos))
/*     */           return; 
/* 105 */         if (level.isEmptyBlock(testPos.above()) && isFlammable((LevelReader)level, testPos))
/* 106 */           level.setBlockAndUpdate(testPos.above(), BaseFireBlock.getState((BlockGetter)level, testPos)); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
/* 114 */     effectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 115 */     effectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
/* 116 */     effectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
/*     */   }
/*     */   
/*     */   private boolean hasFlammableNeighbours(LevelReader level, BlockPos pos) {
/* 120 */     for (Direction direction : Direction.values()) {
/* 121 */       if (isFlammable(level, pos.relative(direction)))
/* 122 */         return true; 
/*     */     } 
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isFlammable(LevelReader level, BlockPos pos) {
/* 129 */     if (level.isInsideBuildHeight(pos.getY()) && !level.hasChunkAt(pos))
/* 130 */       return false; 
/* 132 */     return level.getBlockState(pos).ignitedByLava();
/*     */   }
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/* 137 */     return (ParticleOptions)ParticleTypes.DRIPPING_LAVA;
/*     */   }
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
/* 142 */     fizz(level, pos);
/*     */   }
/*     */   
/*     */   public int getSlopeFindDistance(LevelReader level) {
/* 147 */     return isFastLava(level) ? 4 : 2;
/*     */   }
/*     */   
/*     */   public BlockState createLegacyBlock(FluidState fluidState) {
/* 152 */     return (BlockState)Blocks.LAVA.defaultBlockState().setValue((Property)LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(fluidState)));
/*     */   }
/*     */   
/*     */   public boolean isSame(Fluid other) {
/* 157 */     return (other == Fluids.LAVA || other == Fluids.FLOWING_LAVA);
/*     */   }
/*     */   
/*     */   public int getDropOff(LevelReader level) {
/* 162 */     return isFastLava(level) ? 1 : 2;
/*     */   }
/*     */   
/*     */   public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
/* 167 */     return (state.getHeight(level, pos) >= 0.44444445F && other.is(FluidTags.WATER));
/*     */   }
/*     */   
/*     */   public int getTickDelay(LevelReader level) {
/* 172 */     return isFastLava(level) ? 10 : 30;
/*     */   }
/*     */   
/*     */   public int getSpreadDelay(Level level, BlockPos pos, FluidState oldFluidState, FluidState newFluidState) {
/* 177 */     int result = getTickDelay((LevelReader)level);
/* 179 */     if (!oldFluidState.isEmpty() && !newFluidState.isEmpty() && !((Boolean)oldFluidState.getValue((Property)FALLING)).booleanValue() && !((Boolean)newFluidState.getValue((Property)FALLING)).booleanValue() && newFluidState.getHeight((BlockGetter)level, pos) > oldFluidState.getHeight((BlockGetter)level, pos) && level.getRandom().nextInt(4) != 0)
/* 180 */       result *= 4; 
/* 182 */     return result;
/*     */   }
/*     */   
/*     */   private void fizz(LevelAccessor level, BlockPos pos) {
/* 186 */     level.levelEvent(1501, pos, 0);
/*     */   }
/*     */   
/*     */   protected boolean canConvertToSource(ServerLevel level) {
/* 191 */     return ((Boolean)level.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION)).booleanValue();
/*     */   }
/*     */   
/*     */   protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState target) {
/* 196 */     if (direction == Direction.DOWN) {
/* 197 */       FluidState fluidState = level.getFluidState(pos);
/* 198 */       if (is(FluidTags.LAVA) && fluidState.is(FluidTags.WATER)) {
/* 199 */         if (state.getBlock() instanceof LiquidBlock)
/* 200 */           level.setBlock(pos, Blocks.STONE.defaultBlockState(), 3); 
/* 202 */         fizz(level, pos);
/*     */         return;
/*     */       } 
/*     */     } 
/* 207 */     super.spreadTo(level, pos, state, direction, target);
/*     */   }
/*     */   
/*     */   protected boolean isRandomlyTicking() {
/* 212 */     return true;
/*     */   }
/*     */   
/*     */   protected float getExplosionResistance() {
/* 217 */     return 100.0F;
/*     */   }
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 222 */     return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
/*     */   }
/*     */   
/*     */   private static boolean isFastLava(LevelReader level) {
/* 226 */     return ((Boolean)level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA)).booleanValue();
/*     */   }
/*     */   
/*     */   public static class Source extends LavaFluid {
/*     */     public int getAmount(FluidState fluidState) {
/* 232 */       return 8;
/*     */     }
/*     */     
/*     */     public boolean isSource(FluidState fluidState) {
/* 237 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Flowing extends LavaFluid {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
/* 244 */       super.createFluidStateDefinition(builder);
/* 245 */       builder.add(new Property[] { (Property)LEVEL });
/*     */     }
/*     */     
/*     */     public int getAmount(FluidState fluidState) {
/* 250 */       return ((Integer)fluidState.getValue((Property)LEVEL)).intValue();
/*     */     }
/*     */     
/*     */     public boolean isSource(FluidState fluidState) {
/* 255 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\LavaFluid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
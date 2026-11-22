/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.TypedInstance;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public final class FluidState extends StateHolder<Fluid, FluidState> implements TypedInstance<Fluid> {
/*  25 */   public static final Codec<FluidState> CODEC = codec(BuiltInRegistries.FLUID.byNameCodec(), Fluid::defaultFluidState, Fluid::getStateDefinition).stable();
/*     */   
/*     */   public static final int AMOUNT_MAX = 9;
/*     */   
/*     */   public static final int AMOUNT_FULL = 8;
/*     */   
/*     */   public FluidState(Fluid owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
/*  30 */     super(owner, (Property[])propertyKeys, (Comparable[])propertyValues);
/*     */   }
/*     */   
/*     */   public Fluid getType() {
/*  37 */     return (Fluid)this.owner;
/*     */   }
/*     */   
/*     */   public boolean isSource() {
/*  41 */     return getType().isSource(this);
/*     */   }
/*     */   
/*     */   public boolean isSourceOfType(Fluid fluidType) {
/*  45 */     return (this.owner == fluidType && ((Fluid)this.owner).isSource(this));
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  49 */     return getType().isEmpty();
/*     */   }
/*     */   
/*     */   public float getHeight(BlockGetter level, BlockPos pos) {
/*  53 */     return getType().getHeight(this, level, pos);
/*     */   }
/*     */   
/*     */   public float getOwnHeight() {
/*  57 */     return getType().getOwnHeight(this);
/*     */   }
/*     */   
/*     */   public boolean isFull() {
/*  61 */     return (getAmount() == 8);
/*     */   }
/*     */   
/*     */   public int getAmount() {
/*  65 */     return getType().getAmount(this);
/*     */   }
/*     */   
/*     */   public boolean shouldRenderBackwardUpFace(BlockGetter level, BlockPos above) {
/*  69 */     for (int ox = -1; ox <= 1; ox++) {
/*  70 */       for (int oz = -1; oz <= 1; oz++) {
/*  71 */         BlockPos offset = above.offset(ox, 0, oz);
/*  72 */         FluidState fluidState = level.getFluidState(offset);
/*  73 */         if (!fluidState.getType().isSame(getType()) && !level.getBlockState(offset).isSolidRender())
/*  74 */           return true; 
/*     */       } 
/*     */     } 
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, BlockPos pos, BlockState blockState) {
/*  82 */     getType().tick(level, pos, blockState, this);
/*     */   }
/*     */   
/*     */   public void animateTick(Level level, BlockPos pos, RandomSource random) {
/*  86 */     getType().animateTick(level, pos, this, random);
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTicking() {
/*  90 */     return getType().isRandomlyTicking();
/*     */   }
/*     */   
/*     */   public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) {
/*  94 */     getType().randomTick(level, pos, this, random);
/*     */   }
/*     */   
/*     */   public Vec3 getFlow(BlockGetter level, BlockPos pos) {
/*  98 */     return getType().getFlow(level, pos, this);
/*     */   }
/*     */   
/*     */   public BlockState createLegacyBlock() {
/* 102 */     return getType().createLegacyBlock(this);
/*     */   }
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/* 106 */     return getType().getDripParticle();
/*     */   }
/*     */   
/*     */   public Holder<Fluid> typeHolder() {
/* 111 */     return (Holder<Fluid>)getType().builtInRegistryHolder();
/*     */   }
/*     */   
/*     */   public float getExplosionResistance() {
/* 115 */     return getType().getExplosionResistance();
/*     */   }
/*     */   
/*     */   public boolean canBeReplacedWith(BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
/* 119 */     return getType().canBeReplacedWith(this, level, pos, other, direction);
/*     */   }
/*     */   
/*     */   public VoxelShape getShape(BlockGetter level, BlockPos pos) {
/* 123 */     return getType().getShape(this, level, pos);
/*     */   }
/*     */   
/*     */   public AABB getAABB(BlockGetter level, BlockPos pos) {
/* 127 */     return getType().getAABB(this, level, pos);
/*     */   }
/*     */   
/*     */   public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
/* 131 */     getType().entityInside(level, pos, entity, effectApplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\FluidState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
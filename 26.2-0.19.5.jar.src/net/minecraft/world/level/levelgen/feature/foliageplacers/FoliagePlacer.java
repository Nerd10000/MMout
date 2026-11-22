/*     */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*     */ 
/*     */ import com.mojang.datafixers.Products;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.IntProviders;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public abstract class FoliagePlacer {
/*  21 */   public static final Codec<FoliagePlacer> CODEC = BuiltInRegistries.FOLIAGE_PLACER_TYPE.byNameCodec().dispatch(FoliagePlacer::type, FoliagePlacerType::codec);
/*     */   
/*     */   protected final IntProvider radius;
/*     */   
/*     */   protected final IntProvider offset;
/*     */   
/*     */   protected static <P extends FoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider> foliagePlacerParts(RecordCodecBuilder.Instance<P> instance) {
/*  27 */     return instance.group(
/*  28 */         (App)IntProviders.codec(0, 16).fieldOf("radius").forGetter(p -> p.radius), 
/*  29 */         (App)IntProviders.codec(0, 16).fieldOf("offset").forGetter(p -> p.offset));
/*     */   }
/*     */   
/*     */   public FoliagePlacer(IntProvider radius, IntProvider offset) {
/*  40 */     this.radius = radius;
/*  41 */     this.offset = offset;
/*     */   }
/*     */   
/*     */   public void createFoliage(WorldGenLevel level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius) {
/*  47 */     createFoliage(level, foliageSetter, random, config, treeHeight, foliageAttachment, foliageHeight, leafRadius, offset(random));
/*     */   }
/*     */   
/*     */   public int foliageRadius(RandomSource random, int trunkHeight) {
/*  55 */     return this.radius.sample(random);
/*     */   }
/*     */   
/*     */   private int offset(RandomSource random) {
/*  59 */     return this.offset.sample(random);
/*     */   }
/*     */   
/*     */   protected boolean shouldSkipLocationSigned(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/*     */     int minDx;
/*     */     int minDz;
/*  67 */     if (doubleTrunk) {
/*  70 */       minDx = Math.min(Math.abs(dx), Math.abs(dx - 1));
/*  71 */       minDz = Math.min(Math.abs(dz), Math.abs(dz - 1));
/*     */     } else {
/*  75 */       minDx = Math.abs(dx);
/*  76 */       minDz = Math.abs(dz);
/*     */     } 
/*  78 */     return shouldSkipLocation(random, minDx, y, minDz, currentRadius, doubleTrunk);
/*     */   }
/*     */   
/*     */   protected void placeLeavesRow(WorldGenLevel level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, BlockPos origin, int currentRadius, int y, boolean doubleTrunk) {
/*  82 */     int offset = doubleTrunk ? 1 : 0;
/*  83 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*  84 */     for (int dx = -currentRadius; dx <= currentRadius + offset; dx++) {
/*  85 */       for (int dz = -currentRadius; dz <= currentRadius + offset; dz++) {
/*  86 */         if (!shouldSkipLocationSigned(random, dx, y, dz, currentRadius, doubleTrunk)) {
/*  89 */           pos.setWithOffset((Vec3i)origin, dx, y, dz);
/*  90 */           tryPlaceLeaf(level, foliageSetter, random, config, (BlockPos)pos);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void placeLeavesRowWithHangingLeavesBelow(WorldGenLevel level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, BlockPos origin, int currentRadius, int y, boolean doubleTrunk, float hangingLeavesChance, float hangingLeavesExtensionChance) {
/*  97 */     placeLeavesRow(level, foliageSetter, random, config, origin, currentRadius, y, doubleTrunk);
/*  99 */     int offset = doubleTrunk ? 1 : 0;
/* 100 */     BlockPos logPos = origin.below();
/* 101 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 102 */     for (Direction alongEdge : Direction.Plane.HORIZONTAL) {
/* 103 */       Direction toEdge = alongEdge.getClockWise();
/* 104 */       int offsetToEdge = (toEdge.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (currentRadius + offset) : currentRadius;
/* 106 */       pos.setWithOffset((Vec3i)origin, 0, y - 1, 0)
/* 107 */         .move(toEdge, offsetToEdge)
/* 108 */         .move(alongEdge, -currentRadius);
/* 110 */       for (int offsetAlongEdge = -currentRadius; offsetAlongEdge < currentRadius + offset; offsetAlongEdge++, pos.move(alongEdge)) {
/* 113 */         boolean leavesAbove = foliageSetter.isSet((BlockPos)pos.move(Direction.UP));
/* 114 */         pos.move(Direction.DOWN);
/* 116 */         if (leavesAbove)
/* 119 */           if (tryPlaceExtension(level, foliageSetter, random, config, hangingLeavesChance, logPos, pos)) {
/* 122 */             pos.move(Direction.DOWN);
/* 123 */             tryPlaceExtension(level, foliageSetter, random, config, hangingLeavesExtensionChance, logPos, pos);
/* 124 */             pos.move(Direction.UP);
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean tryPlaceExtension(WorldGenLevel level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, float chance, BlockPos logPos, BlockPos.MutableBlockPos pos) {
/* 130 */     if (pos.distManhattan((Vec3i)logPos) >= 7)
/* 131 */       return false; 
/* 133 */     if (random.nextFloat() > chance)
/* 134 */       return false; 
/* 136 */     return tryPlaceLeaf(level, foliageSetter, random, config, (BlockPos)pos);
/*     */   }
/*     */   
/*     */   protected static boolean tryPlaceLeaf(WorldGenLevel level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, BlockPos pos) {
/* 140 */     boolean isPersistent = level.isStateAtPosition(pos, state -> ((Boolean)state.getValueOrElse((Property)BlockStateProperties.PERSISTENT, Boolean.valueOf(false))).booleanValue());
/* 141 */     if (isPersistent || !TreeFeature.validTreePos((LevelSimulatedReader)level, pos))
/* 142 */       return false; 
/* 144 */     BlockState foliageState = config.foliageProvider.getState(level, random, pos);
/* 145 */     if (foliageState.hasProperty((Property)BlockStateProperties.WATERLOGGED))
/* 146 */       foliageState = (BlockState)foliageState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(level.isFluidAtPosition(pos, fluidState -> fluidState.isSourceOfType((Fluid)Fluids.WATER)))); 
/* 148 */     foliageSetter.set(pos, foliageState);
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract FoliagePlacerType<?> type();
/*     */   
/*     */   protected abstract void createFoliage(WorldGenLevel paramWorldGenLevel, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   public abstract int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration);
/*     */   
/*     */   protected abstract boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
/*     */   
/*     */   public static interface FoliageSetter {
/*     */     void set(BlockPos param1BlockPos, BlockState param1BlockState);
/*     */     
/*     */     boolean isSet(BlockPos param1BlockPos);
/*     */   }
/*     */   
/*     */   public static final class FoliageAttachment {
/*     */     private final BlockPos pos;
/*     */     
/*     */     private final int radiusOffset;
/*     */     
/*     */     private final boolean doubleTrunk;
/*     */     
/*     */     public FoliageAttachment(BlockPos pos, int radiusOffset, boolean doubleTrunk) {
/* 158 */       this.pos = pos;
/* 159 */       this.radiusOffset = radiusOffset;
/* 160 */       this.doubleTrunk = doubleTrunk;
/*     */     }
/*     */     
/*     */     public BlockPos pos() {
/* 164 */       return this.pos;
/*     */     }
/*     */     
/*     */     public int radiusOffset() {
/* 168 */       return this.radiusOffset;
/*     */     }
/*     */     
/*     */     public boolean doubleTrunk() {
/* 172 */       return this.doubleTrunk;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
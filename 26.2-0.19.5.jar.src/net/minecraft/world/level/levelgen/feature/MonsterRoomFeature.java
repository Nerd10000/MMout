/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MonsterRoomFeature extends Feature<NoneFeatureConfiguration> {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  28 */   private static final EntityType<?>[] MOBS = new EntityType[] { EntityTypes.SKELETON, EntityTypes.ZOMBIE, EntityTypes.ZOMBIE, EntityTypes.SPIDER };
/*     */   
/*  29 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*     */   public MonsterRoomFeature(Codec<NoneFeatureConfiguration> codec) {
/*  32 */     super(codec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/*  37 */     Predicate<BlockState> replaceableTag = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
/*  38 */     BlockPos origin = context.origin();
/*  39 */     RandomSource random = context.random();
/*  40 */     WorldGenLevel level = context.level();
/*  41 */     int hr = 3;
/*  42 */     int xr = random.nextInt(2) + 2;
/*  43 */     int minX = -xr - 1;
/*  44 */     int maxX = xr + 1;
/*  46 */     int minY = -1;
/*  47 */     int maxY = 4;
/*  49 */     int zr = random.nextInt(2) + 2;
/*  50 */     int minZ = -zr - 1;
/*  51 */     int maxZ = zr + 1;
/*  53 */     int holeCount = 0;
/*     */     int dx;
/*  54 */     for (dx = minX; dx <= maxX; dx++) {
/*  55 */       for (int dy = -1; dy <= 4; dy++) {
/*  56 */         for (int dz = minZ; dz <= maxZ; dz++) {
/*  57 */           BlockPos holePos = origin.offset(dx, dy, dz);
/*  58 */           boolean solid = level.getBlockState(holePos).isSolid();
/*  60 */           if (dy == -1 && !solid)
/*  61 */             return false; 
/*  63 */           if (dy == 4 && !solid)
/*  64 */             return false; 
/*  67 */           if ((dx == minX || dx == maxX || dz == minZ || dz == maxZ) && 
/*  68 */             dy == 0 && level.isEmptyBlock(holePos) && level.isEmptyBlock(holePos.above()))
/*  69 */             holeCount++; 
/*     */         } 
/*     */       } 
/*     */     } 
/*  76 */     if (holeCount < 1 || holeCount > 5)
/*  77 */       return false; 
/*  80 */     for (dx = minX; dx <= maxX; dx++) {
/*  81 */       for (int dy = 3; dy >= -1; dy--) {
/*  82 */         for (int dz = minZ; dz <= maxZ; dz++) {
/*  83 */           BlockPos wallBlock = origin.offset(dx, dy, dz);
/*  85 */           BlockState wallState = level.getBlockState(wallBlock);
/*  86 */           if (dx == minX || dy == -1 || dz == minZ || dx == maxX || dy == 4 || dz == maxZ) {
/*  87 */             if (wallBlock.getY() >= level.getMinY() && !level.getBlockState(wallBlock.below()).isSolid()) {
/*  88 */               level.setBlock(wallBlock, AIR, 2);
/*  89 */             } else if (wallState.isSolid() && 
/*  90 */               !wallState.is(Blocks.CHEST)) {
/*  91 */               if (dy == -1 && random.nextInt(4) != 0) {
/*  92 */                 safeSetBlock(level, wallBlock, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), replaceableTag);
/*     */               } else {
/*  94 */                 safeSetBlock(level, wallBlock, Blocks.COBBLESTONE.defaultBlockState(), replaceableTag);
/*     */               } 
/*     */             } 
/*  99 */           } else if (!wallState.is(Blocks.CHEST) && !wallState.is(Blocks.SPAWNER)) {
/* 100 */             safeSetBlock(level, wallBlock, AIR, replaceableTag);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 107 */     for (int cc = 0; cc < 2; cc++) {
/* 108 */       for (int i = 0; i < 3; i++) {
/* 109 */         int xc = origin.getX() + random.nextInt(xr * 2 + 1) - xr;
/* 110 */         int yc = origin.getY();
/* 111 */         int zc = origin.getZ() + random.nextInt(zr * 2 + 1) - zr;
/* 112 */         BlockPos chestPos = new BlockPos(xc, yc, zc);
/* 114 */         if (level.isEmptyBlock(chestPos)) {
/* 118 */           int wallCount = 0;
/* 119 */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 120 */             if (level.getBlockState(chestPos.relative(direction)).isSolid())
/* 121 */               wallCount++; 
/*     */           } 
/* 125 */           if (wallCount == 1) {
/* 129 */             safeSetBlock(level, chestPos, StructurePiece.reorient((BlockGetter)level, chestPos, Blocks.CHEST.defaultBlockState()), replaceableTag);
/* 130 */             RandomizableContainer.setBlockEntityLootTable((BlockGetter)level, random, chestPos, BuiltInLootTables.SIMPLE_DUNGEON);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 136 */     safeSetBlock(level, origin, Blocks.SPAWNER.defaultBlockState(), replaceableTag);
/* 137 */     BlockEntity blockEntity = level.getBlockEntity(origin);
/* 139 */     if (blockEntity instanceof SpawnerBlockEntity) {
/* 139 */       SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 140 */       spawner.setEntityId(randomEntityId(random), random);
/*     */     } else {
/* 142 */       LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", new Object[] { Integer.valueOf(origin.getX()), Integer.valueOf(origin.getY()), Integer.valueOf(origin.getZ()) });
/*     */     } 
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   private EntityType<?> randomEntityId(RandomSource random) {
/* 149 */     return (EntityType)Util.getRandom((Object[])MOBS, random);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\MonsterRoomFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
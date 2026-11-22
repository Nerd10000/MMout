/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SnowyBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class SnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public SnowAndFreezeFeature(Codec<NoneFeatureConfiguration> codec) {
/* 17 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 22 */     WorldGenLevel level = context.level();
/* 23 */     BlockPos origin = context.origin();
/* 24 */     BlockPos.MutableBlockPos topPos = new BlockPos.MutableBlockPos();
/* 25 */     BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/* 27 */     for (int dx = 0; dx < 16; dx++) {
/* 28 */       for (int dz = 0; dz < 16; dz++) {
/* 29 */         int x = origin.getX() + dx;
/* 30 */         int z = origin.getZ() + dz;
/* 31 */         int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
/* 33 */         topPos.set(x, y, z);
/* 34 */         belowPos.set((Vec3i)topPos).move(Direction.DOWN, 1);
/* 36 */         Biome biome = (Biome)level.getBiome((BlockPos)topPos).value();
/* 38 */         if (biome.shouldFreeze((LevelReader)level, (BlockPos)belowPos, false))
/* 39 */           level.setBlock((BlockPos)belowPos, Blocks.ICE.defaultBlockState(), 2); 
/* 41 */         if (biome.shouldSnow((LevelReader)level, (BlockPos)topPos)) {
/* 42 */           level.setBlock((BlockPos)topPos, Blocks.SNOW.defaultBlockState(), 2);
/* 44 */           BlockState belowState = level.getBlockState((BlockPos)belowPos);
/* 45 */           if (belowState.hasProperty((Property)SnowyBlock.SNOWY))
/* 46 */             level.setBlock((BlockPos)belowPos, (BlockState)belowState.setValue((Property)SnowyBlock.SNOWY, Boolean.valueOf(true)), 2); 
/*    */         } 
/*    */       } 
/*    */     } 
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SnowAndFreezeFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
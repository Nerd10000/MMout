/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
/*    */ 
/*    */ public class MultifaceGrowthConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<MultifaceGrowthConfiguration> CODEC;
/*    */   
/*    */   public final Block placeBlock;
/*    */   
/*    */   public final int searchRange;
/*    */   
/*    */   public final boolean canPlaceOnFloor;
/*    */   
/*    */   public final boolean canPlaceOnCeiling;
/*    */   
/*    */   public final boolean canPlaceOnWall;
/*    */   
/*    */   public final float chanceOfSpreading;
/*    */   
/*    */   public final HolderSet<Block> canBePlacedOn;
/*    */   
/*    */   private final ObjectArrayList<Direction> validDirections;
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().validate(MultifaceGrowthConfiguration::validateBlock).fieldOf("block").forGetter(()), (App)Codec.intRange(1, 64).optionalFieldOf("search_range", Integer.valueOf(10)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("can_place_on_floor", Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("can_place_on_ceiling", Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("can_place_on_wall", Boolean.valueOf(false)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_spreading", Float.valueOf(0.5F)).forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_be_placed_on").forGetter(())).apply((Applicative)i, MultifaceGrowthConfiguration::new));
/*    */   }
/*    */   
/*    */   private static DataResult<Block> validateBlock(Block block) {
/* 31 */     MultifaceSpreadeableBlock multifaceBlock = (MultifaceSpreadeableBlock)block;
/* 31 */     return (block instanceof MultifaceSpreadeableBlock) ? 
/* 32 */       DataResult.success(multifaceBlock) : 
/* 33 */       DataResult.error(() -> "Growth block should be a multiface spreadeable block");
/*    */   }
/*    */   
/*    */   public MultifaceGrowthConfiguration(Block placeBlock, int searchRange, boolean canPlaceOnFloor, boolean canPlaceOnCeiling, boolean canPlaceOnWall, float chanceOfSpreading, HolderSet<Block> canBePlacedOn) {
/* 48 */     this.placeBlock = placeBlock;
/* 49 */     this.searchRange = searchRange;
/* 50 */     this.canPlaceOnFloor = canPlaceOnFloor;
/* 51 */     this.canPlaceOnCeiling = canPlaceOnCeiling;
/* 52 */     this.canPlaceOnWall = canPlaceOnWall;
/* 53 */     this.chanceOfSpreading = chanceOfSpreading;
/* 54 */     this.canBePlacedOn = canBePlacedOn;
/* 56 */     this.validDirections = new ObjectArrayList(6);
/* 57 */     if (canPlaceOnCeiling)
/* 58 */       this.validDirections.add(Direction.UP); 
/* 60 */     if (canPlaceOnFloor)
/* 61 */       this.validDirections.add(Direction.DOWN); 
/* 63 */     if (canPlaceOnWall) {
/* 64 */       Objects.requireNonNull(this.validDirections);
/* 64 */       Direction.Plane.HORIZONTAL.forEach(this.validDirections::add);
/*    */     } 
/*    */   }
/*    */   
/*    */   public List<Direction> getShuffledDirectionsExcept(RandomSource random, Direction excludeDirection) {
/* 69 */     return Util.toShuffledList(this.validDirections.stream().filter(direction -> (direction != excludeDirection)), random);
/*    */   }
/*    */   
/*    */   public List<Direction> getShuffledDirections(RandomSource random) {
/* 73 */     return Util.shuffledCopy(this.validDirections, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\MultifaceGrowthConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
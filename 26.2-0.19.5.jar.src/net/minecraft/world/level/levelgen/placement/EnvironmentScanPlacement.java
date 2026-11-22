/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public class EnvironmentScanPlacement extends PlacementModifier {
/*    */   private final Direction directionOfSearch;
/*    */   
/*    */   private final BlockPredicate targetCondition;
/*    */   
/*    */   private final BlockPredicate allowedSearchCondition;
/*    */   
/*    */   private final int maxSteps;
/*    */   
/*    */   public static final MapCodec<EnvironmentScanPlacement> CODEC;
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Direction.VERTICAL_CODEC.fieldOf("direction_of_search").forGetter(()), (App)BlockPredicate.CODEC.fieldOf("target_condition").forGetter(()), (App)BlockPredicate.CODEC.optionalFieldOf("allowed_search_condition", BlockPredicate.alwaysTrue()).forGetter(()), (App)Codec.intRange(1, 32).fieldOf("max_steps").forGetter(())).apply((Applicative)i, EnvironmentScanPlacement::new));
/*    */   }
/*    */   
/*    */   private EnvironmentScanPlacement(Direction directionOfSearch, BlockPredicate targetCondition, BlockPredicate allowedSearchCondition, int maxSteps) {
/* 34 */     this.directionOfSearch = directionOfSearch;
/* 35 */     this.targetCondition = targetCondition;
/* 36 */     this.allowedSearchCondition = allowedSearchCondition;
/* 37 */     this.maxSteps = maxSteps;
/*    */   }
/*    */   
/*    */   public static EnvironmentScanPlacement scanningFor(Direction directionOfSearch, BlockPredicate targetCondition, BlockPredicate allowedSearchCondition, int maxSteps) {
/* 41 */     return new EnvironmentScanPlacement(directionOfSearch, targetCondition, allowedSearchCondition, maxSteps);
/*    */   }
/*    */   
/*    */   public static EnvironmentScanPlacement scanningFor(Direction directionOfSearch, BlockPredicate targetCondition, int maxSteps) {
/* 45 */     return scanningFor(directionOfSearch, targetCondition, BlockPredicate.alwaysTrue(), maxSteps);
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 50 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 51 */     WorldGenLevel level = context.getLevel();
/* 52 */     if (!this.allowedSearchCondition.test(level, pos))
/* 53 */       return Stream.of(new BlockPos[0]); 
/* 56 */     for (int i = 0; i < this.maxSteps; i++) {
/* 57 */       if (this.targetCondition.test(level, pos))
/* 58 */         return (Stream)Stream.of(pos); 
/* 60 */       pos.move(this.directionOfSearch);
/* 61 */       if (level.isOutsideBuildHeight(pos.getY()))
/* 62 */         return Stream.of(new BlockPos[0]); 
/* 64 */       if (!this.allowedSearchCondition.test(level, pos))
/*    */         break; 
/*    */     } 
/* 68 */     if (this.targetCondition.test(level, pos))
/* 69 */       return (Stream)Stream.of(pos); 
/* 71 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 76 */     return PlacementModifierType.ENVIRONMENT_SCAN;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\EnvironmentScanPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
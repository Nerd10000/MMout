/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface BlockPredicateType<P extends BlockPredicate> {
/*  8 */   public static final BlockPredicateType<MatchingBlocksPredicate> MATCHING_BLOCKS = register("matching_blocks", MatchingBlocksPredicate.CODEC);
/*    */   
/*  9 */   public static final BlockPredicateType<MatchingBlockTagPredicate> MATCHING_BLOCK_TAG = register("matching_block_tag", MatchingBlockTagPredicate.CODEC);
/*    */   
/* 10 */   public static final BlockPredicateType<MatchingFluidsPredicate> MATCHING_FLUIDS = register("matching_fluids", MatchingFluidsPredicate.CODEC);
/*    */   
/* 11 */   public static final BlockPredicateType<MatchingBiomesPredicate> MATCHING_BIOMES = register("matching_biomes", MatchingBiomesPredicate.CODEC);
/*    */   
/* 12 */   public static final BlockPredicateType<HasSturdyFacePredicate> HAS_STURDY_FACE = register("has_sturdy_face", HasSturdyFacePredicate.CODEC);
/*    */   
/* 13 */   public static final BlockPredicateType<SolidPredicate> SOLID = register("solid", SolidPredicate.CODEC);
/*    */   
/* 14 */   public static final BlockPredicateType<ReplaceablePredicate> REPLACEABLE = register("replaceable", ReplaceablePredicate.CODEC);
/*    */   
/* 15 */   public static final BlockPredicateType<WouldSurvivePredicate> WOULD_SURVIVE = register("would_survive", WouldSurvivePredicate.CODEC);
/*    */   
/* 16 */   public static final BlockPredicateType<InsideWorldBoundsPredicate> INSIDE_WORLD_BOUNDS = register("inside_world_bounds", InsideWorldBoundsPredicate.CODEC);
/*    */   
/* 17 */   public static final BlockPredicateType<AnyOfPredicate> ANY_OF = register("any_of", AnyOfPredicate.CODEC);
/*    */   
/* 18 */   public static final BlockPredicateType<AllOfPredicate> ALL_OF = register("all_of", AllOfPredicate.CODEC);
/*    */   
/* 19 */   public static final BlockPredicateType<NotPredicate> NOT = register("not", NotPredicate.CODEC);
/*    */   
/* 20 */   public static final BlockPredicateType<TrueBlockPredicate> TRUE = register("true", TrueBlockPredicate.CODEC);
/*    */   
/* 21 */   public static final BlockPredicateType<UnobstructedPredicate> UNOBSTRUCTED = register("unobstructed", UnobstructedPredicate.CODEC);
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   private static <P extends BlockPredicate> BlockPredicateType<P> register(String id, MapCodec<P> codec) {
/* 26 */     return (BlockPredicateType<P>)Registry.register(BuiltInRegistries.BLOCK_PREDICATE_TYPE, id, () -> codec);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\BlockPredicateType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class RarityFilter extends PlacementFilter {
/*    */   public static final MapCodec<RarityFilter> CODEC;
/*    */   
/*    */   private final int chance;
/*    */   
/*    */   static {
/* 10 */     CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("chance").xmap(RarityFilter::new, c -> Integer.valueOf(c.chance));
/*    */   }
/*    */   
/*    */   private RarityFilter(int chance) {
/* 15 */     this.chance = chance;
/*    */   }
/*    */   
/*    */   public static RarityFilter onAverageOnceEvery(int chance) {
/* 19 */     return new RarityFilter(chance);
/*    */   }
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 24 */     return (random.nextFloat() < 1.0F / this.chance);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 29 */     return PlacementModifierType.RARITY_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\RarityFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
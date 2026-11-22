/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ 
/*    */ public class CountPlacement extends RepeatingPlacement {
/*    */   public static final MapCodec<CountPlacement> CODEC;
/*    */   
/*    */   private final IntProvider count;
/*    */   
/*    */   static {
/* 13 */     CODEC = IntProviders.codec(0, 4096).fieldOf("count").xmap(CountPlacement::new, c -> c.count);
/*    */   }
/*    */   
/*    */   private CountPlacement(IntProvider count) {
/* 18 */     this.count = count;
/*    */   }
/*    */   
/*    */   public static CountPlacement of(IntProvider count) {
/* 22 */     return new CountPlacement(count);
/*    */   }
/*    */   
/*    */   public static CountPlacement of(int count) {
/* 26 */     return of((IntProvider)ConstantInt.of(count));
/*    */   }
/*    */   
/*    */   protected int count(RandomSource random, BlockPos origin) {
/* 31 */     return this.count.sample(random);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 36 */     return PlacementModifierType.COUNT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\CountPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
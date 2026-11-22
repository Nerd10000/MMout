/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.function.LongFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public enum Algorithm {
/* 84 */   LEGACY(LegacyRandomSource::new),
/* 85 */   XOROSHIRO(XoroshiroRandomSource::new);
/*    */   
/*    */   private final LongFunction<RandomSource> constructor;
/*    */   
/*    */   Algorithm(LongFunction<RandomSource> constructor) {
/* 91 */     this.constructor = constructor;
/*    */   }
/*    */   
/*    */   public RandomSource newInstance(long seed) {
/* 95 */     return this.constructor.apply(seed);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\WorldgenRandom$Algorithm.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class LegacyPositionalRandomFactory implements PositionalRandomFactory {
/*    */   private final long seed;
/*    */   
/*    */   public LegacyPositionalRandomFactory(long seed) {
/* 62 */     this.seed = seed;
/*    */   }
/*    */   
/*    */   public RandomSource at(int x, int y, int z) {
/* 67 */     long positionalSeed = Mth.getSeed(x, y, z);
/* 68 */     long randomSeed = positionalSeed ^ this.seed;
/* 69 */     return new LegacyRandomSource(randomSeed);
/*    */   }
/*    */   
/*    */   public RandomSource fromHashOf(String name) {
/* 74 */     int positionalSeed = name.hashCode();
/* 75 */     return new LegacyRandomSource(positionalSeed ^ this.seed);
/*    */   }
/*    */   
/*    */   public RandomSource fromSeed(long seed) {
/* 80 */     return new LegacyRandomSource(seed);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public void parityConfigString(StringBuilder sb) {
/* 86 */     sb.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\LegacyRandomSource$LegacyPositionalRandomFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
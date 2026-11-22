/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class XoroshiroPositionalRandomFactory implements PositionalRandomFactory {
/*     */   private final long seedLo;
/*     */   
/*     */   private final long seedHi;
/*     */   
/*     */   public XoroshiroPositionalRandomFactory(long seedLo, long seedHi) {
/* 135 */     this.seedLo = seedLo;
/* 136 */     this.seedHi = seedHi;
/*     */   }
/*     */   
/*     */   public RandomSource at(int x, int y, int z) {
/* 141 */     long positionalSeed = Mth.getSeed(x, y, z);
/* 142 */     long randomSeed = positionalSeed ^ this.seedLo;
/* 143 */     return new XoroshiroRandomSource(randomSeed, this.seedHi);
/*     */   }
/*     */   
/*     */   public RandomSource fromHashOf(String name) {
/* 148 */     RandomSupport.Seed128bit seed = RandomSupport.seedFromHashOf(name);
/* 149 */     return new XoroshiroRandomSource(seed.xor(this.seedLo, this.seedHi));
/*     */   }
/*     */   
/*     */   public RandomSource fromSeed(long seed) {
/* 154 */     return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder sb) {
/* 160 */     sb.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\XoroshiroRandomSource$XoroshiroPositionalRandomFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
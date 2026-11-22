/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedListInt implements IntProvider {
/*    */   public static final MapCodec<WeightedListInt> MAP_CODEC;
/*    */   
/*    */   private final WeightedList<IntProvider> distribution;
/*    */   
/*    */   private final int minValue;
/*    */   
/*    */   private final int maxValue;
/*    */   
/*    */   static {
/* 10 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(IntProviders.CODEC).fieldOf("distribution").forGetter(())).apply((Applicative)i, WeightedListInt::new));
/*    */   }
/*    */   
/*    */   public WeightedListInt(WeightedList<IntProvider> distribution) {
/* 19 */     this.distribution = distribution;
/* 20 */     int min = Integer.MAX_VALUE;
/* 21 */     int max = Integer.MIN_VALUE;
/* 22 */     for (Weighted<IntProvider> value : (Iterable<Weighted<IntProvider>>)distribution.unwrap()) {
/* 23 */       int entryMin = ((IntProvider)value.value()).minInclusive();
/* 24 */       int entryMax = ((IntProvider)value.value()).maxInclusive();
/* 25 */       min = Math.min(min, entryMin);
/* 26 */       max = Math.max(max, entryMax);
/*    */     } 
/* 28 */     this.minValue = min;
/* 29 */     this.maxValue = max;
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random) {
/* 34 */     return ((IntProvider)this.distribution.getRandomOrThrow(random)).sample(random);
/*    */   }
/*    */   
/*    */   public int minInclusive() {
/* 39 */     return this.minValue;
/*    */   }
/*    */   
/*    */   public int maxInclusive() {
/* 44 */     return this.maxValue;
/*    */   }
/*    */   
/*    */   public MapCodec<WeightedListInt> codec() {
/* 49 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\WeightedListInt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class BiasedToBottomHeight extends HeightProvider {
/*    */   public static final MapCodec<BiasedToBottomHeight> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(()), (App)Codec.intRange(1, 2147483647).optionalFieldOf("inner", Integer.valueOf(1)).forGetter(())).apply((Applicative)i, BiasedToBottomHeight::new));
/*    */   }
/*    */   
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   
/*    */   private final VerticalAnchor maxInclusive;
/*    */   
/*    */   private final int inner;
/*    */   
/*    */   private BiasedToBottomHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int inner) {
/* 26 */     this.minInclusive = minInclusive;
/* 27 */     this.maxInclusive = maxInclusive;
/* 28 */     this.inner = inner;
/*    */   }
/*    */   
/*    */   public static BiasedToBottomHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int offset) {
/* 32 */     return new BiasedToBottomHeight(minInclusive, maxInclusive, offset);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 37 */     int min = this.minInclusive.resolveY(context);
/* 38 */     int max = this.maxInclusive.resolveY(context);
/* 39 */     if (max - min - this.inner + 1 <= 0) {
/* 40 */       LOGGER.warn("Empty height range: {}", this);
/* 41 */       return min;
/*    */     } 
/* 44 */     int limit = random.nextInt(max - min - this.inner + 1);
/* 45 */     return random.nextInt(limit + this.inner) + min;
/*    */   }
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 50 */     return HeightProviderType.BIASED_TO_BOTTOM;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 55 */     return "biased[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + " inner: " + this.inner + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\heightproviders\BiasedToBottomHeight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
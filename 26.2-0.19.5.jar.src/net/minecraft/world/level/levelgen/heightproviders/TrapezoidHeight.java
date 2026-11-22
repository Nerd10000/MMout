/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class TrapezoidHeight extends HeightProvider {
/*    */   public static final MapCodec<TrapezoidHeight> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(()), (App)Codec.INT.optionalFieldOf("plateau", Integer.valueOf(0)).forGetter(())).apply((Applicative)i, TrapezoidHeight::new));
/*    */   }
/*    */   
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   
/*    */   private final VerticalAnchor maxInclusive;
/*    */   
/*    */   private final int plateau;
/*    */   
/*    */   private TrapezoidHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int plateau) {
/* 27 */     this.minInclusive = minInclusive;
/* 28 */     this.maxInclusive = maxInclusive;
/* 29 */     this.plateau = plateau;
/*    */   }
/*    */   
/*    */   public static TrapezoidHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int plateau) {
/* 33 */     return new TrapezoidHeight(minInclusive, maxInclusive, plateau);
/*    */   }
/*    */   
/*    */   public static TrapezoidHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 37 */     return of(minInclusive, maxInclusive, 0);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 42 */     int min = this.minInclusive.resolveY(context);
/* 43 */     int max = this.maxInclusive.resolveY(context);
/* 44 */     if (min > max) {
/* 45 */       LOGGER.warn("Empty height range: {}", this);
/* 46 */       return min;
/*    */     } 
/* 49 */     int range = max - min;
/* 50 */     if (this.plateau >= range)
/* 51 */       return Mth.randomBetweenInclusive(random, min, max); 
/* 54 */     int plateauStart = (range - this.plateau) / 2;
/* 55 */     int plateauEnd = range - plateauStart;
/* 57 */     return min + Mth.randomBetweenInclusive(random, 0, plateauEnd) + Mth.randomBetweenInclusive(random, 0, plateauStart);
/*    */   }
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 62 */     return HeightProviderType.TRAPEZOID;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 67 */     return (this.plateau == 0) ? ("triangle (" + 
/* 68 */       String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + ")") : ("trapezoid(" + 
/*    */       
/* 70 */       this.plateau + ") in [" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\heightproviders\TrapezoidHeight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
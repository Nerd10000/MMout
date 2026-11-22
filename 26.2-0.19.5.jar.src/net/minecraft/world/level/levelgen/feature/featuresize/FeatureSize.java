/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public abstract class FeatureSize {
/* 12 */   public static final Codec<FeatureSize> CODEC = BuiltInRegistries.FEATURE_SIZE_TYPE.byNameCodec().dispatch(FeatureSize::type, FeatureSizeType::codec);
/*    */   
/*    */   protected static final int MAX_WIDTH = 16;
/*    */   
/*    */   protected final OptionalInt minClippedHeight;
/*    */   
/*    */   protected static <S extends FeatureSize> RecordCodecBuilder<S, OptionalInt> minClippedHeightCodec() {
/* 16 */     return Codec.intRange(0, 80).optionalFieldOf("min_clipped_height")
/* 17 */       .xmap(o -> (OptionalInt)o.map(OptionalInt::of).orElse(OptionalInt.empty()), o -> o.isPresent() ? Optional.<Integer>of(Integer.valueOf(o.getAsInt())) : Optional.empty()).forGetter(f -> f.minClippedHeight);
/*    */   }
/*    */   
/*    */   public FeatureSize(OptionalInt minClippedHeight) {
/* 23 */     this.minClippedHeight = minClippedHeight;
/*    */   }
/*    */   
/*    */   public OptionalInt minClippedHeight() {
/* 31 */     return this.minClippedHeight;
/*    */   }
/*    */   
/*    */   protected abstract FeatureSizeType<?> type();
/*    */   
/*    */   public abstract int getSizeAtHeight(int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\featuresize\FeatureSize.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
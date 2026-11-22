/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class WeightedListHeight extends HeightProvider {
/*    */   public static final MapCodec<WeightedListHeight> CODEC;
/*    */   
/*    */   private final WeightedList<HeightProvider> distribution;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(HeightProvider.CODEC).fieldOf("distribution").forGetter(())).apply((Applicative)i, WeightedListHeight::new));
/*    */   }
/*    */   
/*    */   public WeightedListHeight(WeightedList<HeightProvider> distribution) {
/* 17 */     this.distribution = distribution;
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext heightAccessor) {
/* 22 */     return ((HeightProvider)this.distribution.getRandomOrThrow(random)).sample(random, heightAccessor);
/*    */   }
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 27 */     return HeightProviderType.WEIGHTED_LIST;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\heightproviders\WeightedListHeight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
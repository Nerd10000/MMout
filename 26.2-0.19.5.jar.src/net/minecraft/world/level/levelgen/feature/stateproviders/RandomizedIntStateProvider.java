/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RandomizedIntStateProvider extends BlockStateProvider {
/*    */   public static final MapCodec<RandomizedIntStateProvider> CODEC;
/*    */   
/*    */   private final BlockStateProvider source;
/*    */   
/*    */   private final String propertyName;
/*    */   
/*    */   private IntegerProperty property;
/*    */   
/*    */   private final IntProvider values;
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("source").forGetter(()), (App)Codec.STRING.fieldOf("property").forGetter(()), (App)IntProviders.CODEC.fieldOf("values").forGetter(())).apply((Applicative)i, RandomizedIntStateProvider::new));
/*    */   }
/*    */   
/*    */   public RandomizedIntStateProvider(BlockStateProvider source, IntegerProperty property, IntProvider values) {
/* 32 */     this.source = source;
/* 33 */     this.property = property;
/* 34 */     this.propertyName = property.getName();
/* 35 */     this.values = values;
/* 37 */     Collection<Integer> possibleValues = property.getPossibleValues();
/* 38 */     for (int i = values.minInclusive(); i <= values.maxInclusive(); i++) {
/* 39 */       if (!possibleValues.contains(Integer.valueOf(i)))
/* 40 */         throw new IllegalArgumentException("Property value out of range: " + property.getName() + ": " + i); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public RandomizedIntStateProvider(BlockStateProvider source, String propertyName, IntProvider values) {
/* 46 */     this.source = source;
/* 47 */     this.propertyName = propertyName;
/* 48 */     this.values = values;
/*    */   }
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 53 */     return BlockStateProviderType.RANDOMIZED_INT_STATE_PROVIDER;
/*    */   }
/*    */   
/*    */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 58 */     BlockState unmodifiedState = this.source.getState(level, random, pos);
/* 59 */     if (this.property == null || !unmodifiedState.hasProperty((Property)this.property)) {
/* 60 */       IntegerProperty property = findProperty(unmodifiedState, this.propertyName);
/* 61 */       if (property == null)
/* 62 */         return unmodifiedState; 
/* 64 */       this.property = property;
/*    */     } 
/* 66 */     return (BlockState)unmodifiedState.setValue((Property)this.property, Integer.valueOf(this.values.sample(random)));
/*    */   }
/*    */   
/*    */   private static IntegerProperty findProperty(BlockState source, String propertyName) {
/* 70 */     Collection<Property<?>> properties = source.getProperties();
/* 74 */     Optional<IntegerProperty> found = properties.stream().filter(p -> p.getName().equals(propertyName)).filter(p -> p instanceof IntegerProperty).map(p -> (IntegerProperty)p).findAny();
/* 75 */     return found.orElse(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RandomizedIntStateProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
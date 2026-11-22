/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class LayerConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<LayerConfiguration> CODEC;
/*    */   
/*    */   public final int height;
/*    */   
/*    */   public final BlockState state;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)i, LayerConfiguration::new));
/*    */   }
/*    */   
/*    */   public LayerConfiguration(int height, BlockState state) {
/* 18 */     this.height = height;
/* 19 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\LayerConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
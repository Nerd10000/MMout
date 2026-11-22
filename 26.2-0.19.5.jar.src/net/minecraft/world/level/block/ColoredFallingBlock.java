/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ColoredFallingBlock extends FallingBlock {
/*    */   public static final MapCodec<ColoredFallingBlock> CODEC;
/*    */   
/*    */   protected final ColorRGBA dustColor;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ColoredFallingBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<? extends ColoredFallingBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   public ColoredFallingBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) {
/* 24 */     super(properties);
/* 25 */     this.dustColor = dustColor;
/*    */   }
/*    */   
/*    */   public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
/* 30 */     return this.dustColor.rgba();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ColoredFallingBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
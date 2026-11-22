/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SandBlock extends ColoredFallingBlock {
/*    */   public static final MapCodec<SandBlock> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, SandBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<SandBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SandBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) {
/* 24 */     super(dustColor, properties);
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 29 */     super.animateTick(state, level, pos, random);
/* 30 */     AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SandBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
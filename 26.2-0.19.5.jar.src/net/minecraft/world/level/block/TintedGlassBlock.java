/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TintedGlassBlock extends TransparentBlock {
/*  8 */   public static final MapCodec<TintedGlassBlock> CODEC = simpleCodec(TintedGlassBlock::new);
/*    */   
/*    */   public MapCodec<TintedGlassBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   
/*    */   public TintedGlassBlock(BlockBehaviour.Properties properties) {
/* 15 */     super(properties);
/*    */   }
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState state) {
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   protected int getLightDampening(BlockState state) {
/* 25 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TintedGlassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
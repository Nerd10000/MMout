/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class PlayerHeadBlock extends SkullBlock {
/*  6 */   public static final MapCodec<PlayerHeadBlock> CODEC = simpleCodec(PlayerHeadBlock::new);
/*    */   
/*    */   public MapCodec<PlayerHeadBlock> codec() {
/* 10 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected PlayerHeadBlock(BlockBehaviour.Properties properties) {
/* 14 */     super(SkullBlock.Types.PLAYER, properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PlayerHeadBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
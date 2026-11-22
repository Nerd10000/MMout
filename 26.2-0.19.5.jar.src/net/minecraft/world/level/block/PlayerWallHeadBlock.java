/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class PlayerWallHeadBlock extends WallSkullBlock {
/*  6 */   public static final MapCodec<PlayerWallHeadBlock> CODEC = simpleCodec(PlayerWallHeadBlock::new);
/*    */   
/*    */   public MapCodec<PlayerWallHeadBlock> codec() {
/* 10 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected PlayerWallHeadBlock(BlockBehaviour.Properties properties) {
/* 14 */     super(SkullBlock.Types.PLAYER, properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PlayerWallHeadBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
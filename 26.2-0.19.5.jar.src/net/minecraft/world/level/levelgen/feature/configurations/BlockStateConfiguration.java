/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<BlockStateConfiguration> CODEC;
/*    */   
/*    */   public final BlockState state;
/*    */   
/*    */   static {
/*  7 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, c -> c.state).codec();
/*    */   }
/*    */   
/*    */   public BlockStateConfiguration(BlockState state) {
/* 12 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockStateConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class BlockPileConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<BlockPileConfiguration> CODEC;
/*    */   
/*    */   public final BlockStateProvider stateProvider;
/*    */   
/*    */   static {
/*  7 */     CODEC = BlockStateProvider.CODEC.fieldOf("state_provider").xmap(BlockPileConfiguration::new, c -> c.stateProvider).codec();
/*    */   }
/*    */   
/*    */   public BlockPileConfiguration(BlockStateProvider stateProvider) {
/* 12 */     this.stateProvider = stateProvider;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockPileConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
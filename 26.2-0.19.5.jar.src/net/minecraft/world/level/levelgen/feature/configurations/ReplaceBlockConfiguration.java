/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*    */ 
/*    */ public class ReplaceBlockConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<ReplaceBlockConfiguration> CODEC;
/*    */   
/*    */   public final List<OreConfiguration.TargetBlockState> targetStates;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(())).apply((Applicative)i, ReplaceBlockConfiguration::new));
/*    */   }
/*    */   
/*    */   public ReplaceBlockConfiguration(BlockState targetState, BlockState state) {
/* 19 */     this((List<OreConfiguration.TargetBlockState>)ImmutableList.of(OreConfiguration.target((RuleTest)new BlockStateMatchTest(targetState), state)));
/*    */   }
/*    */   
/*    */   public ReplaceBlockConfiguration(List<OreConfiguration.TargetBlockState> targetBlockStates) {
/* 23 */     this.targetStates = targetBlockStates;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceBlockConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
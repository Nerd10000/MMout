/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class SpringConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<SpringConfiguration> CODEC;
/*    */   
/*    */   public final FluidState state;
/*    */   
/*    */   public final boolean requiresBlockBelow;
/*    */   
/*    */   public final int rockCount;
/*    */   
/*    */   public final int holeCount;
/*    */   
/*    */   public final HolderSet<Block> validBlocks;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)FluidState.CODEC.fieldOf("state").forGetter(()), (App)Codec.BOOL.optionalFieldOf("requires_block_below", Boolean.valueOf(true)).forGetter(()), (App)Codec.INT.optionalFieldOf("rock_count", Integer.valueOf(4)).forGetter(()), (App)Codec.INT.optionalFieldOf("hole_count", Integer.valueOf(1)).forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("valid_blocks").forGetter(())).apply((Applicative)i, SpringConfiguration::new));
/*    */   }
/*    */   
/*    */   public SpringConfiguration(FluidState state, boolean requiresBlockBelow, int rockCount, int holeCount, HolderSet<Block> validBlocks) {
/* 27 */     this.state = state;
/* 28 */     this.requiresBlockBelow = requiresBlockBelow;
/* 29 */     this.rockCount = rockCount;
/* 30 */     this.holeCount = holeCount;
/* 31 */     this.validBlocks = validBlocks;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpringConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
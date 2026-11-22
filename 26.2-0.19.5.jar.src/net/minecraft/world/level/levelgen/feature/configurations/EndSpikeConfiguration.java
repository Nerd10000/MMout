/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
/*    */ 
/*    */ public class EndSpikeConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<EndSpikeConfiguration> CODEC;
/*    */   
/*    */   private final boolean crystalInvulnerable;
/*    */   
/*    */   private final List<EndSpikeFeature.EndSpike> spikes;
/*    */   
/*    */   private final BlockPos crystalBeamTarget;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("crystal_invulnerable", Boolean.valueOf(false)).forGetter(()), (App)EndSpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(()), (App)BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(())).apply((Applicative)i, EndSpikeConfiguration::new));
/*    */   }
/*    */   
/*    */   public EndSpikeConfiguration(boolean crystalInvulnerable, List<EndSpikeFeature.EndSpike> spikes, BlockPos crystalBeamTarget) {
/* 24 */     this(crystalInvulnerable, spikes, Optional.ofNullable(crystalBeamTarget));
/*    */   }
/*    */   
/*    */   private EndSpikeConfiguration(boolean crystalInvulnerable, List<EndSpikeFeature.EndSpike> spikes, Optional<BlockPos> crystalBeamTarget) {
/* 28 */     this.crystalInvulnerable = crystalInvulnerable;
/* 29 */     this.spikes = spikes;
/* 30 */     this.crystalBeamTarget = crystalBeamTarget.orElse(null);
/*    */   }
/*    */   
/*    */   public boolean isCrystalInvulnerable() {
/* 34 */     return this.crystalInvulnerable;
/*    */   }
/*    */   
/*    */   public List<EndSpikeFeature.EndSpike> getSpikes() {
/* 38 */     return this.spikes;
/*    */   }
/*    */   
/*    */   public BlockPos getCrystalBeamTarget() {
/* 42 */     return this.crystalBeamTarget;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\EndSpikeConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
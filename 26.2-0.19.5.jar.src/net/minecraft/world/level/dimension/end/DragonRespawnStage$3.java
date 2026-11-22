/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.EndSpikeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ enum null {
/*    */   public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/* 45 */     int interval = 40;
/* 46 */     boolean startOfBeam = (time % 40 == 0);
/* 47 */     boolean endOfBeam = (time % 40 == 39);
/* 48 */     if (startOfBeam || endOfBeam) {
/* 49 */       List<EndSpikeFeature.EndSpike> spikes = EndSpikeFeature.getSpikesForLevel((WorldGenLevel)level);
/* 50 */       int index = time / 40;
/* 51 */       if (index < spikes.size()) {
/* 52 */         EndSpikeFeature.EndSpike spike = spikes.get(index);
/* 54 */         if (startOfBeam) {
/* 55 */           for (EndCrystal respawnCrystal : crystals)
/* 56 */             respawnCrystal.setBeamTarget(new BlockPos(spike.getCenterX(), spike.getHeight() + 1, spike.getCenterZ())); 
/*    */         } else {
/* 59 */           int radius = 10;
/* 60 */           for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(spike
/* 61 */                 .getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10), new BlockPos(spike
/* 62 */                 .getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10)))
/* 64 */             level.removeBlock(pos, false); 
/* 66 */           level.explode(null, (spike.getCenterX() + 0.5F), spike.getHeight(), (spike.getCenterZ() + 0.5F), 5.0F, Level.ExplosionInteraction.BLOCK);
/* 68 */           EndSpikeConfiguration configuration = new EndSpikeConfiguration(true, (List)ImmutableList.of(spike), new BlockPos(0, 128, 0));
/* 69 */           Feature.END_SPIKE.place((FeatureConfiguration)configuration, (WorldGenLevel)level, level.getChunkSource().getGenerator(), RandomSource.create(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
/*    */         } 
/* 71 */       } else if (startOfBeam) {
/* 72 */         fight.setRespawnStage(SUMMONING_DRAGON);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\end\DragonRespawnStage$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
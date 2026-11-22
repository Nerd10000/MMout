/*     */ package net.minecraft.world.level.dimension.end;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndSpikeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ 
/*     */ public enum DragonRespawnStage implements StringRepresentable {
/*  20 */   START("start") {
/*     */     public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/*  23 */       BlockPos beamPos = new BlockPos(0, 128, 0);
/*  24 */       for (EndCrystal respawnCrystal : crystals)
/*  25 */         respawnCrystal.setBeamTarget(beamPos); 
/*  27 */       fight.setRespawnStage(PREPARING_TO_SUMMON_PILLARS);
/*     */     }
/*     */   },
/*  30 */   PREPARING_TO_SUMMON_PILLARS("preparing_to_summon_pillars") {
/*     */     public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/*  33 */       if (time < 100) {
/*  34 */         if (time == 0 || time == 50 || time == 51 || time == 52 || time >= 95)
/*  35 */           level.levelEvent(3001, new BlockPos(0, 128, 0), 0); 
/*     */       } else {
/*  38 */         fight.setRespawnStage(SUMMONING_PILLARS);
/*     */       } 
/*     */     }
/*     */   },
/*  42 */   SUMMONING_PILLARS("summoning_pillars") {
/*     */     public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/*  45 */       int interval = 40;
/*  46 */       boolean startOfBeam = (time % 40 == 0);
/*  47 */       boolean endOfBeam = (time % 40 == 39);
/*  48 */       if (startOfBeam || endOfBeam) {
/*  49 */         List<EndSpikeFeature.EndSpike> spikes = EndSpikeFeature.getSpikesForLevel((WorldGenLevel)level);
/*  50 */         int index = time / 40;
/*  51 */         if (index < spikes.size()) {
/*  52 */           EndSpikeFeature.EndSpike spike = spikes.get(index);
/*  54 */           if (startOfBeam) {
/*  55 */             for (EndCrystal respawnCrystal : crystals)
/*  56 */               respawnCrystal.setBeamTarget(new BlockPos(spike.getCenterX(), spike.getHeight() + 1, spike.getCenterZ())); 
/*     */           } else {
/*  59 */             int radius = 10;
/*  60 */             for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(spike
/*  61 */                   .getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10), new BlockPos(spike
/*  62 */                   .getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10)))
/*  64 */               level.removeBlock(pos, false); 
/*  66 */             level.explode(null, (spike.getCenterX() + 0.5F), spike.getHeight(), (spike.getCenterZ() + 0.5F), 5.0F, Level.ExplosionInteraction.BLOCK);
/*  68 */             EndSpikeConfiguration configuration = new EndSpikeConfiguration(true, (List)ImmutableList.of(spike), new BlockPos(0, 128, 0));
/*  69 */             Feature.END_SPIKE.place((FeatureConfiguration)configuration, (WorldGenLevel)level, level.getChunkSource().getGenerator(), RandomSource.create(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
/*     */           } 
/*  71 */         } else if (startOfBeam) {
/*  72 */           fight.setRespawnStage(SUMMONING_DRAGON);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   },
/*  77 */   SUMMONING_DRAGON("summoning_dragon") {
/*     */     public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/*  80 */       if (time >= 100) {
/*  81 */         fight.setRespawnStage(END);
/*  82 */         fight.resetSpikeCrystals();
/*  83 */         for (EndCrystal crystal : crystals) {
/*  84 */           crystal.setBeamTarget(null);
/*  85 */           level.explode((Entity)crystal, crystal.getX(), crystal.getY(), crystal.getZ(), 6.0F, Level.ExplosionInteraction.NONE);
/*  86 */           crystal.discard();
/*     */         } 
/*  88 */       } else if (time >= 80) {
/*  89 */         level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*  90 */       } else if (time == 0) {
/*  91 */         for (EndCrystal crystal : crystals)
/*  92 */           crystal.setBeamTarget(new BlockPos(0, 128, 0)); 
/*  94 */       } else if (time < 5) {
/*  95 */         level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*     */       } 
/*     */     }
/*     */   },
/*  99 */   END("end") {
/*     */     public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {}
/*     */   };
/*     */   
/*     */   public static final Codec<DragonRespawnStage> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 107 */     CODEC = (Codec<DragonRespawnStage>)StringRepresentable.fromEnum(DragonRespawnStage::values);
/*     */   }
/*     */   
/*     */   DragonRespawnStage(String name) {
/* 111 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 116 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract void tick(ServerLevel paramServerLevel, EnderDragonFight paramEnderDragonFight, List<EndCrystal> paramList, int paramInt);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\end\DragonRespawnStage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
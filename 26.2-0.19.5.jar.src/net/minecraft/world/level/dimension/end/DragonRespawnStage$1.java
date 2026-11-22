/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ 
/*    */ enum null {
/*    */   public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/* 23 */     BlockPos beamPos = new BlockPos(0, 128, 0);
/* 24 */     for (EndCrystal respawnCrystal : crystals)
/* 25 */       respawnCrystal.setBeamTarget(beamPos); 
/* 27 */     fight.setRespawnStage(PREPARING_TO_SUMMON_PILLARS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\end\DragonRespawnStage$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
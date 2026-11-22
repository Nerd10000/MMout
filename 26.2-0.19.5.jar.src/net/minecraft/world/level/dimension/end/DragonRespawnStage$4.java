/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ enum null {
/*    */   public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/* 80 */     if (time >= 100) {
/* 81 */       fight.setRespawnStage(END);
/* 82 */       fight.resetSpikeCrystals();
/* 83 */       for (EndCrystal crystal : crystals) {
/* 84 */         crystal.setBeamTarget(null);
/* 85 */         level.explode((Entity)crystal, crystal.getX(), crystal.getY(), crystal.getZ(), 6.0F, Level.ExplosionInteraction.NONE);
/* 86 */         crystal.discard();
/*    */       } 
/* 88 */     } else if (time >= 80) {
/* 89 */       level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/* 90 */     } else if (time == 0) {
/* 91 */       for (EndCrystal crystal : crystals)
/* 92 */         crystal.setBeamTarget(new BlockPos(0, 128, 0)); 
/* 94 */     } else if (time < 5) {
/* 95 */       level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\end\DragonRespawnStage$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
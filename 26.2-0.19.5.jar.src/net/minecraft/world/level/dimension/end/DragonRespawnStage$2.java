/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ 
/*    */ enum null {
/*    */   public void tick(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time) {
/* 33 */     if (time < 100) {
/* 34 */       if (time == 0 || time == 50 || time == 51 || time == 52 || time >= 95)
/* 35 */         level.levelEvent(3001, new BlockPos(0, 128, 0), 0); 
/*    */     } else {
/* 38 */       fight.setRespawnStage(SUMMONING_PILLARS);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\end\DragonRespawnStage$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
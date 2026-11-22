/*    */ package net.minecraft.world.entity.animal.sniffer;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*    */ 
/*    */ class null extends AnimalPanic<Sniffer> {
/*    */   null(float speedMultiplier) {
/* 72 */     super(speedMultiplier);
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, Sniffer body, long timestamp) {
/* 75 */     SnifferAi.resetSniffing(body);
/* 76 */     super.start(level, (PathfinderMob)body, timestamp);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
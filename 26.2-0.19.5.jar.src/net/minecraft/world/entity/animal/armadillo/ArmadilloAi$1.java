/*    */ package net.minecraft.world.entity.animal.armadillo;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*    */ 
/*    */ class null extends MoveToTargetSink {
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/* 73 */     if (body instanceof Armadillo) {
/* 73 */       Armadillo armadillo = (Armadillo)body;
/* 73 */       if (armadillo.isScared())
/* 74 */         return false; 
/*    */     } 
/* 76 */     return super.checkExtraStartConditions(level, body);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\armadillo\ArmadilloAi$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
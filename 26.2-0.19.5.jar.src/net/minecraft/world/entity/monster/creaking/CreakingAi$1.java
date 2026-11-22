/*    */ package net.minecraft.world.entity.monster.creaking;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Swim;
/*    */ 
/*    */ class null extends Swim<Creaking> {
/*    */   null(float chance) {
/* 35 */     super(chance);
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Creaking body) {
/* 38 */     return (body.canMove() && super.checkExtraStartConditions(level, (LivingEntity)body));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\CreakingAi$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
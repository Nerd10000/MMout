/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ 
/*    */ public class ForceUnmount extends Behavior<LivingEntity> {
/*    */   public ForceUnmount() {
/* 10 */     super((Map)ImmutableMap.of());
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity body) {
/* 15 */     return body.isPassenger();
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, LivingEntity body, long timestamp) {
/* 20 */     body.unRide();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\warden\ForceUnmount.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
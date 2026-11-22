/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.cubemob.Slime;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface NearbySlimes {
/*    */   int count(int paramInt);
/*    */   
/*    */   private static NearbySlimes closeTo(LivingEntity mob) {
/* 62 */     return maxResults -> {
/*    */         List<Slime> slimesNearby = new ArrayList<>();
/*    */         mob.level().getEntities((EntityTypeTest)EntityTypes.SLIME, mob.getBoundingBox().inflate(2.0D), (), slimesNearby, maxResults);
/*    */         return slimesNearby.size();
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\OozingMobEffect$NearbySlimes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ class null implements PlayerDetector.EntitySelector {
/*    */   public List<ServerPlayer> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/* 58 */     return level.getPlayers(selector);
/*    */   }
/*    */   
/*    */   public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB aabb, Predicate<? super T> selector) {
/* 63 */     return level.getEntities(type, aabb, selector);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector$EntitySelector$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
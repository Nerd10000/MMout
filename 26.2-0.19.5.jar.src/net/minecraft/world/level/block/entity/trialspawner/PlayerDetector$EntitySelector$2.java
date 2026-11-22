/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ class null implements PlayerDetector.EntitySelector {
/*    */   public List<Player> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/* 75 */     return players.stream()
/* 76 */       .filter(selector)
/* 77 */       .toList();
/*    */   }
/*    */   
/*    */   public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 83 */     Objects.requireNonNull(type);
/* 83 */     return players.stream().map(type::tryCast)
/* 84 */       .filter(Objects::nonNull)
/* 85 */       .filter(selector)
/* 86 */       .toList();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector$EntitySelector$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
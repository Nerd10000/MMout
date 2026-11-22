/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ 
/*    */ public interface PlayerDetector {
/*    */   public static final PlayerDetector NO_CREATIVE_PLAYERS;
/*    */   
/*    */   public static final PlayerDetector INCLUDING_CREATIVE_PLAYERS;
/*    */   
/*    */   public static final PlayerDetector SHEEP;
/*    */   
/*    */   static {
/* 25 */     NO_CREATIVE_PLAYERS = ((level, selector, pos, requiredPlayerRange, requireLineOfSight) -> selector.getPlayers(level, ()).stream().filter(()).map(Entity::getUUID).toList());
/* 30 */     INCLUDING_CREATIVE_PLAYERS = ((level, selector, pos, requiredPlayerRange, requireLineOfSight) -> selector.getPlayers(level, ()).stream().filter(()).map(Entity::getUUID).toList());
/* 35 */     SHEEP = ((level, selector, pos, requiredPlayerRange, requireLineOfSight) -> {
/*    */         AABB area = (new AABB(pos)).inflate(requiredPlayerRange);
/*    */         return selector.<Entity>getEntities(level, (EntityTypeTest<Entity, Entity>)EntityTypes.SHEEP, area, LivingEntity::isAlive).stream().filter(()).map(Entity::getUUID).toList();
/*    */       });
/*    */   }
/*    */   
/*    */   private static boolean inLineOfSight(Level level, Vec3 origin, Vec3 dest) {
/* 46 */     BlockHitResult hitResult = level.clip(new ClipContext(dest, origin, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
/* 47 */     return (hitResult.getBlockPos().equals(BlockPos.containing((Position)origin)) || hitResult.getType() == HitResult.Type.MISS);
/*    */   }
/*    */   
/*    */   List<UUID> detect(ServerLevel paramServerLevel, EntitySelector paramEntitySelector, BlockPos paramBlockPos, double paramDouble, boolean paramBoolean);
/*    */   
/*    */   class null implements EntitySelector {
/*    */     public List<ServerPlayer> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/* 58 */       return level.getPlayers(selector);
/*    */     }
/*    */     
/*    */     public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB aabb, Predicate<? super T> selector) {
/* 63 */       return level.getEntities(type, aabb, selector);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface EntitySelector {
/*    */     public static final EntitySelector SELECT_FROM_LEVEL = new EntitySelector() {
/*    */         public List<ServerPlayer> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/*    */           return level.getPlayers(selector);
/*    */         }
/*    */         
/*    */         public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB aabb, Predicate<? super T> selector) {
/* 63 */           return level.getEntities(type, aabb, selector);
/*    */         }
/*    */       };
/*    */     
/*    */     List<? extends Player> getPlayers(ServerLevel param1ServerLevel, Predicate<? super Player> param1Predicate);
/*    */     
/*    */     <T extends Entity> List<T> getEntities(ServerLevel param1ServerLevel, EntityTypeTest<Entity, T> param1EntityTypeTest, AABB param1AABB, Predicate<? super T> param1Predicate);
/*    */     
/*    */     static EntitySelector onlySelectPlayer(Player player) {
/* 68 */       return onlySelectPlayers(List.of(player));
/*    */     }
/*    */     
/*    */     static EntitySelector onlySelectPlayers(final List<Player> players) {
/* 72 */       return new EntitySelector() {
/*    */           public List<Player> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/* 75 */             return players.stream()
/* 76 */               .filter(selector)
/* 77 */               .toList();
/*    */           }
/*    */           
/*    */           public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 83 */             Objects.requireNonNull(type);
/* 83 */             return players.stream().map(type::tryCast)
/* 84 */               .filter(Objects::nonNull)
/* 85 */               .filter(selector)
/* 86 */               .toList();
/*    */           }
/*    */         };
/*    */     }
/*    */   }
/*    */   
/*    */   class null implements EntitySelector {
/*    */     public List<Player> getPlayers(ServerLevel level, Predicate<? super Player> selector) {
/*    */       return players.stream().filter(selector).toList();
/*    */     }
/*    */     
/*    */     public <T extends Entity> List<T> getEntities(ServerLevel level, EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/*    */       Objects.requireNonNull(type);
/* 86 */       return players.stream().map(type::tryCast).filter(Objects::nonNull).filter(selector).toList();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class PlayerSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 20 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 33 */     Objects.requireNonNull(body);
/* 34 */     List<Player> players = (List<Player>)level.players().stream().filter(EntitySelector.NO_SPECTATORS).filter(player -> body.closerThan((Entity)player, getFollowDistance(body))).sorted(Comparator.comparingDouble(body::distanceToSqr)).collect(Collectors.toList());
/* 36 */     Brain<?> brain = body.getBrain();
/* 37 */     brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, players);
/* 40 */     List<Player> visiblePlayers = (List<Player>)players.stream().filter(livingEntity -> isEntityTargetable(level, body, (LivingEntity)livingEntity)).collect(Collectors.toList());
/* 41 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, visiblePlayers.isEmpty() ? null : visiblePlayers.get(0));
/* 44 */     List<Player> visibleAttackablePlayers = visiblePlayers.stream().filter(livingEntity -> isEntityAttackable(level, body, (LivingEntity)livingEntity)).toList();
/* 45 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS, visibleAttackablePlayers);
/* 47 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, visibleAttackablePlayers.isEmpty() ? null : visibleAttackablePlayers.get(0));
/*    */   }
/*    */   
/*    */   protected double getFollowDistance(LivingEntity body) {
/* 52 */     return body.getAttributeValue(Attributes.FOLLOW_RANGE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\PlayerSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
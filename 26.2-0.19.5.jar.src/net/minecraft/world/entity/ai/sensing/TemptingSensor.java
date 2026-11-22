/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class TemptingSensor extends Sensor<PathfinderMob> {
/* 23 */   private static final TargetingConditions TEMPT_TARGETING = TargetingConditions.forNonCombat().ignoreLineOfSight();
/*    */   
/*    */   private final BiPredicate<PathfinderMob, ItemStack> temptations;
/*    */   
/*    */   public TemptingSensor(Predicate<ItemStack> tt) {
/* 28 */     this((m, i) -> tt.test(i));
/*    */   }
/*    */   
/*    */   public static TemptingSensor forAnimal() {
/* 32 */     return new TemptingSensor((m, i) -> {
/*    */           if (m instanceof Animal) {
/*    */             Animal animal = (Animal)m;
/*    */             return animal.isFood(i);
/*    */           } 
/*    */           return false;
/*    */         });
/*    */   }
/*    */   
/*    */   private TemptingSensor(BiPredicate<PathfinderMob, ItemStack> temptations) {
/* 42 */     this.temptations = temptations;
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, PathfinderMob body) {
/* 47 */     Brain<?> brain = body.getBrain();
/* 48 */     TargetingConditions targeting = TEMPT_TARGETING.copy().range((float)body.getAttributeValue(Attributes.TEMPT_RANGE));
/* 55 */     Objects.requireNonNull(body);
/* 56 */     List<Player> players = (List<Player>)level.players().stream().filter(EntitySelector.NO_SPECTATORS).filter(player -> targeting.test(level, (LivingEntity)body, (LivingEntity)player)).filter(p -> playerHoldingTemptation(body, (Player)p)).filter(player -> !body.hasPassenger((Entity)player)).sorted(Comparator.comparingDouble(body::distanceToSqr)).collect(Collectors.toList());
/* 58 */     if (!players.isEmpty()) {
/* 59 */       Player player = players.get(0);
/* 60 */       brain.setMemory(MemoryModuleType.TEMPTING_PLAYER, player);
/*    */     } else {
/* 62 */       brain.eraseMemory(MemoryModuleType.TEMPTING_PLAYER);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean playerHoldingTemptation(PathfinderMob mob, Player player) {
/* 67 */     return (isTemptation(mob, player.getMainHandItem()) || isTemptation(mob, player.getOffhandItem()));
/*    */   }
/*    */   
/*    */   private boolean isTemptation(PathfinderMob mob, ItemStack itemStack) {
/* 71 */     return this.temptations.test(mob, itemStack);
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 76 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.TEMPTING_PLAYER);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\TemptingSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
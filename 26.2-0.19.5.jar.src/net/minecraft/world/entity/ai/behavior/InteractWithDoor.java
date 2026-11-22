/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.OptionalBox;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class InteractWithDoor {
/*     */   private static final int COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE = 20;
/*     */   
/*     */   private static final double SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN = 3.0D;
/*     */   
/*     */   private static final double MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS = 2.0D;
/*     */   
/*     */   public static BehaviorControl<LivingEntity> create() {
/*  43 */     MutableObject<Node> lastCheckedNode = new MutableObject();
/*  44 */     MutableInt remainingCooldown = new MutableInt(0);
/*  46 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.PATH), (App)i.registered(MemoryModuleType.DOORS_TO_CLOSE), (App)i.registered(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel level, LivingEntity body, Node movingFromNode, Node movingToNode, Set<GlobalPos> doors, Optional<List<LivingEntity>> nearestEntities) {
/*  96 */     Iterator<GlobalPos> iterator = doors.iterator();
/*  97 */     while (iterator.hasNext()) {
/*  98 */       GlobalPos doorGlobalPos = iterator.next();
/*  99 */       BlockPos doorPos = doorGlobalPos.pos();
/* 102 */       if (movingFromNode != null && movingFromNode.asBlockPos().equals(doorPos))
/*     */         continue; 
/* 105 */       if (movingToNode != null && movingToNode.asBlockPos().equals(doorPos))
/*     */         continue; 
/* 109 */       if (isDoorTooFarAway(level, body, doorGlobalPos)) {
/* 110 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 113 */       BlockState state = level.getBlockState(doorPos);
/* 114 */       if (!state.is(BlockTags.MOB_INTERACTABLE_DOORS, s -> s.getBlock() instanceof DoorBlock)) {
/* 115 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 118 */       DoorBlock block = (DoorBlock)state.getBlock();
/* 119 */       if (!block.isOpen(state)) {
/* 120 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 123 */       if (areOtherMobsComingThroughDoor(body, doorPos, nearestEntities)) {
/* 124 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 127 */       block.setOpen((Entity)body, (Level)level, state, doorPos, false);
/* 128 */       iterator.remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areOtherMobsComingThroughDoor(LivingEntity body, BlockPos doorPos, Optional<List<LivingEntity>> nearestEntities) {
/* 133 */     if (nearestEntities.isEmpty())
/* 134 */       return false; 
/* 137 */     return ((List)nearestEntities.get()).stream()
/* 138 */       .filter(otherMob -> (otherMob.getType() == body.getType()))
/* 139 */       .filter(otherMob -> doorPos.closerToCenterThan((Position)otherMob.position(), 2.0D))
/* 140 */       .anyMatch(otherMob -> isMobComingThroughDoor(otherMob.getBrain(), doorPos));
/*     */   }
/*     */   
/*     */   private static boolean isMobComingThroughDoor(Brain<?> otherBrain, BlockPos doorPos) {
/* 144 */     if (!otherBrain.hasMemoryValue(MemoryModuleType.PATH))
/* 145 */       return false; 
/* 147 */     Path path = otherBrain.getMemory(MemoryModuleType.PATH).get();
/* 148 */     if (path.isDone())
/* 150 */       return false; 
/* 153 */     Node movingFromNode = path.getPreviousNode();
/* 154 */     if (movingFromNode == null)
/* 155 */       return false; 
/* 158 */     Node movingToNode = path.getNextNode();
/* 159 */     return (doorPos.equals(movingFromNode.asBlockPos()) || doorPos.equals(movingToNode.asBlockPos()));
/*     */   }
/*     */   
/*     */   private static boolean isDoorTooFarAway(ServerLevel level, LivingEntity body, GlobalPos doorGlobalPos) {
/* 163 */     return (doorGlobalPos.dimension() != level.dimension() || 
/* 164 */       !doorGlobalPos.pos().closerToCenterThan((Position)body.position(), 3.0D));
/*     */   }
/*     */   
/*     */   private static Optional<Set<GlobalPos>> rememberDoorToClose(MemoryAccessor<OptionalBox.Mu, Set<GlobalPos>> doorsMemory, Optional<Set<GlobalPos>> doors, ServerLevel level, BlockPos doorPos) {
/* 168 */     GlobalPos globalDoorPos = GlobalPos.of(level.dimension(), doorPos);
/* 170 */     return Optional.of(doors.<Set<GlobalPos>>map(set -> {
/*     */             set.add(globalDoorPos);
/*     */             return set;
/* 173 */           }).orElseGet(() -> {
/*     */             Set<GlobalPos> set = Sets.newHashSet((Object[])new GlobalPos[] { globalDoorPos });
/*     */             doorsMemory.set(set);
/*     */             return set;
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\InteractWithDoor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
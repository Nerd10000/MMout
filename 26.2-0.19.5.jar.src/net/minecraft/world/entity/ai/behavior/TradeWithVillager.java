/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class TradeWithVillager extends Behavior<Villager> {
/*  22 */   private Set<Item> trades = (Set<Item>)ImmutableSet.of();
/*     */   
/*     */   public TradeWithVillager() {
/*  25 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  33 */     return BehaviorUtils.targetIsValid(body.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityTypes.VILLAGER);
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/*  38 */     return checkExtraStartConditions(level, body);
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Villager myBody, long timestamp) {
/*  43 */     Villager target = myBody.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*  44 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)myBody, (LivingEntity)target, 0.5F, 2);
/*  46 */     this.trades = figureOutWhatIAmWillingToTrade(myBody, target);
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/*  51 */     Villager target = body.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*  53 */     if (body.distanceToSqr((Entity)target) > 5.0D)
/*     */       return; 
/*  57 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)target, 0.5F, 2);
/*  59 */     body.gossip(level, target, timestamp);
/*  61 */     boolean isFarmer = body.getVillagerData().profession().is(VillagerProfession.FARMER);
/*  62 */     if (body.hasExcessFood() && (isFarmer || target.wantsMoreFood()))
/*  63 */       throwHalfStack(body, Villager.FOOD_POINTS.keySet(), (LivingEntity)target); 
/*  66 */     if (isFarmer && body.getInventory().countItem(Items.WHEAT) > Items.WHEAT.getDefaultMaxStackSize() / 2)
/*  67 */       throwHalfStack(body, (Set<Item>)ImmutableSet.of(Items.WHEAT), (LivingEntity)target); 
/*  70 */     if (!this.trades.isEmpty() && body.getInventory().hasAnyOf(this.trades))
/*  71 */       throwHalfStack(body, this.trades, (LivingEntity)target); 
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/*  77 */     body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*     */   }
/*     */   
/*     */   private static Set<Item> figureOutWhatIAmWillingToTrade(Villager myBody, Villager target) {
/*  83 */     ImmutableSet<Item> targetItems = ((VillagerProfession)target.getVillagerData().profession().value()).requestedItems();
/*  84 */     ImmutableSet<Item> selfItems = ((VillagerProfession)myBody.getVillagerData().profession().value()).requestedItems();
/*  85 */     return (Set<Item>)targetItems.stream().filter(entry -> !selfItems.contains(entry)).collect(Collectors.toSet());
/*     */   }
/*     */   
/*     */   private static void throwHalfStack(Villager villager, Set<Item> items, LivingEntity target) {
/*  90 */     SimpleContainer inventory = villager.getInventory();
/*  92 */     ItemStack toThrow = ItemStack.EMPTY;
/*  93 */     for (int i = 0; i < inventory.getContainerSize(); i++) {
/*  94 */       ItemStack itemStack = inventory.getItem(i);
/*  95 */       if (!itemStack.isEmpty()) {
/*  96 */         Item item = itemStack.getItem();
/*  97 */         if (items.contains(item)) {
/*     */           int count;
/*  99 */           if (itemStack.getCount() > itemStack.getMaxStackSize() / 2) {
/* 100 */             count = itemStack.getCount() / 2;
/* 101 */           } else if (itemStack.getCount() > 24) {
/* 102 */             count = itemStack.getCount() - 24;
/*     */           } else {
/*     */             continue;
/*     */           } 
/* 106 */           itemStack.shrink(count);
/* 107 */           toThrow = new ItemStack((ItemLike)item, count);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       continue;
/*     */     } 
/* 113 */     if (!toThrow.isEmpty())
/* 114 */       BehaviorUtils.throwItem((LivingEntity)villager, toThrow, target.position()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\TradeWithVillager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package net.minecraft.world.entity.vehicle.boat;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.ContainerEntity;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractChestBoat extends AbstractBoat implements HasCustomInventoryScreen, ContainerEntity {
/*     */   private static final int CONTAINER_SIZE = 27;
/*     */   
/*  35 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
/*     */   
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   
/*     */   private long lootTableSeed;
/*     */   
/*     */   public AbstractChestBoat(EntityType<? extends AbstractChestBoat> type, Level level, Supplier<Item> dropItem) {
/*  40 */     super((EntityType)type, level, dropItem);
/*     */   }
/*     */   
/*     */   protected float getSinglePassengerXOffset() {
/*  46 */     return 0.15F;
/*     */   }
/*     */   
/*     */   protected int getMaxPassengers() {
/*  51 */     return 1;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  56 */     super.addAdditionalSaveData(output);
/*  57 */     addChestVehicleSaveData(output);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  62 */     super.readAdditionalSaveData(input);
/*  63 */     readChestVehicleSaveData(input);
/*     */   }
/*     */   
/*     */   public void destroy(ServerLevel level, DamageSource source) {
/*  68 */     destroy(level, getDropItem());
/*  69 */     chestVehicleDestroyed(source, level, (Entity)this);
/*     */   }
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/*  74 */     if (!level().isClientSide() && reason.shouldDestroy())
/*  75 */       Containers.dropContents(level(), (Entity)this, (Container)this); 
/*  77 */     super.remove(reason);
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/*  82 */     InteractionResult superInteraction = super.interact(player, hand, location);
/*  83 */     if (superInteraction != InteractionResult.PASS)
/*  84 */       return superInteraction; 
/*  86 */     if (!canAddPassenger((Entity)player) || player.isSecondaryUseActive()) {
/*  87 */       InteractionResult result = interactWithContainerVehicle(player);
/*  88 */       if (result.consumesAction()) {
/*  88 */         Level level = player.level();
/*  88 */         if (level instanceof ServerLevel) {
/*  88 */           ServerLevel serverLevel = (ServerLevel)level;
/*  89 */           gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)player);
/*  90 */           PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*     */         } 
/*     */       } 
/*  92 */       return result;
/*     */     } 
/*  94 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public void openCustomInventoryScreen(Player player) {
/*  99 */     player.openMenu((MenuProvider)this);
/* 100 */     Level level = player.level();
/* 100 */     if (level instanceof ServerLevel) {
/* 100 */       ServerLevel serverLevel = (ServerLevel)level;
/* 101 */       gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)player);
/* 102 */       PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearContent() {
/* 108 */     clearChestVehicleContent();
/*     */   }
/*     */   
/*     */   public int getContainerSize() {
/* 113 */     return 27;
/*     */   }
/*     */   
/*     */   public ItemStack getItem(int slot) {
/* 118 */     return getChestVehicleItem(slot);
/*     */   }
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/* 123 */     return removeChestVehicleItem(slot, count);
/*     */   }
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/* 128 */     return removeChestVehicleItemNoUpdate(slot);
/*     */   }
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 133 */     setChestVehicleItem(slot, itemStack);
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 138 */     return getChestVehicleSlot(slot);
/*     */   }
/*     */   
/*     */   public void setChanged() {}
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 147 */     return isChestVehicleStillValid(player);
/*     */   }
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 152 */     if (this.lootTable == null || !player.isSpectator()) {
/* 153 */       unpackLootTable(inventory.player);
/* 154 */       return (AbstractContainerMenu)ChestMenu.threeRows(containerId, inventory, (Container)this);
/*     */     } 
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   public void unpackLootTable(Player player) {
/* 160 */     unpackChestVehicleLootTable(player);
/*     */   }
/*     */   
/*     */   public ResourceKey<LootTable> getContainerLootTable() {
/* 165 */     return this.lootTable;
/*     */   }
/*     */   
/*     */   public void setContainerLootTable(ResourceKey<LootTable> lootTable) {
/* 170 */     this.lootTable = lootTable;
/*     */   }
/*     */   
/*     */   public long getContainerLootTableSeed() {
/* 175 */     return this.lootTableSeed;
/*     */   }
/*     */   
/*     */   public void setContainerLootTableSeed(long lootTableSeed) {
/* 180 */     this.lootTableSeed = lootTableSeed;
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getItemStacks() {
/* 185 */     return this.itemStacks;
/*     */   }
/*     */   
/*     */   public void clearItemStacks() {
/* 190 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public void stopOpen(ContainerUser containerUser) {
/* 195 */     level().gameEvent((Holder)GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of((Entity)containerUser.getLivingEntity()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\boat\AbstractChestBoat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
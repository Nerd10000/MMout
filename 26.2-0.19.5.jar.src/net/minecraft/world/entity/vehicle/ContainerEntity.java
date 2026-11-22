/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public interface ContainerEntity extends Container, MenuProvider {
/*     */   Vec3 position();
/*     */   
/*     */   AABB getBoundingBox();
/*     */   
/*     */   ResourceKey<LootTable> getContainerLootTable();
/*     */   
/*     */   void setContainerLootTable(ResourceKey<LootTable> paramResourceKey);
/*     */   
/*     */   long getContainerLootTableSeed();
/*     */   
/*     */   void setContainerLootTableSeed(long paramLong);
/*     */   
/*     */   NonNullList<ItemStack> getItemStacks();
/*     */   
/*     */   void clearItemStacks();
/*     */   
/*     */   Level level();
/*     */   
/*     */   boolean isRemoved();
/*     */   
/*     */   default boolean isEmpty() {
/*  59 */     return isChestVehicleEmpty();
/*     */   }
/*     */   
/*     */   default void addChestVehicleSaveData(ValueOutput output) {
/*  63 */     if (getContainerLootTable() != null) {
/*  64 */       output.putString("LootTable", getContainerLootTable().identifier().toString());
/*  65 */       if (getContainerLootTableSeed() != 0L)
/*  66 */         output.putLong("LootTableSeed", getContainerLootTableSeed()); 
/*     */     } else {
/*  69 */       ContainerHelper.saveAllItems(output, getItemStacks());
/*     */     } 
/*     */   }
/*     */   
/*     */   default void readChestVehicleSaveData(ValueInput input) {
/*  74 */     clearItemStacks();
/*  76 */     ResourceKey<LootTable> lootTable = input.read("LootTable", LootTable.KEY_CODEC).orElse(null);
/*  77 */     setContainerLootTable(lootTable);
/*  78 */     setContainerLootTableSeed(input.getLongOr("LootTableSeed", 0L));
/*  79 */     if (lootTable == null)
/*  80 */       ContainerHelper.loadAllItems(input, getItemStacks()); 
/*     */   }
/*     */   
/*     */   default void chestVehicleDestroyed(DamageSource source, ServerLevel level, Entity entity) {
/*  85 */     if (!((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue())
/*     */       return; 
/*  89 */     Containers.dropContents((Level)level, entity, this);
/*  91 */     Entity directEntity = source.getDirectEntity();
/*  92 */     if (directEntity instanceof Player) {
/*  92 */       Player player = (Player)directEntity;
/*  93 */       PiglinAi.angerNearbyPiglins(level, player, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   default InteractionResult interactWithContainerVehicle(Player player) {
/*  98 */     player.openMenu(this);
/*  99 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   default void unpackChestVehicleLootTable(Player player) {
/* 103 */     MinecraftServer server = level().getServer();
/* 104 */     if (getContainerLootTable() != null && server != null) {
/* 105 */       LootTable lootTable = server.reloadableRegistries().getLootTable(getContainerLootTable());
/* 106 */       if (player != null)
/* 107 */         CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)player, getContainerLootTable()); 
/* 109 */       setContainerLootTable((ResourceKey<LootTable>)null);
/* 112 */       LootParams.Builder builder = (new LootParams.Builder((ServerLevel)level())).withParameter(LootContextParams.ORIGIN, position());
/* 114 */       if (player != null)
/* 115 */         builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player); 
/* 117 */       lootTable.fill(this, builder.create(LootContextParamSets.CHEST), getContainerLootTableSeed());
/*     */     } 
/*     */   }
/*     */   
/*     */   default void clearChestVehicleContent() {
/* 122 */     unpackChestVehicleLootTable((Player)null);
/* 123 */     getItemStacks().clear();
/*     */   }
/*     */   
/*     */   default boolean isChestVehicleEmpty() {
/* 127 */     for (ItemStack itemStack : getItemStacks()) {
/* 128 */       if (!itemStack.isEmpty())
/* 129 */         return false; 
/*     */     } 
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   default ItemStack removeChestVehicleItemNoUpdate(int slot) {
/* 136 */     unpackChestVehicleLootTable((Player)null);
/* 137 */     ItemStack itemStack = (ItemStack)getItemStacks().get(slot);
/* 138 */     if (itemStack.isEmpty())
/* 139 */       return ItemStack.EMPTY; 
/* 141 */     getItemStacks().set(slot, ItemStack.EMPTY);
/* 142 */     return itemStack;
/*     */   }
/*     */   
/*     */   default ItemStack getChestVehicleItem(int slot) {
/* 146 */     unpackChestVehicleLootTable((Player)null);
/* 147 */     return (ItemStack)getItemStacks().get(slot);
/*     */   }
/*     */   
/*     */   default ItemStack removeChestVehicleItem(int slot, int count) {
/* 151 */     unpackChestVehicleLootTable((Player)null);
/* 152 */     return ContainerHelper.removeItem((List)getItemStacks(), slot, count);
/*     */   }
/*     */   
/*     */   default void setChestVehicleItem(int slot, ItemStack itemStack) {
/* 156 */     unpackChestVehicleLootTable((Player)null);
/* 157 */     getItemStacks().set(slot, itemStack);
/* 158 */     itemStack.limitSize(getMaxStackSize(itemStack));
/*     */   }
/*     */   
/*     */   default SlotAccess getChestVehicleSlot(final int slot) {
/* 162 */     if (slot >= 0 && slot < getContainerSize())
/* 163 */       return new SlotAccess() {
/*     */           public ItemStack get() {
/* 166 */             return ContainerEntity.this.getChestVehicleItem(slot);
/*     */           }
/*     */           
/*     */           public boolean set(ItemStack itemStack) {
/* 171 */             ContainerEntity.this.setChestVehicleItem(slot, itemStack);
/* 172 */             return true;
/*     */           }
/*     */         }; 
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   default boolean isChestVehicleStillValid(Player player) {
/* 181 */     return (!isRemoved() && player.isWithinEntityInteractionRange(getBoundingBox(), 4.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\ContainerEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
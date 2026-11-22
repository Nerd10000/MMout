/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.SeededContainerLoot;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class RandomizableContainerBlockEntity extends BaseContainerBlockEntity implements RandomizableContainer {
/*     */   protected ResourceKey<LootTable> lootTable;
/*     */   
/*  22 */   protected long lootTableSeed = 0L;
/*     */   
/*     */   protected RandomizableContainerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
/*  25 */     super(type, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public ResourceKey<LootTable> getLootTable() {
/*  30 */     return this.lootTable;
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> lootTable) {
/*  35 */     this.lootTable = lootTable;
/*     */   }
/*     */   
/*     */   public long getLootTableSeed() {
/*  40 */     return this.lootTableSeed;
/*     */   }
/*     */   
/*     */   public void setLootTableSeed(long lootTableSeed) {
/*  45 */     this.lootTableSeed = lootTableSeed;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  50 */     unpackLootTable(null);
/*  51 */     return super.isEmpty();
/*     */   }
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  56 */     unpackLootTable(null);
/*  57 */     return super.getItem(slot);
/*     */   }
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  62 */     unpackLootTable(null);
/*  63 */     return super.removeItem(slot, count);
/*     */   }
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  68 */     unpackLootTable(null);
/*  69 */     return super.removeItemNoUpdate(slot);
/*     */   }
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  74 */     unpackLootTable(null);
/*  75 */     super.setItem(slot, itemStack);
/*     */   }
/*     */   
/*     */   public boolean canOpen(Player player) {
/*  80 */     return ((this.lootTable == null || !player.isSpectator()) && super.canOpen(player));
/*     */   }
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/*  85 */     if (canOpen(player)) {
/*  86 */       unpackLootTable(inventory.player);
/*  87 */       return createMenu(containerId, inventory);
/*     */     } 
/*  89 */     if (!player.isSpectator())
/*  90 */       BaseContainerBlockEntity.sendChestLockedNotifications(Vec3.atCenterOf((Vec3i)getBlockPos()), player, getDisplayName()); 
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/*  97 */     super.applyImplicitComponents(components);
/*  98 */     SeededContainerLoot loot = (SeededContainerLoot)components.get(DataComponents.CONTAINER_LOOT);
/*  99 */     if (loot != null) {
/* 100 */       this.lootTable = loot.lootTable();
/* 101 */       this.lootTableSeed = loot.seed();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 107 */     super.collectImplicitComponents(components);
/* 108 */     if (this.lootTable != null)
/* 109 */       components.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed)); 
/*     */   }
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 115 */     super.removeComponentsFromTag(output);
/* 116 */     output.discard("LootTable");
/* 117 */     output.discard("LootTableSeed");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\RandomizableContainerBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
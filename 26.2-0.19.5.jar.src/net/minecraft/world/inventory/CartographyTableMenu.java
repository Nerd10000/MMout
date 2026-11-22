/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.item.component.MapPostProcessing;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ public class CartographyTableMenu extends AbstractContainerMenu {
/*     */   public static final int MAP_SLOT = 0;
/*     */   
/*     */   public static final int ADDITIONAL_SLOT = 1;
/*     */   
/*     */   public static final int RESULT_SLOT = 2;
/*     */   
/*     */   private static final int INV_SLOT_START = 3;
/*     */   
/*     */   private static final int INV_SLOT_END = 30;
/*     */   
/*     */   private static final int USE_ROW_SLOT_START = 30;
/*     */   
/*     */   private static final int USE_ROW_SLOT_END = 39;
/*     */   
/*     */   private final ContainerLevelAccess access;
/*     */   
/*     */   private long lastSoundTime;
/*     */   
/*  29 */   public final Container container = (Container)new SimpleContainer(2) {
/*     */       public void setChanged() {
/*  32 */         CartographyTableMenu.this.slotsChanged((Container)this);
/*  33 */         super.setChanged();
/*     */       }
/*     */     };
/*     */   
/*  36 */   private final ResultContainer resultContainer = new ResultContainer() {
/*     */       public void setChanged() {
/*  40 */         CartographyTableMenu.this.slotsChanged(this);
/*  41 */         super.setChanged();
/*     */       }
/*     */     };
/*     */   
/*     */   public CartographyTableMenu(int containerId, Inventory inventory) {
/*  46 */     this(containerId, inventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public CartographyTableMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
/*  50 */     super(MenuType.CARTOGRAPHY_TABLE, containerId);
/*  52 */     this.access = access;
/*  54 */     addSlot(new Slot(this, this.container, 0, 15, 15) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  57 */             return itemStack.has(DataComponents.MAP_ID);
/*     */           }
/*     */         });
/*  61 */     addSlot(new Slot(this, this.container, 1, 15, 52) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  64 */             return (itemStack.is(Items.PAPER) || itemStack.is(Items.MAP) || itemStack.is(Items.GLASS_PANE));
/*     */           }
/*     */         });
/*  68 */     addSlot(new Slot(this.resultContainer, 2, 145, 39) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  71 */             return false;
/*     */           }
/*     */           
/*     */           public void onTake(Player player, ItemStack carried) {
/*  76 */             ((Slot)CartographyTableMenu.this.slots.get(0)).remove(1);
/*  77 */             ((Slot)CartographyTableMenu.this.slots.get(1)).remove(1);
/*  79 */             carried.getItem().onCraftedBy(carried, player);
/*  81 */             access.execute((level, pos) -> {
/*     */                   long gameTime = level.getGameTime();
/*     */                   if (CartographyTableMenu.this.lastSoundTime != gameTime) {
/*     */                     level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */                     CartographyTableMenu.this.lastSoundTime = gameTime;
/*     */                   } 
/*     */                 });
/*  90 */             super.onTake(player, carried);
/*     */           }
/*     */           
/*     */           public ItemStack safeClone(Player player) {
/*  95 */             ItemStack result = super.safeClone(player);
/*  96 */             result.getItem().onCraftedBy(result, player);
/*  97 */             return result;
/*     */           }
/*     */         });
/* 101 */     addStandardInventorySlots((Container)inventory, 8, 84);
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 106 */     return stillValid(this.access, player, Blocks.CARTOGRAPHY_TABLE);
/*     */   }
/*     */   
/*     */   public void slotsChanged(Container container) {
/* 111 */     ItemStack mapStack = this.container.getItem(0);
/* 112 */     ItemStack additionalStack = this.container.getItem(1);
/* 113 */     ItemStack resultStack = this.resultContainer.getItem(2);
/* 115 */     if (!resultStack.isEmpty() && (mapStack.isEmpty() || additionalStack.isEmpty())) {
/* 116 */       this.resultContainer.removeItemNoUpdate(2);
/* 117 */     } else if (!mapStack.isEmpty() && !additionalStack.isEmpty()) {
/* 118 */       setupResultSlot(mapStack, additionalStack, resultStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupResultSlot(ItemStack mapStack, ItemStack additionalStack, ItemStack resultStack) {
/* 123 */     this.access.execute((level, pos) -> {
/*     */           ItemStack result;
/*     */           MapItemSavedData mapData = MapItem.getSavedData(mapStack, level);
/*     */           if (mapData == null)
/*     */             return; 
/*     */           if (additionalStack.is(Items.PAPER) && !mapData.locked && mapData.scale < 4) {
/*     */             result = mapStack.copyWithCount(1);
/*     */             result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.SCALE);
/*     */             broadcastChanges();
/*     */           } else if (additionalStack.is(Items.GLASS_PANE) && !mapData.locked) {
/*     */             result = mapStack.copyWithCount(1);
/*     */             result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.LOCK);
/*     */             broadcastChanges();
/*     */           } else if (additionalStack.is(Items.MAP)) {
/*     */             result = mapStack.copyWithCount(2);
/*     */             broadcastChanges();
/*     */           } else {
/*     */             this.resultContainer.removeItemNoUpdate(2);
/*     */             broadcastChanges();
/*     */             return;
/*     */           } 
/*     */           if (!ItemStack.matches(result, resultStack)) {
/*     */             this.resultContainer.setItem(2, result);
/*     */             broadcastChanges();
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
/* 157 */     return (target.container != this.resultContainer && super.canTakeItemForPickAll(carried, target));
/*     */   }
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 162 */     ItemStack clicked = ItemStack.EMPTY;
/* 163 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 164 */     if (slot != null && slot.hasItem()) {
/* 165 */       ItemStack stack = slot.getItem();
/* 166 */       clicked = stack.copy();
/* 168 */       if (slotIndex == 2) {
/* 169 */         stack.getItem().onCraftedBy(stack, player);
/* 170 */         if (!moveItemStackTo(stack, 3, 39, true))
/* 171 */           return ItemStack.EMPTY; 
/* 173 */         slot.onQuickCraft(stack, clicked);
/* 174 */       } else if (slotIndex == 1 || slotIndex == 0) {
/* 175 */         if (!moveItemStackTo(stack, 3, 39, false))
/* 176 */           return ItemStack.EMPTY; 
/* 178 */       } else if (stack.has(DataComponents.MAP_ID)) {
/* 179 */         if (!moveItemStackTo(stack, 0, 1, false))
/* 180 */           return ItemStack.EMPTY; 
/* 182 */       } else if (stack.is(Items.PAPER) || stack.is(Items.MAP) || stack.is(Items.GLASS_PANE)) {
/* 183 */         if (!moveItemStackTo(stack, 1, 2, false))
/* 184 */           return ItemStack.EMPTY; 
/* 186 */       } else if (slotIndex >= 3 && slotIndex < 30) {
/* 187 */         if (!moveItemStackTo(stack, 30, 39, false))
/* 188 */           return ItemStack.EMPTY; 
/* 190 */       } else if (slotIndex >= 30 && slotIndex < 39 && 
/* 191 */         !moveItemStackTo(stack, 3, 30, false)) {
/* 192 */         return ItemStack.EMPTY;
/*     */       } 
/* 196 */       if (stack.isEmpty())
/* 197 */         slot.setByPlayer(ItemStack.EMPTY); 
/* 200 */       slot.setChanged();
/* 202 */       if (stack.getCount() == clicked.getCount())
/* 203 */         return ItemStack.EMPTY; 
/* 205 */       slot.onTake(player, stack);
/* 206 */       broadcastChanges();
/*     */     } 
/* 209 */     return clicked;
/*     */   }
/*     */   
/*     */   public void removed(Player player) {
/* 214 */     super.removed(player);
/* 216 */     this.resultContainer.removeItemNoUpdate(2);
/* 217 */     this.access.execute((level, pos) -> clearContainer(player, this.container));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\CartographyTableMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
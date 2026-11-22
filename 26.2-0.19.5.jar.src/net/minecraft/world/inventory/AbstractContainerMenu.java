/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.HashedStack;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.BundleItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class AbstractContainerMenu {
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int SLOT_CLICKED_OUTSIDE = -999;
/*     */   
/*     */   public static final int QUICKCRAFT_TYPE_CHARITABLE = 0;
/*     */   
/*     */   public static final int QUICKCRAFT_TYPE_GREEDY = 1;
/*     */   
/*     */   public static final int QUICKCRAFT_TYPE_CLONE = 2;
/*     */   
/*     */   public static final int QUICKCRAFT_HEADER_START = 0;
/*     */   
/*     */   public static final int QUICKCRAFT_HEADER_CONTINUE = 1;
/*     */   
/*     */   public static final int QUICKCRAFT_HEADER_END = 2;
/*     */   
/*     */   public static final int CARRIED_SLOT_SIZE = 2147483647;
/*     */   
/*     */   public static final int SLOTS_PER_ROW = 9;
/*     */   
/*     */   public static final int SLOT_SIZE = 18;
/*     */   
/*  57 */   private final NonNullList<ItemStack> lastSlots = NonNullList.create();
/*     */   
/*  58 */   public final NonNullList<Slot> slots = NonNullList.create();
/*     */   
/*  59 */   private final List<DataSlot> dataSlots = Lists.newArrayList();
/*     */   
/*  60 */   private ItemStack carried = ItemStack.EMPTY;
/*     */   
/*  63 */   private final NonNullList<RemoteSlot> remoteSlots = NonNullList.create();
/*     */   
/*  64 */   private final IntList remoteDataSlots = (IntList)new IntArrayList();
/*     */   
/*  65 */   private RemoteSlot remoteCarried = RemoteSlot.PLACEHOLDER;
/*     */   
/*     */   private int stateId;
/*     */   
/*     */   private final MenuType<?> menuType;
/*     */   
/*     */   public final int containerId;
/*     */   
/*  72 */   private int quickcraftType = -1;
/*     */   
/*     */   private int quickcraftStatus;
/*     */   
/*  74 */   private final Set<Slot> quickcraftSlots = Sets.newHashSet();
/*     */   
/*  76 */   private final List<ContainerListener> containerListeners = Lists.newArrayList();
/*     */   
/*     */   private ContainerSynchronizer synchronizer;
/*     */   
/*     */   private boolean suppressRemoteUpdates;
/*     */   
/*     */   protected AbstractContainerMenu(MenuType<?> menuType, int containerId) {
/*  81 */     this.menuType = menuType;
/*  82 */     this.containerId = containerId;
/*     */   }
/*     */   
/*     */   protected void addInventoryHotbarSlots(Container inventory, int left, int top) {
/*  86 */     for (int x = 0; x < 9; x++)
/*  87 */       addSlot(new Slot(inventory, x, left + x * 18, top)); 
/*     */   }
/*     */   
/*     */   protected void addInventoryExtendedSlots(Container inventory, int left, int top) {
/*  92 */     for (int y = 0; y < 3; y++) {
/*  93 */       for (int x = 0; x < 9; x++)
/*  94 */         addSlot(new Slot(inventory, x + (y + 1) * 9, left + x * 18, top + y * 18)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addStandardInventorySlots(Container container, int left, int top) {
/* 100 */     addInventoryExtendedSlots(container, left, top);
/* 101 */     int hotbarSeparator = 4;
/* 102 */     int topToHotbar = 58;
/* 103 */     addInventoryHotbarSlots(container, left, top + 58);
/*     */   }
/*     */   
/*     */   protected static boolean stillValid(ContainerLevelAccess access, Player player, Block block) {
/* 107 */     return ((Boolean)access.<Boolean>evaluate((level, pos) -> !level.getBlockState(pos).is(block) ? Boolean.valueOf(false) : Boolean.valueOf(player.isWithinBlockInteractionRange(pos, 4.0D)), 
/*     */         
/* 112 */         Boolean.valueOf(true))).booleanValue();
/*     */   }
/*     */   
/*     */   public MenuType<?> getType() {
/* 116 */     if (this.menuType == null)
/* 117 */       throw new UnsupportedOperationException("Unable to construct this menu by type"); 
/* 119 */     return this.menuType;
/*     */   }
/*     */   
/*     */   protected static void checkContainerSize(Container container, int expected) {
/* 123 */     int actual = container.getContainerSize();
/* 124 */     if (actual < expected)
/* 125 */       throw new IllegalArgumentException("Container size " + actual + " is smaller than expected " + expected); 
/*     */   }
/*     */   
/*     */   protected static void checkContainerDataCount(ContainerData data, int expected) {
/* 130 */     int actual = data.getCount();
/* 131 */     if (actual < expected)
/* 132 */       throw new IllegalArgumentException("Container data count " + actual + " is smaller than expected " + expected); 
/*     */   }
/*     */   
/*     */   public boolean isValidSlotIndex(int slotIndex) {
/* 137 */     return (slotIndex == -1 || slotIndex == -999 || slotIndex < this.slots.size());
/*     */   }
/*     */   
/*     */   protected Slot addSlot(Slot slot) {
/* 141 */     slot.index = this.slots.size();
/* 142 */     this.slots.add(slot);
/* 143 */     this.lastSlots.add(ItemStack.EMPTY);
/* 144 */     this.remoteSlots.add((this.synchronizer != null) ? this.synchronizer.createSlot() : RemoteSlot.PLACEHOLDER);
/* 145 */     return slot;
/*     */   }
/*     */   
/*     */   protected DataSlot addDataSlot(DataSlot dataSlot) {
/* 149 */     this.dataSlots.add(dataSlot);
/* 150 */     this.remoteDataSlots.add(0);
/* 151 */     return dataSlot;
/*     */   }
/*     */   
/*     */   protected void addDataSlots(ContainerData container) {
/* 155 */     for (int i = 0; i < container.getCount(); i++)
/* 156 */       addDataSlot(DataSlot.forContainer(container, i)); 
/*     */   }
/*     */   
/*     */   public void addSlotListener(ContainerListener listener) {
/* 161 */     if (this.containerListeners.contains(listener))
/*     */       return; 
/* 164 */     this.containerListeners.add(listener);
/* 165 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   public void setSynchronizer(ContainerSynchronizer synchronizer) {
/* 169 */     this.synchronizer = synchronizer;
/* 171 */     this.remoteCarried = synchronizer.createSlot();
/* 172 */     this.remoteSlots.replaceAll(ignored -> synchronizer.createSlot());
/* 173 */     sendAllDataToRemote();
/*     */   }
/*     */   
/*     */   public void sendAllDataToRemote() {
/* 177 */     List<ItemStack> itemsToSend = new ArrayList<>(this.slots.size());
/* 178 */     for (int i = 0, slotsSize = this.slots.size(); i < slotsSize; i++) {
/* 179 */       ItemStack slotContents = ((Slot)this.slots.get(i)).getItem();
/* 180 */       itemsToSend.add(slotContents.copy());
/* 181 */       ((RemoteSlot)this.remoteSlots.get(i)).force(slotContents);
/*     */     } 
/* 184 */     ItemStack carried = getCarried();
/* 185 */     this.remoteCarried.force(carried);
/* 187 */     for (int j = 0, k = this.dataSlots.size(); j < k; j++)
/* 188 */       this.remoteDataSlots.set(j, ((DataSlot)this.dataSlots.get(j)).get()); 
/* 191 */     if (this.synchronizer != null)
/* 192 */       this.synchronizer.sendInitialData(this, itemsToSend, carried.copy(), this.remoteDataSlots.toIntArray()); 
/*     */   }
/*     */   
/*     */   public void removeSlotListener(ContainerListener listener) {
/* 197 */     this.containerListeners.remove(listener);
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 201 */     NonNullList<ItemStack> itemStacks = NonNullList.create();
/* 202 */     for (Slot slot : this.slots)
/* 203 */       itemStacks.add(slot.getItem()); 
/* 205 */     return itemStacks;
/*     */   }
/*     */   
/*     */   public void broadcastChanges() {
/*     */     int i;
/* 209 */     for (i = 0; i < this.slots.size(); i++) {
/* 210 */       ItemStack current = ((Slot)this.slots.get(i)).getItem();
/* 211 */       Objects.requireNonNull(current);
/* 211 */       Supplier supplier = Suppliers.memoize(current::copy);
/* 212 */       triggerSlotListeners(i, current, (Supplier<ItemStack>)supplier);
/* 213 */       synchronizeSlotToRemote(i, current, (Supplier<ItemStack>)supplier);
/*     */     } 
/* 216 */     synchronizeCarriedToRemote();
/* 218 */     for (i = 0; i < this.dataSlots.size(); i++) {
/* 219 */       DataSlot current = this.dataSlots.get(i);
/* 220 */       int currentValue = current.get();
/* 221 */       if (current.checkAndClearUpdateFlag())
/* 222 */         updateDataSlotListeners(i, currentValue); 
/* 224 */       synchronizeDataSlotToRemote(i, currentValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastFullState() {
/*     */     int i;
/* 229 */     for (i = 0; i < this.slots.size(); i++) {
/* 230 */       ItemStack current = ((Slot)this.slots.get(i)).getItem();
/* 231 */       Objects.requireNonNull(current);
/* 231 */       triggerSlotListeners(i, current, current::copy);
/*     */     } 
/* 234 */     for (i = 0; i < this.dataSlots.size(); i++) {
/* 235 */       DataSlot current = this.dataSlots.get(i);
/* 236 */       if (current.checkAndClearUpdateFlag())
/* 237 */         updateDataSlotListeners(i, current.get()); 
/*     */     } 
/* 240 */     sendAllDataToRemote();
/*     */   }
/*     */   
/*     */   private void updateDataSlotListeners(int id, int currentValue) {
/* 244 */     for (ContainerListener containerListener : this.containerListeners)
/* 245 */       containerListener.dataChanged(this, id, currentValue); 
/*     */   }
/*     */   
/*     */   private void triggerSlotListeners(int i, ItemStack current, Supplier<ItemStack> currentCopy) {
/* 250 */     ItemStack localExpected = (ItemStack)this.lastSlots.get(i);
/* 251 */     if (!ItemStack.matches(localExpected, current)) {
/* 252 */       ItemStack newItem = currentCopy.get();
/* 253 */       this.lastSlots.set(i, newItem);
/* 254 */       for (ContainerListener containerListener : this.containerListeners)
/* 255 */         containerListener.slotChanged(this, i, newItem); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void synchronizeSlotToRemote(int i, ItemStack current, Supplier<ItemStack> currentCopy) {
/* 261 */     if (this.suppressRemoteUpdates)
/*     */       return; 
/* 265 */     RemoteSlot remoteExpected = (RemoteSlot)this.remoteSlots.get(i);
/* 266 */     if (!remoteExpected.matches(current)) {
/* 267 */       remoteExpected.force(current);
/* 268 */       if (this.synchronizer != null)
/* 269 */         this.synchronizer.sendSlotChange(this, i, currentCopy.get()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void synchronizeDataSlotToRemote(int i, int current) {
/* 275 */     if (this.suppressRemoteUpdates)
/*     */       return; 
/* 279 */     int remoteExpected = this.remoteDataSlots.getInt(i);
/* 280 */     if (remoteExpected != current) {
/* 281 */       this.remoteDataSlots.set(i, current);
/* 282 */       if (this.synchronizer != null)
/* 283 */         this.synchronizer.sendDataChange(this, i, current); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void synchronizeCarriedToRemote() {
/* 289 */     if (this.suppressRemoteUpdates)
/*     */       return; 
/* 293 */     ItemStack carriedItem = getCarried();
/* 294 */     if (!this.remoteCarried.matches(carriedItem)) {
/* 295 */       this.remoteCarried.force(carriedItem);
/* 296 */       if (this.synchronizer != null)
/* 297 */         this.synchronizer.sendCarriedChange(this, carriedItem.copy()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRemoteSlot(int slot, ItemStack itemStack) {
/* 303 */     ((RemoteSlot)this.remoteSlots.get(slot)).force(itemStack);
/*     */   }
/*     */   
/*     */   public void setRemoteSlotUnsafe(int slot, HashedStack itemStack) {
/* 308 */     if (slot < 0 || slot >= this.remoteSlots.size()) {
/* 309 */       LOGGER.debug("Incorrect slot index: {} available slots: {}", Integer.valueOf(slot), Integer.valueOf(this.remoteSlots.size()));
/*     */       return;
/*     */     } 
/* 312 */     ((RemoteSlot)this.remoteSlots.get(slot)).receive(itemStack);
/*     */   }
/*     */   
/*     */   public void setRemoteCarried(HashedStack carriedItem) {
/* 316 */     this.remoteCarried.receive(carriedItem);
/*     */   }
/*     */   
/*     */   public boolean clickMenuButton(Player player, int buttonId) {
/* 320 */     return false;
/*     */   }
/*     */   
/*     */   public Slot getSlot(int index) {
/* 324 */     return (Slot)this.slots.get(index);
/*     */   }
/*     */   
/*     */   public void setSelectedBundleItemIndex(int slotIndex, int selectedItemIndex) {
/* 330 */     if (slotIndex >= 0 && slotIndex < this.slots.size()) {
/* 331 */       ItemStack itemStack = ((Slot)this.slots.get(slotIndex)).getItem();
/* 332 */       BundleItem.toggleSelectedItem(itemStack, selectedItemIndex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
/*     */     try {
/* 338 */       doClick(slotIndex, buttonNum, containerInput, player);
/* 339 */     } catch (Exception e) {
/* 340 */       CrashReport report = CrashReport.forThrowable(e, "Container click");
/* 341 */       CrashReportCategory category = report.addCategory("Click info");
/* 342 */       category.setDetail("Menu Type", () -> (this.menuType != null) ? BuiltInRegistries.MENU.getKey(this.menuType).toString() : "<no type>");
/* 343 */       category.setDetail("Menu Class", () -> getClass().getCanonicalName());
/* 344 */       category.setDetail("Slot Count", Integer.valueOf(this.slots.size()));
/* 345 */       category.setDetail("Slot", Integer.valueOf(slotIndex));
/* 346 */       category.setDetail("Button", Integer.valueOf(buttonNum));
/* 347 */       category.setDetail("Type", containerInput);
/* 348 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doClick(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
/* 354 */     Inventory inventory = player.getInventory();
/* 356 */     if (containerInput == ContainerInput.QUICK_CRAFT) {
/* 357 */       int expectedStatus = this.quickcraftStatus;
/* 358 */       this.quickcraftStatus = getQuickcraftHeader(buttonNum);
/* 360 */       if ((expectedStatus != 1 || this.quickcraftStatus != 2) && expectedStatus != this.quickcraftStatus) {
/* 361 */         resetQuickCraft();
/* 362 */       } else if (getCarried().isEmpty()) {
/* 363 */         resetQuickCraft();
/* 364 */       } else if (this.quickcraftStatus == 0) {
/* 365 */         this.quickcraftType = getQuickcraftType(buttonNum);
/* 367 */         if (isValidQuickcraftType(this.quickcraftType, player)) {
/* 368 */           this.quickcraftStatus = 1;
/* 369 */           this.quickcraftSlots.clear();
/*     */         } else {
/* 371 */           resetQuickCraft();
/*     */         } 
/* 373 */       } else if (this.quickcraftStatus == 1) {
/* 374 */         Slot slot = (Slot)this.slots.get(slotIndex);
/* 376 */         ItemStack carriedItemStack = getCarried();
/* 377 */         if (canItemQuickReplace(slot, carriedItemStack, true) && slot.mayPlace(carriedItemStack) && (this.quickcraftType == 2 || carriedItemStack.getCount() > this.quickcraftSlots.size()) && canDragTo(slot))
/* 378 */           this.quickcraftSlots.add(slot); 
/* 380 */       } else if (this.quickcraftStatus == 2) {
/* 381 */         if (!this.quickcraftSlots.isEmpty()) {
/* 382 */           if (this.quickcraftSlots.size() == 1) {
/* 384 */             int slot = ((Slot)this.quickcraftSlots.iterator().next()).index;
/* 385 */             resetQuickCraft();
/* 386 */             doClick(slot, this.quickcraftType, ContainerInput.PICKUP, player);
/*     */             return;
/*     */           } 
/* 389 */           ItemStack source = getCarried().copy();
/* 390 */           if (source.isEmpty()) {
/* 391 */             resetQuickCraft();
/*     */             return;
/*     */           } 
/* 394 */           int remaining = getCarried().getCount();
/* 396 */           for (Slot slot : this.quickcraftSlots) {
/* 397 */             ItemStack carriedItemStack = getCarried();
/* 398 */             if (slot != null && canItemQuickReplace(slot, carriedItemStack, true) && slot.mayPlace(carriedItemStack) && (this.quickcraftType == 2 || carriedItemStack.getCount() >= this.quickcraftSlots.size()) && canDragTo(slot)) {
/* 399 */               int carry = slot.hasItem() ? slot.getItem().getCount() : 0;
/* 400 */               int maxSize = Math.min(source.getMaxStackSize(), slot.getMaxStackSize(source));
/* 401 */               int newCount = Math.min(getQuickCraftPlaceCount(this.quickcraftSlots.size(), this.quickcraftType, source) + carry, maxSize);
/* 403 */               remaining -= newCount - carry;
/* 404 */               slot.setByPlayer(source.copyWithCount(newCount));
/*     */             } 
/*     */           } 
/* 408 */           source.setCount(remaining);
/* 409 */           setCarried(source);
/*     */         } 
/* 412 */         resetQuickCraft();
/*     */       } else {
/* 414 */         resetQuickCraft();
/*     */       } 
/* 416 */     } else if (this.quickcraftStatus != 0) {
/* 417 */       resetQuickCraft();
/* 418 */     } else if ((containerInput == ContainerInput.PICKUP || containerInput == ContainerInput.QUICK_MOVE) && (buttonNum == 0 || buttonNum == 1)) {
/* 419 */       ClickAction clickAction = (buttonNum == 0) ? ClickAction.PRIMARY : ClickAction.SECONDARY;
/* 420 */       if (slotIndex == -999) {
/* 421 */         if (!getCarried().isEmpty())
/* 422 */           if (clickAction == ClickAction.PRIMARY) {
/* 423 */             player.drop(getCarried(), true);
/* 424 */             setCarried(ItemStack.EMPTY);
/*     */           } else {
/* 426 */             player.drop(getCarried().split(1), true);
/*     */           }  
/* 429 */       } else if (containerInput == ContainerInput.QUICK_MOVE) {
/* 430 */         if (slotIndex < 0)
/*     */           return; 
/* 433 */         Slot slot = (Slot)this.slots.get(slotIndex);
/* 434 */         if (!slot.mayPickup(player))
/*     */           return; 
/* 438 */         ItemStack clicked = quickMoveStack(player, slotIndex);
/* 439 */         while (!clicked.isEmpty() && ItemStack.isSameItem(slot.getItem(), clicked))
/* 440 */           clicked = quickMoveStack(player, slotIndex); 
/*     */       } else {
/* 443 */         if (slotIndex < 0)
/*     */           return; 
/* 447 */         Slot slot = (Slot)this.slots.get(slotIndex);
/* 448 */         ItemStack clicked = slot.getItem();
/* 449 */         ItemStack carried = getCarried();
/* 451 */         player.updateTutorialInventoryAction(carried, slot.getItem(), clickAction);
/* 452 */         if (!tryItemClickBehaviourOverride(player, clickAction, slot, clicked, carried))
/* 453 */           if (clicked.isEmpty()) {
/* 454 */             if (!carried.isEmpty()) {
/* 455 */               int amount = (clickAction == ClickAction.PRIMARY) ? carried.getCount() : 1;
/* 456 */               setCarried(slot.safeInsert(carried, amount));
/*     */             } 
/* 458 */           } else if (slot.mayPickup(player)) {
/* 460 */             if (carried.isEmpty()) {
/* 461 */               int amount = (clickAction == ClickAction.PRIMARY) ? clicked.getCount() : ((clicked.getCount() + 1) / 2);
/* 462 */               Optional<ItemStack> newCarried = slot.tryRemove(amount, 2147483647, player);
/* 463 */               newCarried.ifPresent(itemsTaken -> {
/*     */                     setCarried(itemsTaken);
/*     */                     slot.onTake(player, itemsTaken);
/*     */                   });
/* 467 */             } else if (slot.mayPlace(carried)) {
/* 469 */               if (ItemStack.isSameItemSameComponents(clicked, carried)) {
/* 471 */                 int amount = (clickAction == ClickAction.PRIMARY) ? carried.getCount() : 1;
/* 472 */                 setCarried(slot.safeInsert(carried, amount));
/* 475 */               } else if (carried.getCount() <= slot.getMaxStackSize(carried)) {
/* 476 */                 setCarried(clicked);
/* 477 */                 slot.setByPlayer(carried);
/*     */               } 
/* 480 */             } else if (ItemStack.isSameItemSameComponents(clicked, carried)) {
/* 481 */               Optional<ItemStack> newCarried = slot.tryRemove(clicked.getCount(), carried.getMaxStackSize() - carried.getCount(), player);
/* 482 */               newCarried.ifPresent(itemsTaken -> {
/*     */                     carried.grow(itemsTaken.getCount());
/*     */                     slot.onTake(player, itemsTaken);
/*     */                   });
/*     */             } 
/*     */           }  
/* 489 */         slot.setChanged();
/*     */       } 
/* 491 */     } else if (containerInput == ContainerInput.SWAP && ((buttonNum >= 0 && buttonNum < 9) || buttonNum == 40)) {
/* 494 */       ItemStack source = inventory.getItem(buttonNum);
/* 495 */       Slot target = (Slot)this.slots.get(slotIndex);
/* 497 */       ItemStack targetItemStack = target.getItem();
/* 498 */       if (!source.isEmpty() || !targetItemStack.isEmpty())
/* 500 */         if (source.isEmpty()) {
/* 502 */           if (target.mayPickup(player)) {
/* 503 */             inventory.setItem(buttonNum, targetItemStack);
/* 504 */             target.onSwapCraft(targetItemStack.getCount());
/* 505 */             target.setByPlayer(ItemStack.EMPTY);
/* 506 */             target.onTake(player, targetItemStack);
/*     */           } 
/* 508 */         } else if (targetItemStack.isEmpty()) {
/* 509 */           if (target.mayPlace(source)) {
/* 510 */             int maxStackSize = target.getMaxStackSize(source);
/* 511 */             if (source.getCount() > maxStackSize) {
/* 513 */               target.setByPlayer(source.split(maxStackSize));
/*     */             } else {
/* 516 */               inventory.setItem(buttonNum, ItemStack.EMPTY);
/* 517 */               target.setByPlayer(source);
/*     */             } 
/*     */           } 
/* 520 */         } else if (target.mayPickup(player) && target.mayPlace(source)) {
/* 523 */           int maxStackSize = target.getMaxStackSize(source);
/* 524 */           if (source.getCount() > maxStackSize) {
/* 525 */             target.setByPlayer(source.split(maxStackSize));
/* 526 */             target.onTake(player, targetItemStack);
/* 527 */             if (!inventory.add(targetItemStack))
/* 528 */               player.drop(targetItemStack, true); 
/*     */           } else {
/* 531 */             inventory.setItem(buttonNum, targetItemStack);
/* 532 */             target.setByPlayer(source);
/* 533 */             target.onTake(player, targetItemStack);
/*     */           } 
/*     */         }  
/* 536 */     } else if (containerInput == ContainerInput.CLONE && player.hasInfiniteMaterials() && getCarried().isEmpty() && slotIndex >= 0) {
/* 537 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 538 */       if (slot.hasItem())
/* 539 */         setCarried(slot.safeClone(player)); 
/* 541 */     } else if (containerInput == ContainerInput.THROW && getCarried().isEmpty() && slotIndex >= 0) {
/* 542 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 543 */       int amount = (buttonNum == 0) ? 1 : slot.getItem().getCount();
/* 544 */       if (!player.canDropItems())
/*     */         return; 
/* 547 */       ItemStack itemStack = slot.safeTake(amount, 2147483647, player);
/* 548 */       player.drop(itemStack, true);
/* 549 */       player.handleCreativeModeItemDrop(itemStack);
/* 550 */       if (buttonNum == 1)
/* 551 */         while (!itemStack.isEmpty() && ItemStack.isSameItem(slot.getItem(), itemStack)) {
/* 552 */           if (!player.canDropItems())
/*     */             return; 
/* 555 */           itemStack = slot.safeTake(amount, 2147483647, player);
/* 556 */           player.drop(itemStack, true);
/* 557 */           player.handleCreativeModeItemDrop(itemStack);
/*     */         }  
/* 560 */     } else if (containerInput == ContainerInput.PICKUP_ALL && slotIndex >= 0) {
/* 561 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 562 */       ItemStack carried = getCarried();
/* 564 */       if (!carried.isEmpty() && (!slot.hasItem() || !slot.mayPickup(player))) {
/* 565 */         int start = (buttonNum == 0) ? 0 : (this.slots.size() - 1);
/* 566 */         int step = (buttonNum == 0) ? 1 : -1;
/* 568 */         for (int pass = 0; pass < 2; pass++) {
/*     */           int i;
/* 570 */           for (i = start; i >= 0 && i < this.slots.size() && carried.getCount() < carried.getMaxStackSize(); i += step) {
/* 571 */             Slot target = (Slot)this.slots.get(i);
/* 573 */             if (target.hasItem() && canItemQuickReplace(target, carried, true) && target.mayPickup(player) && canTakeItemForPickAll(carried, target)) {
/* 574 */               ItemStack itemStack = target.getItem();
/* 575 */               if (pass != 0 || itemStack.getCount() != itemStack.getMaxStackSize()) {
/* 578 */                 ItemStack removed = target.safeTake(itemStack.getCount(), carried.getMaxStackSize() - carried.getCount(), player);
/* 579 */                 carried.grow(removed.getCount());
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tryItemClickBehaviourOverride(Player player, ClickAction clickAction, Slot slot, ItemStack clicked, ItemStack carried) {
/* 588 */     FeatureFlagSet enabledFeatures = player.level().enabledFeatures();
/* 589 */     if (carried.isItemEnabled(enabledFeatures) && carried.overrideStackedOnOther(slot, clickAction, player))
/* 590 */       return true; 
/* 592 */     return (clicked.isItemEnabled(enabledFeatures) && clicked.overrideOtherStackedOnMe(carried, slot, clickAction, player, createCarriedSlotAccess()));
/*     */   }
/*     */   
/*     */   private SlotAccess createCarriedSlotAccess() {
/* 596 */     return new SlotAccess() {
/*     */         public ItemStack get() {
/* 599 */           return AbstractContainerMenu.this.getCarried();
/*     */         }
/*     */         
/*     */         public boolean set(ItemStack itemStack) {
/* 604 */           AbstractContainerMenu.this.setCarried(itemStack);
/* 605 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
/* 611 */     return true;
/*     */   }
/*     */   
/*     */   public void removed(Player player) {
/* 615 */     if (!(player instanceof net.minecraft.server.level.ServerPlayer))
/*     */       return; 
/* 618 */     ItemStack carried = getCarried();
/* 619 */     if (!carried.isEmpty()) {
/* 620 */       dropOrPlaceInInventory(player, carried);
/* 621 */       setCarried(ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void dropOrPlaceInInventory(Player player, ItemStack carried) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual isRemoved : ()Z
/*     */     //   4: ifeq -> 21
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual getRemovalReason : ()Lnet/minecraft/world/entity/Entity$RemovalReason;
/*     */     //   11: getstatic net/minecraft/world/entity/Entity$RemovalReason.CHANGED_DIMENSION : Lnet/minecraft/world/entity/Entity$RemovalReason;
/*     */     //   14: if_acmpeq -> 21
/*     */     //   17: iconst_1
/*     */     //   18: goto -> 22
/*     */     //   21: iconst_0
/*     */     //   22: istore_2
/*     */     //   23: aload_0
/*     */     //   24: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   27: ifeq -> 48
/*     */     //   30: aload_0
/*     */     //   31: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   34: astore #4
/*     */     //   36: aload #4
/*     */     //   38: invokevirtual hasDisconnected : ()Z
/*     */     //   41: ifeq -> 48
/*     */     //   44: iconst_1
/*     */     //   45: goto -> 49
/*     */     //   48: iconst_0
/*     */     //   49: istore_3
/*     */     //   50: iload_2
/*     */     //   51: ifne -> 58
/*     */     //   54: iload_3
/*     */     //   55: ifeq -> 68
/*     */     //   58: aload_0
/*     */     //   59: aload_1
/*     */     //   60: iconst_0
/*     */     //   61: invokevirtual drop : (Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;
/*     */     //   64: pop
/*     */     //   65: goto -> 83
/*     */     //   68: aload_0
/*     */     //   69: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   72: ifeq -> 83
/*     */     //   75: aload_0
/*     */     //   76: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   79: aload_1
/*     */     //   80: invokevirtual placeItemBackInInventory : (Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   83: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #626	-> 0
/*     */     //   #627	-> 23
/*     */     //   #628	-> 50
/*     */     //   #629	-> 58
/*     */     //   #630	-> 68
/*     */     //   #631	-> 75
/*     */     //   #633	-> 83
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   36	12	4	serverPlayer	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   0	84	0	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	84	1	carried	Lnet/minecraft/world/item/ItemStack;
/*     */     //   23	61	2	playerRemovedNotChangingDimension	Z
/*     */     //   50	34	3	serverPlayerHasDisconnected	Z
/*     */   }
/*     */   
/*     */   protected void clearContainer(Player player, Container container) {
/* 636 */     for (int i = 0; i < container.getContainerSize(); i++)
/* 637 */       dropOrPlaceInInventory(player, container.removeItemNoUpdate(i)); 
/*     */   }
/*     */   
/*     */   public void slotsChanged(Container container) {
/* 642 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   public void setItem(int slot, int stateId, ItemStack itemStack) {
/* 646 */     getSlot(slot).set(itemStack);
/* 647 */     this.stateId = stateId;
/*     */   }
/*     */   
/*     */   public void initializeContents(int stateId, List<ItemStack> items, ItemStack carried) {
/* 651 */     for (int i = 0; i < items.size(); i++)
/* 652 */       getSlot(i).set(items.get(i)); 
/* 654 */     this.carried = carried;
/* 655 */     this.stateId = stateId;
/*     */   }
/*     */   
/*     */   public void setData(int id, int value) {
/* 659 */     ((DataSlot)this.dataSlots.get(id)).set(value);
/*     */   }
/*     */   
/*     */   protected boolean moveItemStackTo(ItemStack itemStack, int startSlot, int endSlot, boolean backwards) {
/* 665 */     boolean anythingChanged = false;
/* 667 */     int destSlot = startSlot;
/* 668 */     if (backwards)
/* 669 */       destSlot = endSlot - 1; 
/* 673 */     if (itemStack.isStackable())
/* 674 */       while (!itemStack.isEmpty() && (backwards ? (destSlot >= startSlot) : (destSlot < endSlot))) {
/* 675 */         Slot slot = (Slot)this.slots.get(destSlot);
/* 676 */         ItemStack target = slot.getItem();
/* 677 */         if (!target.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, target)) {
/* 678 */           int totalStack = target.getCount() + itemStack.getCount();
/* 679 */           int maxStackSize = slot.getMaxStackSize(target);
/* 680 */           if (totalStack <= maxStackSize) {
/* 681 */             itemStack.setCount(0);
/* 682 */             target.setCount(totalStack);
/* 683 */             slot.setChanged();
/* 684 */             anythingChanged = true;
/* 685 */           } else if (target.getCount() < maxStackSize) {
/* 686 */             itemStack.shrink(maxStackSize - target.getCount());
/* 687 */             target.setCount(maxStackSize);
/* 688 */             slot.setChanged();
/* 689 */             anythingChanged = true;
/*     */           } 
/*     */         } 
/* 693 */         if (backwards) {
/* 694 */           destSlot--;
/*     */           continue;
/*     */         } 
/* 696 */         destSlot++;
/*     */       }  
/* 702 */     if (!itemStack.isEmpty()) {
/* 703 */       if (backwards) {
/* 704 */         destSlot = endSlot - 1;
/*     */       } else {
/* 706 */         destSlot = startSlot;
/*     */       } 
/* 708 */       while (backwards ? (destSlot >= startSlot) : (destSlot < endSlot)) {
/* 709 */         Slot slot = (Slot)this.slots.get(destSlot);
/* 710 */         ItemStack target = slot.getItem();
/* 712 */         if (target.isEmpty() && slot.mayPlace(itemStack)) {
/* 713 */           int maxStackSize = slot.getMaxStackSize(itemStack);
/* 714 */           slot.setByPlayer(itemStack.split(Math.min(itemStack.getCount(), maxStackSize)));
/* 715 */           slot.setChanged();
/* 716 */           anythingChanged = true;
/*     */           break;
/*     */         } 
/* 720 */         if (backwards) {
/* 721 */           destSlot--;
/*     */           continue;
/*     */         } 
/* 723 */         destSlot++;
/*     */       } 
/*     */     } 
/* 728 */     return anythingChanged;
/*     */   }
/*     */   
/*     */   public static int getQuickcraftType(int mask) {
/* 732 */     return mask >> 2 & 0x3;
/*     */   }
/*     */   
/*     */   public static int getQuickcraftHeader(int mask) {
/* 736 */     return mask & 0x3;
/*     */   }
/*     */   
/*     */   public static int getQuickcraftMask(int header, int type) {
/* 740 */     return header & 0x3 | (type & 0x3) << 2;
/*     */   }
/*     */   
/*     */   public static boolean isValidQuickcraftType(int type, Player player) {
/* 744 */     if (type == 0)
/* 745 */       return true; 
/* 747 */     if (type == 1)
/* 748 */       return true; 
/* 750 */     if (type == 2 && player.hasInfiniteMaterials())
/* 751 */       return true; 
/* 753 */     return false;
/*     */   }
/*     */   
/*     */   protected void resetQuickCraft() {
/* 757 */     this.quickcraftStatus = 0;
/* 758 */     this.quickcraftSlots.clear();
/*     */   }
/*     */   
/*     */   public static boolean canItemQuickReplace(Slot slot, ItemStack itemStack, boolean ignoreSize) {
/* 762 */     boolean slotIsEmpty = (slot == null || !slot.hasItem());
/* 764 */     if (!slotIsEmpty && ItemStack.isSameItemSameComponents(itemStack, slot.getItem()))
/* 765 */       return (slot.getItem().getCount() + (ignoreSize ? 0 : itemStack.getCount()) <= itemStack.getMaxStackSize()); 
/* 768 */     return slotIsEmpty;
/*     */   }
/*     */   
/*     */   public static int getQuickCraftPlaceCount(int quickCraftSlotsSize, int quickCraftingType, ItemStack itemStack) {
/* 772 */     switch (quickCraftingType) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */     } 
/* 772 */     return 
/*     */       
/* 776 */       itemStack.getCount();
/*     */   }
/*     */   
/*     */   public boolean canDragTo(Slot slot) {
/* 781 */     return true;
/*     */   }
/*     */   
/*     */   public static int getRedstoneSignalFromBlockEntity(BlockEntity blockEntity) {
/* 785 */     if (blockEntity instanceof Container) {
/* 785 */       Container container = (Container)blockEntity;
/* 786 */       return getRedstoneSignalFromContainer(container);
/*     */     } 
/* 789 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getRedstoneSignalFromContainer(Container container) {
/* 793 */     if (container == null)
/* 794 */       return 0; 
/* 796 */     float totalPercent = 0.0F;
/* 797 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 798 */       ItemStack itemStack = container.getItem(i);
/* 799 */       if (!itemStack.isEmpty())
/* 800 */         totalPercent += itemStack.getCount() / container.getMaxStackSize(itemStack); 
/*     */     } 
/* 804 */     totalPercent /= container.getContainerSize();
/* 805 */     return Mth.lerpDiscrete(totalPercent, 0, 15);
/*     */   }
/*     */   
/*     */   public void setCarried(ItemStack carried) {
/* 809 */     this.carried = carried;
/*     */   }
/*     */   
/*     */   public ItemStack getCarried() {
/* 813 */     return this.carried;
/*     */   }
/*     */   
/*     */   public void suppressRemoteUpdates() {
/* 817 */     this.suppressRemoteUpdates = true;
/*     */   }
/*     */   
/*     */   public void resumeRemoteUpdates() {
/* 821 */     this.suppressRemoteUpdates = false;
/*     */   }
/*     */   
/*     */   public void transferState(AbstractContainerMenu otherContainer) {
/* 825 */     HashBasedTable hashBasedTable = HashBasedTable.create();
/*     */     int slotIndex;
/* 826 */     for (slotIndex = 0; slotIndex < otherContainer.slots.size(); slotIndex++) {
/* 827 */       Slot slot = (Slot)otherContainer.slots.get(slotIndex);
/* 828 */       hashBasedTable.put(slot.container, Integer.valueOf(slot.getContainerSlot()), Integer.valueOf(slotIndex));
/*     */     } 
/* 831 */     for (slotIndex = 0; slotIndex < this.slots.size(); slotIndex++) {
/* 832 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 833 */       Integer otherSlotIndex = (Integer)hashBasedTable.get(slot.container, Integer.valueOf(slot.getContainerSlot()));
/* 834 */       if (otherSlotIndex != null) {
/* 835 */         this.lastSlots.set(slotIndex, otherContainer.lastSlots.get(otherSlotIndex.intValue()));
/* 836 */         RemoteSlot sourceRemoteSlot = (RemoteSlot)otherContainer.remoteSlots.get(otherSlotIndex.intValue());
/* 837 */         RemoteSlot targetRemoteSlot = (RemoteSlot)this.remoteSlots.get(slotIndex);
/* 838 */         if (sourceRemoteSlot instanceof RemoteSlot.Synchronized) {
/* 838 */           RemoteSlot.Synchronized synchronizedSource = (RemoteSlot.Synchronized)sourceRemoteSlot;
/* 838 */           if (targetRemoteSlot instanceof RemoteSlot.Synchronized) {
/* 838 */             RemoteSlot.Synchronized synchronizedTarget = (RemoteSlot.Synchronized)targetRemoteSlot;
/* 839 */             synchronizedTarget.copyFrom(synchronizedSource);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public OptionalInt findSlot(Container inventory, int slotIndex) {
/* 846 */     for (int i = 0; i < this.slots.size(); i++) {
/* 847 */       Slot slot = (Slot)this.slots.get(i);
/* 848 */       if (slot.container == inventory && slotIndex == slot.getContainerSlot())
/* 849 */         return OptionalInt.of(i); 
/*     */     } 
/* 853 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public int getStateId() {
/* 857 */     return this.stateId;
/*     */   }
/*     */   
/*     */   public int incrementStateId() {
/* 862 */     this.stateId = this.stateId + 1 & 0x7FFF;
/* 863 */     return this.stateId;
/*     */   }
/*     */   
/*     */   public abstract ItemStack quickMoveStack(Player paramPlayer, int paramInt);
/*     */   
/*     */   public abstract boolean stillValid(Player paramPlayer);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\AbstractContainerMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.npc.ClientSideMerchant;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.ItemCost;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ public class MerchantMenu extends AbstractContainerMenu {
/*     */   protected static final int PAYMENT1_SLOT = 0;
/*     */   
/*     */   protected static final int PAYMENT2_SLOT = 1;
/*     */   
/*     */   protected static final int RESULT_SLOT = 2;
/*     */   
/*     */   private static final int INV_SLOT_START = 3;
/*     */   
/*     */   private static final int INV_SLOT_END = 30;
/*     */   
/*     */   private static final int USE_ROW_SLOT_START = 30;
/*     */   
/*     */   private static final int USE_ROW_SLOT_END = 39;
/*     */   
/*     */   private static final int SELLSLOT1_X = 136;
/*     */   
/*     */   private static final int SELLSLOT2_X = 162;
/*     */   
/*     */   private static final int BUYSLOT_X = 220;
/*     */   
/*     */   private static final int ROW_Y = 37;
/*     */   
/*     */   private final Merchant trader;
/*     */   
/*     */   private final MerchantContainer tradeContainer;
/*     */   
/*     */   private int merchantLevel;
/*     */   
/*     */   private boolean showProgressBar;
/*     */   
/*     */   private boolean canRestock;
/*     */   
/*     */   public MerchantMenu(int containerId, Inventory inventory) {
/*  39 */     this(containerId, inventory, (Merchant)new ClientSideMerchant(inventory.player));
/*     */   }
/*     */   
/*     */   public MerchantMenu(int containerId, Inventory inventory, Merchant merchant) {
/*  43 */     super(MenuType.MERCHANT, containerId);
/*  44 */     this.trader = merchant;
/*  46 */     this.tradeContainer = new MerchantContainer(merchant);
/*  47 */     addSlot(new Slot(this.tradeContainer, 0, 136, 37));
/*  48 */     addSlot(new Slot(this.tradeContainer, 1, 162, 37));
/*  49 */     addSlot(new MerchantResultSlot(inventory.player, merchant, this.tradeContainer, 2, 220, 37));
/*  51 */     addStandardInventorySlots((Container)inventory, 108, 84);
/*     */   }
/*     */   
/*     */   public void setShowProgressBar(boolean show) {
/*  55 */     this.showProgressBar = show;
/*     */   }
/*     */   
/*     */   public void slotsChanged(Container container) {
/*  60 */     this.tradeContainer.updateSellItem();
/*  61 */     super.slotsChanged(container);
/*     */   }
/*     */   
/*     */   public void setSelectionHint(int hint) {
/*  65 */     this.tradeContainer.setSelectionHint(hint);
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/*  70 */     return this.trader.stillValid(player);
/*     */   }
/*     */   
/*     */   public int getTraderXp() {
/*  74 */     return this.trader.getVillagerXp();
/*     */   }
/*     */   
/*     */   public int getFutureTraderXp() {
/*  78 */     return this.tradeContainer.getFutureXp();
/*     */   }
/*     */   
/*     */   public void setXp(int xp) {
/*  82 */     this.trader.overrideXp(xp);
/*     */   }
/*     */   
/*     */   public int getTraderLevel() {
/*  86 */     return this.merchantLevel;
/*     */   }
/*     */   
/*     */   public void setMerchantLevel(int level) {
/*  90 */     this.merchantLevel = level;
/*     */   }
/*     */   
/*     */   public void setCanRestock(boolean canRestock) {
/*  94 */     this.canRestock = canRestock;
/*     */   }
/*     */   
/*     */   public boolean canRestock() {
/*  98 */     return this.canRestock;
/*     */   }
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 108 */     ItemStack clicked = ItemStack.EMPTY;
/* 109 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 110 */     if (slot != null && slot.hasItem()) {
/* 111 */       ItemStack stack = slot.getItem();
/* 112 */       clicked = stack.copy();
/* 114 */       if (slotIndex == 2) {
/* 115 */         if (!moveItemStackTo(stack, 3, 39, true))
/* 116 */           return ItemStack.EMPTY; 
/* 118 */         slot.onQuickCraft(stack, clicked);
/* 120 */         playTradeSound();
/* 121 */       } else if (slotIndex == 0 || slotIndex == 1) {
/* 122 */         if (!moveItemStackTo(stack, 3, 39, false))
/* 123 */           return ItemStack.EMPTY; 
/* 125 */       } else if (slotIndex >= 3 && slotIndex < 30) {
/* 126 */         if (!moveItemStackTo(stack, 30, 39, false))
/* 127 */           return ItemStack.EMPTY; 
/* 129 */       } else if (slotIndex >= 30 && slotIndex < 39 && 
/* 130 */         !moveItemStackTo(stack, 3, 30, false)) {
/* 131 */         return ItemStack.EMPTY;
/*     */       } 
/* 134 */       if (stack.isEmpty()) {
/* 135 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 137 */         slot.setChanged();
/*     */       } 
/* 139 */       if (stack.getCount() == clicked.getCount())
/* 140 */         return ItemStack.EMPTY; 
/* 142 */       slot.onTake(player, stack);
/*     */     } 
/* 145 */     return clicked;
/*     */   }
/*     */   
/*     */   private void playTradeSound() {
/* 149 */     if (!this.trader.isClientSide()) {
/* 150 */       Entity entity = (Entity)this.trader;
/* 151 */       entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removed(Player player) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial removed : (Lnet/minecraft/world/entity/player/Player;)V
/*     */     //   5: aload_0
/*     */     //   6: getfield trader : Lnet/minecraft/world/item/trading/Merchant;
/*     */     //   9: aconst_null
/*     */     //   10: invokeinterface setTradingPlayer : (Lnet/minecraft/world/entity/player/Player;)V
/*     */     //   15: aload_0
/*     */     //   16: getfield trader : Lnet/minecraft/world/item/trading/Merchant;
/*     */     //   19: invokeinterface isClientSide : ()Z
/*     */     //   24: ifeq -> 28
/*     */     //   27: return
/*     */     //   28: aload_1
/*     */     //   29: invokevirtual isAlive : ()Z
/*     */     //   32: ifeq -> 54
/*     */     //   35: aload_1
/*     */     //   36: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   39: ifeq -> 103
/*     */     //   42: aload_1
/*     */     //   43: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   46: astore_2
/*     */     //   47: aload_2
/*     */     //   48: invokevirtual hasDisconnected : ()Z
/*     */     //   51: ifeq -> 103
/*     */     //   54: aload_0
/*     */     //   55: getfield tradeContainer : Lnet/minecraft/world/inventory/MerchantContainer;
/*     */     //   58: iconst_0
/*     */     //   59: invokevirtual removeItemNoUpdate : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   62: astore_3
/*     */     //   63: aload_3
/*     */     //   64: invokevirtual isEmpty : ()Z
/*     */     //   67: ifne -> 77
/*     */     //   70: aload_1
/*     */     //   71: aload_3
/*     */     //   72: iconst_0
/*     */     //   73: invokevirtual drop : (Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;
/*     */     //   76: pop
/*     */     //   77: aload_0
/*     */     //   78: getfield tradeContainer : Lnet/minecraft/world/inventory/MerchantContainer;
/*     */     //   81: iconst_1
/*     */     //   82: invokevirtual removeItemNoUpdate : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   85: astore_3
/*     */     //   86: aload_3
/*     */     //   87: invokevirtual isEmpty : ()Z
/*     */     //   90: ifne -> 100
/*     */     //   93: aload_1
/*     */     //   94: aload_3
/*     */     //   95: iconst_0
/*     */     //   96: invokevirtual drop : (Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;
/*     */     //   99: pop
/*     */     //   100: goto -> 140
/*     */     //   103: aload_1
/*     */     //   104: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   107: ifeq -> 140
/*     */     //   110: aload_1
/*     */     //   111: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   114: aload_0
/*     */     //   115: getfield tradeContainer : Lnet/minecraft/world/inventory/MerchantContainer;
/*     */     //   118: iconst_0
/*     */     //   119: invokevirtual removeItemNoUpdate : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   122: invokevirtual placeItemBackInInventory : (Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   125: aload_1
/*     */     //   126: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   129: aload_0
/*     */     //   130: getfield tradeContainer : Lnet/minecraft/world/inventory/MerchantContainer;
/*     */     //   133: iconst_1
/*     */     //   134: invokevirtual removeItemNoUpdate : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   137: invokevirtual placeItemBackInInventory : (Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   140: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #157	-> 0
/*     */     //   #158	-> 5
/*     */     //   #160	-> 15
/*     */     //   #161	-> 27
/*     */     //   #165	-> 28
/*     */     //   #166	-> 54
/*     */     //   #167	-> 63
/*     */     //   #168	-> 70
/*     */     //   #170	-> 77
/*     */     //   #171	-> 86
/*     */     //   #172	-> 93
/*     */     //   #174	-> 100
/*     */     //   #175	-> 103
/*     */     //   #176	-> 110
/*     */     //   #177	-> 125
/*     */     //   #180	-> 140
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   47	7	2	serverPlayer	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   63	37	3	itemStack	Lnet/minecraft/world/item/ItemStack;
/*     */     //   0	141	0	this	Lnet/minecraft/world/inventory/MerchantMenu;
/*     */     //   0	141	1	player	Lnet/minecraft/world/entity/player/Player;
/*     */   }
/*     */   
/*     */   public void tryMoveItems(int newTradeIndex) {
/* 183 */     if (newTradeIndex < 0 || getOffers().size() <= newTradeIndex)
/*     */       return; 
/* 188 */     ItemStack oldCostA = this.tradeContainer.getItem(0);
/* 189 */     if (!oldCostA.isEmpty()) {
/* 190 */       if (!moveItemStackTo(oldCostA, 3, 39, true))
/*     */         return; 
/* 194 */       this.tradeContainer.setItem(0, oldCostA);
/*     */     } 
/* 197 */     ItemStack oldCostB = this.tradeContainer.getItem(1);
/* 198 */     if (!oldCostB.isEmpty()) {
/* 199 */       if (!moveItemStackTo(oldCostB, 3, 39, true))
/*     */         return; 
/* 203 */       this.tradeContainer.setItem(1, oldCostB);
/*     */     } 
/* 207 */     if (this.tradeContainer.getItem(0).isEmpty() && this.tradeContainer.getItem(1).isEmpty()) {
/* 208 */       MerchantOffer merchantOffer = (MerchantOffer)getOffers().get(newTradeIndex);
/* 209 */       moveFromInventoryToPaymentSlot(0, merchantOffer.getItemCostA());
/* 210 */       merchantOffer.getItemCostB().ifPresent(costB -> moveFromInventoryToPaymentSlot(1, costB));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveFromInventoryToPaymentSlot(int paymentSlot, ItemCost cost) {
/* 215 */     for (int i = 3; i < 39; i++) {
/* 216 */       ItemStack inventoryItem = ((Slot)this.slots.get(i)).getItem();
/* 217 */       if (!inventoryItem.isEmpty() && cost.test(inventoryItem)) {
/* 218 */         ItemStack currentPaymentItem = this.tradeContainer.getItem(paymentSlot);
/* 219 */         if (currentPaymentItem.isEmpty() || ItemStack.isSameItemSameComponents(inventoryItem, currentPaymentItem)) {
/* 224 */           int maxStackSize = inventoryItem.getMaxStackSize();
/* 225 */           int moveCount = Math.min(maxStackSize - currentPaymentItem.getCount(), inventoryItem.getCount());
/* 227 */           ItemStack newPaymentItem = inventoryItem.copyWithCount(currentPaymentItem.getCount() + moveCount);
/* 228 */           inventoryItem.shrink(moveCount);
/* 229 */           this.tradeContainer.setItem(paymentSlot, newPaymentItem);
/* 231 */           if (newPaymentItem.getCount() >= maxStackSize)
/*     */             break; 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOffers(MerchantOffers offers) {
/* 239 */     this.trader.overrideOffers(offers);
/*     */   }
/*     */   
/*     */   public MerchantOffers getOffers() {
/* 243 */     return this.trader.getOffers();
/*     */   }
/*     */   
/*     */   public boolean showProgressBar() {
/* 247 */     return this.showProgressBar;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\MerchantMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
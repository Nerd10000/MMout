/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BannerPatternTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ 
/*     */ public class LoomMenu extends AbstractContainerMenu {
/*     */   private static final int PATTERN_NOT_SET = -1;
/*     */   
/*     */   private static final int INV_SLOT_START = 4;
/*     */   
/*     */   private static final int INV_SLOT_END = 31;
/*     */   
/*     */   private static final int USE_ROW_SLOT_START = 31;
/*     */   
/*     */   private static final int USE_ROW_SLOT_END = 40;
/*     */   
/*     */   private final ContainerLevelAccess access;
/*     */   
/*  36 */   private final DataSlot selectedBannerPatternIndex = DataSlot.standalone();
/*     */   
/*  37 */   private List<Holder<BannerPattern>> selectablePatterns = List.of();
/*     */   
/*     */   private Runnable slotUpdateListener = () -> {
/*     */     
/*     */     };
/*     */   
/*     */   private final HolderGetter<BannerPattern> patternGetter;
/*     */   
/*     */   private final Slot bannerSlot;
/*     */   
/*     */   private final Slot dyeSlot;
/*     */   
/*     */   private final Slot patternSlot;
/*     */   
/*     */   private final Slot resultSlot;
/*     */   
/*     */   private long lastSoundTime;
/*     */   
/*  48 */   private final Container inputContainer = (Container)new SimpleContainer(3) {
/*     */       public void setChanged() {
/*  51 */         super.setChanged();
/*  52 */         LoomMenu.this.slotsChanged((Container)this);
/*  53 */         LoomMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*     */   
/*  57 */   private final Container outputContainer = (Container)new SimpleContainer(1) {
/*     */       public void setChanged() {
/*  60 */         super.setChanged();
/*  61 */         LoomMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*     */   
/*     */   public LoomMenu(int containerId, Inventory inventory) {
/*  66 */     this(containerId, inventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public LoomMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
/*  70 */     super(MenuType.LOOM, containerId);
/*  71 */     this.access = access;
/*  73 */     this.bannerSlot = addSlot(new Slot(this, this.inputContainer, 0, 13, 26) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  76 */             return itemStack.getItem() instanceof net.minecraft.world.item.BannerItem;
/*     */           }
/*     */         });
/*  80 */     this.dyeSlot = addSlot(new Slot(this, this.inputContainer, 1, 33, 26) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  83 */             return LoomMenu.isDyeItem(itemStack);
/*     */           }
/*     */         });
/*  87 */     this.patternSlot = addSlot(new Slot(this, this.inputContainer, 2, 23, 45) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  90 */             return LoomMenu.isPatternItem(itemStack);
/*     */           }
/*     */         });
/*  94 */     this.resultSlot = addSlot(new Slot(this.outputContainer, 0, 143, 57) {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  97 */             return false;
/*     */           }
/*     */           
/*     */           public void onTake(Player player, ItemStack carried) {
/* 102 */             LoomMenu.this.bannerSlot.remove(1);
/* 103 */             LoomMenu.this.dyeSlot.remove(1);
/* 104 */             if (!LoomMenu.this.bannerSlot.hasItem() || !LoomMenu.this.dyeSlot.hasItem())
/* 105 */               LoomMenu.this.selectedBannerPatternIndex.set(-1); 
/* 107 */             access.execute((level, pos) -> {
/*     */                   long gameTime = level.getGameTime();
/*     */                   if (LoomMenu.this.lastSoundTime != gameTime) {
/*     */                     level.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */                     LoomMenu.this.lastSoundTime = gameTime;
/*     */                   } 
/*     */                 });
/* 117 */             super.onTake(player, carried);
/*     */           }
/*     */         });
/* 121 */     addStandardInventorySlots((Container)inventory, 8, 84);
/* 123 */     addDataSlot(this.selectedBannerPatternIndex);
/* 125 */     this.patternGetter = (HolderGetter<BannerPattern>)inventory.player.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN);
/*     */   }
/*     */   
/*     */   private static boolean isPatternItem(ItemStack itemStack) {
/* 129 */     return (itemStack.is(ItemTags.LOOM_PATTERNS) && itemStack.has(DataComponents.PROVIDES_BANNER_PATTERNS));
/*     */   }
/*     */   
/*     */   private static boolean isDyeItem(ItemStack itemStack) {
/* 133 */     return (itemStack.is(ItemTags.LOOM_DYES) && itemStack.has(DataComponents.DYE));
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 138 */     return stillValid(this.access, player, Blocks.LOOM);
/*     */   }
/*     */   
/*     */   public boolean clickMenuButton(Player player, int buttonId) {
/* 143 */     if (buttonId >= 0 && buttonId < this.selectablePatterns.size()) {
/* 144 */       this.selectedBannerPatternIndex.set(buttonId);
/* 145 */       setupResultSlot(this.selectablePatterns.get(buttonId));
/* 146 */       return true;
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */   
/*     */   private List<Holder<BannerPattern>> getSelectablePatterns(ItemStack patternStack) {
/* 152 */     if (patternStack.isEmpty())
/* 153 */       return (List<Holder<BannerPattern>>)this.patternGetter.get(BannerPatternTags.NO_ITEM_REQUIRED).map(ImmutableList::copyOf).orElse(ImmutableList.of()); 
/* 155 */     HolderSet<BannerPattern> itemPatterns = (HolderSet<BannerPattern>)patternStack.get(DataComponents.PROVIDES_BANNER_PATTERNS);
/* 156 */     return (itemPatterns != null) ? (List<Holder<BannerPattern>>)ImmutableList.copyOf((Iterable)itemPatterns) : (List<Holder<BannerPattern>>)ImmutableList.of();
/*     */   }
/*     */   
/*     */   private boolean isValidPatternIndex(int selectedPattern) {
/* 160 */     return (selectedPattern >= 0 && selectedPattern < this.selectablePatterns.size());
/*     */   }
/*     */   
/*     */   public void slotsChanged(Container container) {
/*     */     Holder<BannerPattern> patternToDisplay;
/* 165 */     ItemStack bannerStack = this.bannerSlot.getItem();
/* 166 */     ItemStack dyeStack = this.dyeSlot.getItem();
/* 167 */     ItemStack patternStack = this.patternSlot.getItem();
/* 169 */     if (bannerStack.isEmpty() || dyeStack.isEmpty()) {
/* 170 */       this.resultSlot.set(ItemStack.EMPTY);
/* 171 */       this.selectablePatterns = List.of();
/* 172 */       this.selectedBannerPatternIndex.set(-1);
/*     */       return;
/*     */     } 
/* 176 */     int selectedPattern = this.selectedBannerPatternIndex.get();
/* 177 */     boolean validPatternIndex = isValidPatternIndex(selectedPattern);
/* 178 */     List<Holder<BannerPattern>> previousSelectablePatterns = this.selectablePatterns;
/* 179 */     this.selectablePatterns = getSelectablePatterns(patternStack);
/* 181 */     if (this.selectablePatterns.size() == 1) {
/* 183 */       this.selectedBannerPatternIndex.set(0);
/* 184 */       patternToDisplay = this.selectablePatterns.get(0);
/* 185 */     } else if (!validPatternIndex) {
/* 186 */       this.selectedBannerPatternIndex.set(-1);
/* 187 */       patternToDisplay = null;
/*     */     } else {
/* 189 */       Holder<BannerPattern> selectedValue = previousSelectablePatterns.get(selectedPattern);
/* 190 */       int newSelectedIndex = this.selectablePatterns.indexOf(selectedValue);
/* 191 */       if (newSelectedIndex != -1) {
/* 192 */         patternToDisplay = selectedValue;
/* 193 */         this.selectedBannerPatternIndex.set(newSelectedIndex);
/*     */       } else {
/* 195 */         patternToDisplay = null;
/* 196 */         this.selectedBannerPatternIndex.set(-1);
/*     */       } 
/*     */     } 
/* 200 */     if (patternToDisplay != null) {
/* 201 */       BannerPatternLayers patterns = (BannerPatternLayers)bannerStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 202 */       boolean hasMaxPatterns = (patterns.layers().size() >= 6);
/* 203 */       if (hasMaxPatterns) {
/* 204 */         this.selectedBannerPatternIndex.set(-1);
/* 205 */         this.resultSlot.set(ItemStack.EMPTY);
/*     */       } else {
/* 207 */         setupResultSlot(patternToDisplay);
/*     */       } 
/*     */     } else {
/* 210 */       this.resultSlot.set(ItemStack.EMPTY);
/*     */     } 
/* 212 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   public List<Holder<BannerPattern>> getSelectablePatterns() {
/* 216 */     return this.selectablePatterns;
/*     */   }
/*     */   
/*     */   public int getSelectedBannerPatternIndex() {
/* 220 */     return this.selectedBannerPatternIndex.get();
/*     */   }
/*     */   
/*     */   public void registerUpdateListener(Runnable slotUpdateListener) {
/* 224 */     this.slotUpdateListener = slotUpdateListener;
/*     */   }
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 229 */     ItemStack clicked = ItemStack.EMPTY;
/* 230 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 231 */     if (slot != null && slot.hasItem()) {
/* 232 */       ItemStack stack = slot.getItem();
/* 233 */       clicked = stack.copy();
/* 235 */       if (slotIndex == this.resultSlot.index) {
/* 236 */         if (!moveItemStackTo(stack, 4, 40, true))
/* 237 */           return ItemStack.EMPTY; 
/* 239 */         slot.onQuickCraft(stack, clicked);
/* 240 */       } else if (slotIndex == this.dyeSlot.index || slotIndex == this.bannerSlot.index || slotIndex == this.patternSlot.index) {
/* 241 */         if (!moveItemStackTo(stack, 4, 40, false))
/* 242 */           return ItemStack.EMPTY; 
/* 244 */       } else if (stack.getItem() instanceof net.minecraft.world.item.BannerItem) {
/* 245 */         if (!moveItemStackTo(stack, this.bannerSlot.index, this.bannerSlot.index + 1, false))
/* 246 */           return ItemStack.EMPTY; 
/* 248 */       } else if (isDyeItem(stack)) {
/* 249 */         if (!moveItemStackTo(stack, this.dyeSlot.index, this.dyeSlot.index + 1, false))
/* 250 */           return ItemStack.EMPTY; 
/* 252 */       } else if (isPatternItem(stack)) {
/* 253 */         if (!moveItemStackTo(stack, this.patternSlot.index, this.patternSlot.index + 1, false))
/* 254 */           return ItemStack.EMPTY; 
/* 256 */       } else if (slotIndex >= 4 && slotIndex < 31) {
/* 257 */         if (!moveItemStackTo(stack, 31, 40, false))
/* 258 */           return ItemStack.EMPTY; 
/* 260 */       } else if (slotIndex >= 31 && slotIndex < 40 && 
/* 261 */         !moveItemStackTo(stack, 4, 31, false)) {
/* 262 */         return ItemStack.EMPTY;
/*     */       } 
/* 266 */       if (stack.isEmpty()) {
/* 267 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 269 */         slot.setChanged();
/*     */       } 
/* 271 */       if (stack.getCount() == clicked.getCount())
/* 272 */         return ItemStack.EMPTY; 
/* 274 */       slot.onTake(player, stack);
/*     */     } 
/* 277 */     return clicked;
/*     */   }
/*     */   
/*     */   public void removed(Player player) {
/* 282 */     super.removed(player);
/* 283 */     this.access.execute((level, pos) -> clearContainer(player, this.inputContainer));
/*     */   }
/*     */   
/*     */   private void setupResultSlot(Holder<BannerPattern> pattern) {
/* 287 */     ItemStack bannerStack = this.bannerSlot.getItem();
/* 288 */     ItemStack dyeStack = this.dyeSlot.getItem();
/* 289 */     ItemStack result = ItemStack.EMPTY;
/* 291 */     if (!bannerStack.isEmpty() && !dyeStack.isEmpty()) {
/* 292 */       DyeColor patternColor = (DyeColor)dyeStack.get(DataComponents.DYE);
/* 293 */       if (patternColor != null) {
/* 294 */         result = bannerStack.copyWithCount(1);
/* 295 */         result.update(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY, layers -> (new BannerPatternLayers.Builder()).addAll(layers).add(pattern, patternColor).build());
/*     */       } 
/*     */     } 
/* 300 */     if (!ItemStack.matches(result, this.resultSlot.getItem()))
/* 301 */       this.resultSlot.set(result); 
/*     */   }
/*     */   
/*     */   public Slot getBannerSlot() {
/* 306 */     return this.bannerSlot;
/*     */   }
/*     */   
/*     */   public Slot getDyeSlot() {
/* 310 */     return this.dyeSlot;
/*     */   }
/*     */   
/*     */   public Slot getPatternSlot() {
/* 314 */     return this.patternSlot;
/*     */   }
/*     */   
/*     */   public Slot getResultSlot() {
/* 318 */     return this.resultSlot;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\LoomMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
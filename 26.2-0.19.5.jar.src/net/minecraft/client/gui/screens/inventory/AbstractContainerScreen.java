/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.BundleMouseActions;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.ItemSlotMouseAction;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerInput;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public abstract class AbstractContainerScreen<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {
/*  31 */   public static final Identifier INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/inventory.png");
/*     */   
/*  33 */   private static final Identifier SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.withDefaultNamespace("container/slot_highlight_back");
/*     */   
/*  34 */   private static final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.withDefaultNamespace("container/slot_highlight_front");
/*     */   
/*     */   protected static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   
/*     */   protected static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*     */   
/*     */   protected static final int DEFAULT_IMAGE_WIDTH = 176;
/*     */   
/*     */   protected static final int DEFAULT_IMAGE_HEIGHT = 166;
/*     */   
/*     */   protected final int imageWidth;
/*     */   
/*     */   protected final int imageHeight;
/*     */   
/*     */   protected int titleLabelX;
/*     */   
/*     */   protected int titleLabelY;
/*     */   
/*     */   protected int inventoryLabelX;
/*     */   
/*     */   protected int inventoryLabelY;
/*     */   
/*     */   private final List<ItemSlotMouseAction> itemSlotMouseActions;
/*     */   
/*     */   protected final T menu;
/*     */   
/*     */   protected final Component playerInventoryTitle;
/*     */   
/*     */   protected Slot hoveredSlot;
/*     */   
/*     */   private Slot lastClickSlot;
/*     */   
/*     */   protected int leftPos;
/*     */   
/*     */   protected int topPos;
/*     */   
/*  59 */   protected final Set<Slot> quickCraftSlots = Sets.newHashSet();
/*     */   
/*     */   protected boolean isQuickCrafting;
/*     */   
/*     */   private int quickCraftingType;
/*     */   
/*     */   private int quickCraftingButton;
/*     */   
/*     */   private boolean skipNextRelease;
/*     */   
/*     */   private int quickCraftingRemainder;
/*     */   
/*     */   private boolean doubleclick;
/*     */   
/*  66 */   private ItemStack lastQuickMoved = ItemStack.EMPTY;
/*     */   
/*     */   public AbstractContainerScreen(T menu, Inventory inventory, Component title) {
/*  69 */     this(menu, inventory, title, 176, 166);
/*     */   }
/*     */   
/*     */   public AbstractContainerScreen(T menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
/*  73 */     super(title);
/*  74 */     this.menu = menu;
/*  75 */     this.playerInventoryTitle = inventory.getDisplayName();
/*  76 */     this.imageWidth = imageWidth;
/*  77 */     this.imageHeight = imageHeight;
/*  78 */     this.skipNextRelease = true;
/*  79 */     this.titleLabelX = 8;
/*  80 */     this.titleLabelY = 6;
/*  81 */     this.inventoryLabelX = 8;
/*  82 */     this.inventoryLabelY = imageHeight - 94;
/*  83 */     this.itemSlotMouseActions = new ArrayList<>();
/*     */   }
/*     */   
/*     */   protected void init() {
/*  88 */     this.leftPos = (this.width - this.imageWidth) / 2;
/*  89 */     this.topPos = (this.height - this.imageHeight) / 2;
/*  90 */     this.itemSlotMouseActions.clear();
/*  91 */     addItemSlotMouseAction((ItemSlotMouseAction)new BundleMouseActions(this.minecraft));
/*     */   }
/*     */   
/*     */   protected void addItemSlotMouseAction(ItemSlotMouseAction itemSlotMouseAction) {
/*  95 */     this.itemSlotMouseActions.add(itemSlotMouseAction);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 100 */     extractContents(graphics, mouseX, mouseY, a);
/* 101 */     extractCarriedItem(graphics, mouseX, mouseY);
/* 102 */     extractTooltip(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 106 */     int xo = this.leftPos;
/* 107 */     int yo = this.topPos;
/* 109 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 111 */     graphics.pose().pushMatrix();
/* 112 */     graphics.pose().translate(xo, yo);
/* 114 */     extractLabels(graphics, mouseX, mouseY);
/* 116 */     Slot previouslyHoveredSlot = this.hoveredSlot;
/* 117 */     this.hoveredSlot = getHoveredSlot(mouseX, mouseY);
/* 119 */     extractSlotHighlightBack(graphics);
/* 120 */     extractSlots(graphics, mouseX, mouseY);
/* 121 */     extractSlotHighlightFront(graphics);
/* 123 */     if (previouslyHoveredSlot != null && previouslyHoveredSlot != this.hoveredSlot)
/* 124 */       onStopHovering(previouslyHoveredSlot); 
/* 127 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   public void extractCarriedItem(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 131 */     ItemStack carried = this.menu.getCarried();
/* 132 */     if (!carried.isEmpty()) {
/* 133 */       int xOffset = 8;
/* 134 */       int yOffset = 8;
/* 135 */       String itemCount = null;
/* 137 */       if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
/* 138 */         carried = carried.copyWithCount(this.quickCraftingRemainder);
/* 140 */         if (carried.isEmpty())
/* 141 */           itemCount = String.valueOf(ChatFormatting.YELLOW) + "0"; 
/*     */       } 
/* 144 */       graphics.nextStratum();
/* 145 */       extractFloatingItem(graphics, carried, mouseX - 8, mouseY - 8, itemCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractSlots(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 150 */     for (Slot slot : ((AbstractContainerMenu)this.menu).slots) {
/* 151 */       if (slot.isActive())
/* 152 */         extractSlot(graphics, slot, mouseX, mouseY); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 159 */     if (this.hoveredSlot != null && this.hoveredSlot.hasItem())
/* 160 */       for (ItemSlotMouseAction itemMouseAction : this.itemSlotMouseActions) {
/* 161 */         if (itemMouseAction.matches(this.hoveredSlot) && 
/* 162 */           itemMouseAction.onMouseScrolled(scrollX, scrollY, this.hoveredSlot.index, this.hoveredSlot.getItem()))
/* 163 */           return true; 
/*     */       }  
/* 168 */     return false;
/*     */   }
/*     */   
/*     */   private void extractSlotHighlightBack(GuiGraphicsExtractor graphics) {
/* 172 */     if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable())
/* 173 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, this.hoveredSlot.x - 4, this.hoveredSlot.y - 4, 24, 24); 
/*     */   }
/*     */   
/*     */   private void extractSlotHighlightFront(GuiGraphicsExtractor graphics) {
/* 178 */     if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable())
/* 179 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, this.hoveredSlot.x - 4, this.hoveredSlot.y - 4, 24, 24); 
/*     */   }
/*     */   
/*     */   protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 184 */     if (this.hoveredSlot == null || !this.hoveredSlot.hasItem())
/*     */       return; 
/* 188 */     ItemStack item = this.hoveredSlot.getItem();
/* 189 */     if (this.menu.getCarried().isEmpty() || showTooltipWithItemInHand(item))
/* 190 */       graphics.setTooltipForNextFrame(this.font, getTooltipFromContainerItem(item), item.getTooltipImage(), mouseX, mouseY, (Identifier)item.get(DataComponents.TOOLTIP_STYLE)); 
/*     */   }
/*     */   
/*     */   private boolean showTooltipWithItemInHand(ItemStack item) {
/* 195 */     return ((Boolean)item.getTooltipImage()
/* 196 */       .map(ClientTooltipComponent::create)
/* 197 */       .map(ClientTooltipComponent::showTooltipWithItemInHand)
/* 198 */       .orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   protected List<Component> getTooltipFromContainerItem(ItemStack itemStack) {
/* 202 */     return getTooltipFromItem(this.minecraft, itemStack);
/*     */   }
/*     */   
/*     */   private void extractFloatingItem(GuiGraphicsExtractor graphics, ItemStack carried, int x, int y, String itemCount) {
/* 206 */     graphics.item(carried, x, y);
/* 207 */     graphics.itemDecorations(this.font, carried, x, y, itemCount);
/*     */   }
/*     */   
/*     */   protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
/* 211 */     graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
/* 212 */     graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
/*     */   }
/*     */   
/*     */   protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
/* 216 */     int x = slot.x;
/* 217 */     int y = slot.y;
/* 218 */     ItemStack itemStack = slot.getItem();
/* 219 */     boolean quickCraftStack = false;
/* 220 */     boolean done = false;
/* 221 */     ItemStack carried = this.menu.getCarried();
/* 222 */     String itemCount = null;
/* 224 */     if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !carried.isEmpty()) {
/* 225 */       if (this.quickCraftSlots.size() == 1)
/*     */         return; 
/* 228 */       if (AbstractContainerMenu.canItemQuickReplace(slot, carried, true) && this.menu.canDragTo(slot)) {
/* 229 */         quickCraftStack = true;
/* 231 */         int maxSize = Math.min(carried.getMaxStackSize(), slot.getMaxStackSize(carried));
/* 232 */         int carry = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
/* 233 */         int newCount = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots.size(), this.quickCraftingType, carried) + carry;
/* 234 */         if (newCount > maxSize) {
/* 235 */           newCount = maxSize;
/* 236 */           itemCount = ChatFormatting.YELLOW.toString() + ChatFormatting.YELLOW.toString();
/*     */         } 
/* 239 */         itemStack = carried.copyWithCount(newCount);
/*     */       } else {
/* 241 */         this.quickCraftSlots.remove(slot);
/* 242 */         recalculateQuickCraftRemaining();
/*     */       } 
/*     */     } 
/* 246 */     if (itemStack.isEmpty() && slot.isActive()) {
/* 247 */       Identifier icon = slot.getNoItemIcon();
/* 248 */       if (icon != null) {
/* 249 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, x, y, 16, 16);
/* 250 */         done = true;
/*     */       } 
/*     */     } 
/* 254 */     if (!done) {
/* 255 */       if (quickCraftStack)
/* 256 */         graphics.fill(x, y, x + 16, y + 16, -2130706433); 
/* 258 */       int seed = slot.x + slot.y * this.imageWidth;
/* 259 */       if (slot.isFake()) {
/* 260 */         graphics.fakeItem(itemStack, x, y, seed);
/*     */       } else {
/* 262 */         graphics.item(itemStack, x, y, seed);
/*     */       } 
/* 264 */       graphics.itemDecorations(this.font, itemStack, x, y, itemCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recalculateQuickCraftRemaining() {
/* 269 */     ItemStack carried = this.menu.getCarried();
/* 270 */     if (carried.isEmpty() || !this.isQuickCrafting)
/*     */       return; 
/* 274 */     if (this.quickCraftingType == 2) {
/* 275 */       this.quickCraftingRemainder = carried.getMaxStackSize();
/*     */       return;
/*     */     } 
/* 279 */     this.quickCraftingRemainder = carried.getCount();
/* 281 */     for (Slot slot : this.quickCraftSlots) {
/* 282 */       ItemStack slotItemStack = slot.getItem();
/* 283 */       int carry = slotItemStack.isEmpty() ? 0 : slotItemStack.getCount();
/* 284 */       int maxSize = Math.min(carried.getMaxStackSize(), slot.getMaxStackSize(carried));
/* 285 */       int newCount = Math.min(AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots.size(), this.quickCraftingType, carried) + carry, maxSize);
/* 286 */       this.quickCraftingRemainder -= newCount - carry;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Slot getHoveredSlot(double x, double y) {
/* 291 */     for (Slot slot : ((AbstractContainerMenu)this.menu).slots) {
/* 292 */       if (slot.isActive() && isHovering(slot, x, y))
/* 293 */         return slot; 
/*     */     } 
/* 296 */     return null;
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 301 */     if (super.mouseClicked(event, doubleClick))
/* 302 */       return true; 
/* 304 */     boolean cloning = (this.minecraft.options.keyPickItem.matchesMouse(event) && this.minecraft.player.hasInfiniteMaterials());
/* 305 */     Slot slot = getHoveredSlot(event.x(), event.y());
/* 306 */     this.doubleclick = (this.lastClickSlot == slot && doubleClick);
/* 307 */     this.skipNextRelease = false;
/* 309 */     if (event.button() == 0 || event.button() == 1 || cloning) {
/* 310 */       int xo = this.leftPos;
/* 311 */       int yo = this.topPos;
/* 312 */       boolean clickedOutside = hasClickedOutside(event.x(), event.y(), xo, yo);
/* 314 */       int slotId = -1;
/* 315 */       if (slot != null)
/* 316 */         slotId = slot.index; 
/* 319 */       if (clickedOutside)
/* 320 */         slotId = -999; 
/* 323 */       if (slotId != -1 && 
/* 324 */         !this.isQuickCrafting)
/* 325 */         if (this.menu.getCarried().isEmpty()) {
/* 326 */           if (cloning) {
/* 327 */             slotClicked(slot, slotId, event.button(), ContainerInput.CLONE);
/*     */           } else {
/* 329 */             boolean quickKey = (slotId != -999 && event.hasShiftDown());
/* 330 */             ContainerInput containerInput = ContainerInput.PICKUP;
/* 331 */             if (quickKey) {
/* 332 */               this.lastQuickMoved = (slot != null && slot.hasItem()) ? slot.getItem().copy() : ItemStack.EMPTY;
/* 333 */               containerInput = ContainerInput.QUICK_MOVE;
/* 334 */             } else if (slotId == -999) {
/* 335 */               containerInput = ContainerInput.THROW;
/*     */             } 
/* 337 */             slotClicked(slot, slotId, event.button(), containerInput);
/*     */           } 
/* 339 */           this.skipNextRelease = true;
/*     */         } else {
/* 341 */           this.isQuickCrafting = true;
/* 342 */           this.quickCraftingButton = event.button();
/* 343 */           this.quickCraftSlots.clear();
/* 345 */           if (event.button() == 0) {
/* 346 */             this.quickCraftingType = 0;
/* 347 */           } else if (event.button() == 1) {
/* 348 */             this.quickCraftingType = 1;
/* 349 */           } else if (cloning) {
/* 350 */             this.quickCraftingType = 2;
/*     */           } 
/*     */         }  
/*     */     } else {
/* 356 */       checkHotbarMouseClicked(event);
/*     */     } 
/* 359 */     this.lastClickSlot = slot;
/* 361 */     return true;
/*     */   }
/*     */   
/*     */   private void checkHotbarMouseClicked(MouseButtonEvent event) {
/* 365 */     if (this.hoveredSlot != null && this.menu.getCarried().isEmpty()) {
/* 366 */       if (this.minecraft.options.keySwapOffhand.matchesMouse(event)) {
/* 367 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ContainerInput.SWAP);
/*     */         return;
/*     */       } 
/* 370 */       for (int i = 0; i < 9; i++) {
/* 371 */         if (this.minecraft.options.keyHotbarSlots[i].matchesMouse(event))
/* 372 */           slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ContainerInput.SWAP); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/* 379 */     return (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 384 */     Slot slot = getHoveredSlot(event.x(), event.y());
/* 385 */     ItemStack carried = this.menu.getCarried();
/* 387 */     if (slot != null && shouldAddSlotToQuickCraft(slot, carried) && this.quickCraftSlots.add(slot)) {
/* 388 */       recalculateQuickCraftRemaining();
/* 389 */       return true;
/*     */     } 
/* 393 */     if (slot == null && this.menu.getCarried().isEmpty())
/* 394 */       return super.mouseDragged(event, dx, dy); 
/* 397 */     return true;
/*     */   }
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 402 */     Slot slot = getHoveredSlot(event.x(), event.y());
/* 403 */     int xo = this.leftPos;
/* 404 */     int yo = this.topPos;
/* 405 */     boolean clickedOutside = hasClickedOutside(event.x(), event.y(), xo, yo);
/* 407 */     int slotId = -1;
/* 408 */     if (slot != null)
/* 409 */       slotId = slot.index; 
/* 412 */     if (clickedOutside)
/* 413 */       slotId = -999; 
/* 416 */     if (this.doubleclick && slot != null && event.button() == 0 && this.menu.canTakeItemForPickAll(ItemStack.EMPTY, slot)) {
/* 417 */       if (event.hasShiftDown()) {
/* 418 */         if (!this.lastQuickMoved.isEmpty())
/* 419 */           for (Slot target : ((AbstractContainerMenu)this.menu).slots) {
/* 420 */             if (target != null && target.mayPickup((Player)this.minecraft.player) && target.hasItem() && target.container == slot.container && AbstractContainerMenu.canItemQuickReplace(target, this.lastQuickMoved, true))
/* 421 */               slotClicked(target, target.index, event.button(), ContainerInput.QUICK_MOVE); 
/*     */           }  
/*     */       } else {
/* 426 */         slotClicked(slot, slotId, event.button(), ContainerInput.PICKUP_ALL);
/*     */       } 
/* 428 */       this.doubleclick = false;
/*     */     } else {
/* 430 */       if (this.isQuickCrafting && this.quickCraftingButton != event.button()) {
/* 431 */         this.isQuickCrafting = false;
/* 432 */         this.quickCraftSlots.clear();
/* 433 */         this.skipNextRelease = true;
/* 434 */         return true;
/*     */       } 
/* 435 */       if (this.skipNextRelease) {
/* 436 */         this.skipNextRelease = false;
/* 437 */         return true;
/*     */       } 
/* 440 */       if (this.isQuickCrafting && !this.quickCraftSlots.isEmpty()) {
/* 441 */         quickCraftToSlots();
/* 442 */       } else if (!this.menu.getCarried().isEmpty()) {
/* 443 */         if (this.minecraft.options.keyPickItem.matchesMouse(event)) {
/* 444 */           slotClicked(slot, slotId, event.button(), ContainerInput.CLONE);
/*     */         } else {
/* 446 */           boolean quickKey = (slotId != -999 && event.hasShiftDown());
/* 447 */           if (quickKey)
/* 448 */             this.lastQuickMoved = (slot != null && slot.hasItem()) ? slot.getItem().copy() : ItemStack.EMPTY; 
/* 450 */           slotClicked(slot, slotId, event.button(), quickKey ? ContainerInput.QUICK_MOVE : ContainerInput.PICKUP);
/*     */         } 
/*     */       } 
/*     */     } 
/* 455 */     this.isQuickCrafting = false;
/* 457 */     return super.mouseReleased(event);
/*     */   }
/*     */   
/*     */   private boolean isHovering(Slot slot, double xm, double ym) {
/* 461 */     return isHovering(slot.x, slot.y, 16, 16, xm, ym);
/*     */   }
/*     */   
/*     */   protected boolean isHovering(int left, int top, int w, int h, double xm, double ym) {
/* 465 */     int xo = this.leftPos;
/* 466 */     int yo = this.topPos;
/* 467 */     xm -= xo;
/* 468 */     ym -= yo;
/* 470 */     return (xm >= (left - 1) && xm < (left + w + 1) && ym >= (top - 1) && ym < (top + h + 1));
/*     */   }
/*     */   
/*     */   private void onStopHovering(Slot slot) {
/* 474 */     if (slot.hasItem())
/* 475 */       for (ItemSlotMouseAction itemMouseAction : this.itemSlotMouseActions) {
/* 476 */         if (itemMouseAction.matches(slot))
/* 477 */           itemMouseAction.onStopHovering(slot); 
/*     */       }  
/*     */   }
/*     */   
/*     */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
/* 484 */     if (slot != null)
/* 485 */       slotId = slot.index; 
/* 487 */     onMouseClickAction(slot, containerInput);
/* 488 */     this.minecraft.gameMode.handleContainerInput(((AbstractContainerMenu)this.menu).containerId, slotId, buttonNum, containerInput, (Player)this.minecraft.player);
/*     */   }
/*     */   
/*     */   protected void onMouseClickAction(Slot slot, ContainerInput containerInput) {
/* 492 */     if (slot != null && slot.hasItem())
/* 493 */       for (ItemSlotMouseAction itemMouseAction : this.itemSlotMouseActions) {
/* 494 */         if (itemMouseAction.matches(slot))
/* 495 */           itemMouseAction.onSlotClicked(slot, containerInput); 
/*     */       }  
/*     */   }
/*     */   
/*     */   protected void handleSlotStateChanged(int slotId, int containerId, boolean newState) {
/* 502 */     this.minecraft.gameMode.handleSlotStateChanged(slotId, containerId, newState);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 507 */     if (super.keyPressed(event))
/* 508 */       return true; 
/* 510 */     if (this.minecraft.options.keyInventory.matches(event)) {
/* 511 */       onClose();
/* 512 */       return true;
/*     */     } 
/* 514 */     checkHotbarKeyPressed(event);
/* 516 */     if (this.hoveredSlot != null && this.hoveredSlot.hasItem())
/* 517 */       if (this.minecraft.options.keyPickItem.matches(event)) {
/* 518 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ContainerInput.CLONE);
/* 519 */       } else if (this.minecraft.options.keyDrop.matches(event)) {
/* 520 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, event.hasControlDown() ? 1 : 0, ContainerInput.THROW);
/*     */       }  
/* 523 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean checkHotbarKeyPressed(KeyEvent event) {
/* 527 */     if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null) {
/* 528 */       if (this.minecraft.options.keySwapOffhand.matches(event)) {
/* 529 */         slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ContainerInput.SWAP);
/* 530 */         return true;
/*     */       } 
/* 532 */       for (int i = 0; i < 9; i++) {
/* 533 */         if (this.minecraft.options.keyHotbarSlots[i].matches(event)) {
/* 534 */           slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ContainerInput.SWAP);
/* 535 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 540 */     return false;
/*     */   }
/*     */   
/*     */   public void removed() {
/* 545 */     if (this.minecraft.player == null)
/*     */       return; 
/* 548 */     this.menu.removed((Player)this.minecraft.player);
/*     */   }
/*     */   
/*     */   public boolean isPauseScreen() {
/* 553 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isInGameUi() {
/* 558 */     return true;
/*     */   }
/*     */   
/*     */   public final void tick() {
/* 563 */     super.tick();
/* 566 */     if (!this.minecraft.player.isAlive() || this.minecraft.player.isRemoved()) {
/* 567 */       this.minecraft.player.closeContainer();
/*     */     } else {
/* 569 */       containerTick();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void containerTick() {}
/*     */   
/*     */   private boolean shouldAddSlotToQuickCraft(Slot slot, ItemStack carried) {
/* 577 */     return (this.isQuickCrafting && !carried.isEmpty() && (carried.getCount() > this.quickCraftSlots.size() || this.quickCraftingType == 2) && 
/* 578 */       AbstractContainerMenu.canItemQuickReplace(slot, carried, true) && slot.mayPlace(carried) && this.menu.canDragTo(slot));
/*     */   }
/*     */   
/*     */   private void quickCraftToSlots() {
/* 582 */     slotClicked((Slot)null, -999, AbstractContainerMenu.getQuickcraftMask(0, this.quickCraftingType), ContainerInput.QUICK_CRAFT);
/* 584 */     for (Slot quickSlot : this.quickCraftSlots)
/* 585 */       slotClicked(quickSlot, quickSlot.index, AbstractContainerMenu.getQuickcraftMask(1, this.quickCraftingType), ContainerInput.QUICK_CRAFT); 
/* 588 */     slotClicked((Slot)null, -999, AbstractContainerMenu.getQuickcraftMask(2, this.quickCraftingType), ContainerInput.QUICK_CRAFT);
/*     */   }
/*     */   
/*     */   public T getMenu() {
/* 593 */     return this.menu;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 598 */     this.minecraft.player.closeContainer();
/* 599 */     if (this.hoveredSlot != null)
/* 600 */       onStopHovering(this.hoveredSlot); 
/* 602 */     super.onClose();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\AbstractContainerScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
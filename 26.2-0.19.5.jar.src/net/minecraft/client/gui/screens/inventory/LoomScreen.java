/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.object.banner.BannerFlagModel;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.LoomMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.BannerItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ 
/*     */ public class LoomScreen extends AbstractContainerScreen<LoomMenu> {
/*  35 */   private static final Identifier BANNER_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/banner");
/*     */   
/*  36 */   private static final Identifier DYE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/dye");
/*     */   
/*  37 */   private static final Identifier PATTERN_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/banner_pattern");
/*     */   
/*  38 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/loom/scroller");
/*     */   
/*  39 */   private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/loom/scroller_disabled");
/*     */   
/*  40 */   private static final Identifier PATTERN_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/loom/pattern_selected");
/*     */   
/*  41 */   private static final Identifier PATTERN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/loom/pattern_highlighted");
/*     */   
/*  42 */   private static final Identifier PATTERN_SPRITE = Identifier.withDefaultNamespace("container/loom/pattern");
/*     */   
/*  43 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/loom/error");
/*     */   
/*  44 */   private static final Identifier BG_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/loom.png");
/*     */   
/*     */   private static final int PATTERN_COLUMNS = 4;
/*     */   
/*     */   private static final int PATTERN_ROWS = 4;
/*     */   
/*     */   private static final int SCROLLER_WIDTH = 12;
/*     */   
/*     */   private static final int SCROLLER_HEIGHT = 15;
/*     */   
/*     */   private static final int PATTERN_IMAGE_SIZE = 14;
/*     */   
/*     */   private static final int SCROLLER_FULL_HEIGHT = 56;
/*     */   
/*     */   private static final int PATTERNS_X = 60;
/*     */   
/*     */   private static final int PATTERNS_Y = 13;
/*     */   
/*     */   private static final float BANNER_PATTERN_TEXTURE_SIZE = 64.0F;
/*     */   
/*     */   private static final float BANNER_PATTERN_WIDTH = 21.0F;
/*     */   
/*     */   private static final float BANNER_PATTERN_HEIGHT = 40.0F;
/*     */   
/*     */   private BannerFlagModel flag;
/*     */   
/*     */   private BannerPatternLayers resultBannerPatterns;
/*     */   
/*  62 */   private ItemStack bannerStack = ItemStack.EMPTY;
/*     */   
/*  63 */   private ItemStack dyeStack = ItemStack.EMPTY;
/*     */   
/*  64 */   private ItemStack patternStack = ItemStack.EMPTY;
/*     */   
/*     */   private boolean displayPatterns;
/*     */   
/*     */   private boolean hasMaxPatterns;
/*     */   
/*     */   private float scrollOffs;
/*     */   
/*     */   private boolean scrolling;
/*     */   
/*     */   private int startRow;
/*     */   
/*     */   public LoomScreen(LoomMenu menu, Inventory inventory, Component title) {
/*  73 */     super(menu, inventory, title);
/*  74 */     menu.registerUpdateListener(this::containerChanged);
/*  75 */     this.titleLabelY -= 2;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  80 */     super.init();
/*  81 */     ModelPart modelPart = this.minecraft.getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG);
/*  82 */     this.flag = new BannerFlagModel(modelPart);
/*     */   }
/*     */   
/*     */   private int totalRowCount() {
/*  86 */     return Mth.positiveCeilDiv(this.menu.getSelectablePatterns().size(), 4);
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  91 */     super.extractBackground(graphics, mouseX, mouseY, a);
/*  92 */     int xo = this.leftPos;
/*  93 */     int yo = this.topPos;
/*  94 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*  96 */     Slot bannerSlot = this.menu.getBannerSlot();
/*  97 */     Slot dyeSlot = this.menu.getDyeSlot();
/*  98 */     Slot patternSlot = this.menu.getPatternSlot();
/*  99 */     Slot resultSlot = this.menu.getResultSlot();
/* 100 */     if (!bannerSlot.hasItem())
/* 101 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BANNER_SLOT_SPRITE, xo + bannerSlot.x, yo + bannerSlot.y, 16, 16); 
/* 103 */     if (!dyeSlot.hasItem())
/* 104 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DYE_SLOT_SPRITE, xo + dyeSlot.x, yo + dyeSlot.y, 16, 16); 
/* 106 */     if (!patternSlot.hasItem())
/* 107 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PATTERN_SLOT_SPRITE, xo + patternSlot.x, yo + patternSlot.y, 16, 16); 
/* 110 */     int sy = (int)(41.0F * this.scrollOffs);
/* 111 */     Identifier sprite = isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
/* 112 */     int scrollerX = xo + 119;
/* 113 */     int scrollerY = yo + 13;
/* 114 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, scrollerX, scrollerY + sy, 12, 15);
/* 116 */     if (mouseX >= scrollerX && mouseX < scrollerX + 12 && mouseY >= scrollerY && mouseY < scrollerY + 56)
/* 117 */       if (isScrollBarActive()) {
/* 118 */         graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
/*     */       } else {
/* 120 */         graphics.requestCursor(CursorTypes.NOT_ALLOWED);
/*     */       }  
/* 124 */     if (this.resultBannerPatterns != null && !this.hasMaxPatterns) {
/* 125 */       DyeColor baseColor = ((BannerItem)resultSlot.getItem().getItem()).getColor();
/* 126 */       int x0 = xo + 141;
/* 127 */       int y0 = yo + 8;
/* 128 */       graphics.bannerPattern(this.flag, baseColor, this.resultBannerPatterns, x0, y0, x0 + 20, y0 + 40);
/* 129 */     } else if (this.hasMaxPatterns) {
/* 130 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + resultSlot.x - 5, yo + resultSlot.y - 5, 26, 26);
/*     */     } 
/* 132 */     if (this.displayPatterns) {
/* 133 */       int x = xo + 60;
/* 134 */       int y = yo + 13;
/* 136 */       List<Holder<BannerPattern>> selectablePatterns = this.menu.getSelectablePatterns();
/* 138 */       for (int row = 0; row < 4; row++) {
/* 139 */         for (int column = 0; column < 4; column++) {
/*     */           Identifier buttonSprite;
/* 140 */           int actualRow = row + this.startRow;
/* 141 */           int index = actualRow * 4 + column;
/* 142 */           if (index >= selectablePatterns.size())
/*     */             // Byte code: goto -> 763 
/* 146 */           int posX = x + column * 14;
/* 147 */           int posY = y + row * 14;
/* 149 */           Holder<BannerPattern> pattern = selectablePatterns.get(index);
/* 150 */           boolean isHighlighted = (mouseX >= posX && mouseY >= posY && mouseX < posX + 14 && mouseY < posY + 14);
/* 152 */           if (index == this.menu.getSelectedBannerPatternIndex()) {
/* 153 */             buttonSprite = PATTERN_SELECTED_SPRITE;
/* 154 */           } else if (isHighlighted) {
/* 155 */             buttonSprite = PATTERN_HIGHLIGHTED_SPRITE;
/* 156 */             DyeColor patternColor = (DyeColor)this.dyeStack.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
/* 157 */             graphics.setTooltipForNextFrame((Component)Component.translatable(((BannerPattern)pattern.value()).translationKey() + "." + ((BannerPattern)pattern.value()).translationKey()), mouseX, mouseY);
/* 158 */             graphics.requestCursor(CursorTypes.POINTING_HAND);
/*     */           } else {
/* 160 */             buttonSprite = PATTERN_SPRITE;
/*     */           } 
/* 162 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, buttonSprite, posX, posY, 14, 14);
/* 163 */           TextureAtlasSprite bannerPatternSprite = graphics.getSprite(Sheets.getBannerSprite(pattern));
/* 164 */           extractBannerOnButton(graphics, posX, posY, bannerPatternSprite);
/*     */         } 
/*     */       } 
/*     */     } 
/* 168 */     (Minecraft.getInstance()).gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_3D);
/*     */   }
/*     */   
/*     */   private boolean isScrollBarActive() {
/* 172 */     return (this.displayPatterns && this.menu.getSelectablePatterns().size() > 16);
/*     */   }
/*     */   
/*     */   private void extractBannerOnButton(GuiGraphicsExtractor graphics, int posX, int posY, TextureAtlasSprite bannerPatternSprite) {
/* 176 */     graphics.pose().pushMatrix();
/* 177 */     graphics.pose().translate((posX + 4), (posY + 2));
/* 178 */     float patternU0 = bannerPatternSprite.getU0();
/* 179 */     float patternU1 = patternU0 + (bannerPatternSprite.getU1() - bannerPatternSprite.getU0()) * 21.0F / 64.0F;
/* 180 */     float patternVSpan = bannerPatternSprite.getV1() - bannerPatternSprite.getV0();
/* 181 */     float patternV0 = bannerPatternSprite.getV0() + patternVSpan / 64.0F;
/* 182 */     float patternV1 = patternV0 + patternVSpan * 40.0F / 64.0F;
/* 183 */     int bannerWidth = 5;
/* 184 */     int bannerHeight = 10;
/* 185 */     graphics.fill(0, 0, 5, 10, DyeColor.GRAY.getTextureDiffuseColor());
/* 186 */     graphics.blit(bannerPatternSprite.atlasLocation(), 0, 0, 5, 10, patternU0, patternU1, patternV0, patternV1);
/* 187 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 192 */     if (this.displayPatterns) {
/* 193 */       int xo = this.leftPos + 60;
/* 194 */       int yo = this.topPos + 13;
/* 196 */       for (int row = 0; row < 4; row++) {
/* 197 */         for (int column = 0; column < 4; column++) {
/* 198 */           double xx = event.x() - (xo + column * 14);
/* 199 */           double yy = event.y() - (yo + row * 14);
/* 200 */           int actualRow = row + this.startRow;
/* 201 */           int index = actualRow * 4 + column;
/* 202 */           if (xx >= 0.0D && yy >= 0.0D && xx < 14.0D && yy < 14.0D && this.menu.clickMenuButton((Player)this.minecraft.player, index)) {
/* 203 */             Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI(SoundEvents.UI_LOOM_SELECT_PATTERN, 1.0F));
/* 204 */             this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
/* 205 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 210 */       xo = this.leftPos + 119;
/* 211 */       yo = this.topPos + 9;
/* 212 */       if (event.x() >= xo && event.x() < (xo + 12) && event.y() >= yo && event.y() < (yo + 56))
/* 213 */         this.scrolling = true; 
/*     */     } 
/* 217 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 222 */     int offscreenRows = totalRowCount() - 4;
/* 223 */     if (this.scrolling && this.displayPatterns && offscreenRows > 0) {
/* 224 */       int yscr = this.topPos + 13;
/* 225 */       int yscr2 = yscr + 56;
/* 226 */       this.scrollOffs = ((float)event.y() - yscr - 7.5F) / ((yscr2 - yscr) - 15.0F);
/* 227 */       this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
/* 228 */       this.startRow = Math.max((int)((this.scrollOffs * offscreenRows) + 0.5D), 0);
/* 229 */       return true;
/*     */     } 
/* 231 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 236 */     this.scrolling = false;
/* 237 */     return super.mouseReleased(event);
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 242 */     if (super.mouseScrolled(x, y, scrollX, scrollY))
/* 243 */       return true; 
/* 246 */     int offscreenRows = totalRowCount() - 4;
/* 247 */     if (this.displayPatterns && offscreenRows > 0) {
/* 248 */       float scrolledDelta = (float)scrollY / offscreenRows;
/* 249 */       this.scrollOffs = Mth.clamp(this.scrollOffs - scrolledDelta, 0.0F, 1.0F);
/* 250 */       this.startRow = Math.max((int)(this.scrollOffs * offscreenRows + 0.5F), 0);
/*     */     } 
/* 252 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
/* 257 */     return (mx < xo || my < yo || mx >= (xo + this.imageWidth) || my >= (yo + this.imageHeight));
/*     */   }
/*     */   
/*     */   private void containerChanged() {
/* 261 */     ItemStack resultStack = this.menu.getResultSlot().getItem();
/* 262 */     if (resultStack.isEmpty()) {
/* 263 */       this.resultBannerPatterns = null;
/*     */     } else {
/* 265 */       this.resultBannerPatterns = (BannerPatternLayers)resultStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/*     */     } 
/* 268 */     ItemStack bannerStack = this.menu.getBannerSlot().getItem();
/* 269 */     ItemStack dyeStack = this.menu.getDyeSlot().getItem();
/* 270 */     ItemStack patternStack = this.menu.getPatternSlot().getItem();
/* 272 */     BannerPatternLayers patterns = (BannerPatternLayers)bannerStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 273 */     this.hasMaxPatterns = (patterns.layers().size() >= 6);
/* 275 */     if (this.hasMaxPatterns)
/* 276 */       this.resultBannerPatterns = null; 
/* 279 */     if (!ItemStack.matches(bannerStack, this.bannerStack) || !ItemStack.matches(dyeStack, this.dyeStack) || !ItemStack.matches(patternStack, this.patternStack))
/* 280 */       this.displayPatterns = (!bannerStack.isEmpty() && !dyeStack.isEmpty() && !this.hasMaxPatterns && !this.menu.getSelectablePatterns().isEmpty()); 
/* 283 */     if (this.startRow >= totalRowCount()) {
/* 284 */       this.startRow = 0;
/* 285 */       this.scrollOffs = 0.0F;
/*     */     } 
/* 287 */     this.bannerStack = bannerStack.copy();
/* 288 */     this.dyeStack = dyeStack.copy();
/* 289 */     this.patternStack = patternStack.copy();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\LoomScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
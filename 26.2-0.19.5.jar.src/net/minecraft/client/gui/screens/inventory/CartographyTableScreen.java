/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.state.MapRenderState;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.CartographyTableMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ public class CartographyTableScreen extends AbstractContainerScreen<CartographyTableMenu> {
/*  19 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/error");
/*     */   
/*  20 */   private static final Identifier SCALED_MAP_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/scaled_map");
/*     */   
/*  21 */   private static final Identifier DUPLICATED_MAP_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/duplicated_map");
/*     */   
/*  22 */   private static final Identifier MAP_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/map");
/*     */   
/*  23 */   private static final Identifier LOCKED_SPRITE = Identifier.withDefaultNamespace("container/cartography_table/locked");
/*     */   
/*  24 */   private static final Identifier BG_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/cartography_table.png");
/*     */   
/*  25 */   private final MapRenderState mapRenderState = new MapRenderState();
/*     */   
/*     */   public CartographyTableScreen(CartographyTableMenu menu, Inventory inventory, Component title) {
/*  28 */     super(menu, inventory, title);
/*  29 */     this.titleLabelY -= 2;
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */     MapItemSavedData mapData;
/*  34 */     super.extractBackground(graphics, mouseX, mouseY, a);
/*  35 */     int xo = this.leftPos;
/*  36 */     int yo = this.topPos;
/*  37 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*  39 */     ItemStack additionalItem = this.menu.getSlot(1).getItem();
/*  40 */     boolean isDuplication = additionalItem.is(Items.MAP);
/*  41 */     boolean isScaling = additionalItem.is(Items.PAPER);
/*  42 */     boolean isLocking = additionalItem.is(Items.GLASS_PANE);
/*  44 */     ItemStack map = this.menu.getSlot(0).getItem();
/*  45 */     MapId mapId = (MapId)map.get(DataComponents.MAP_ID);
/*  48 */     boolean locked = false;
/*  49 */     if (mapId != null) {
/*  50 */       mapData = MapItem.getSavedData(mapId, (Level)this.minecraft.level);
/*  51 */       if (mapData != null) {
/*  52 */         if (mapData.locked) {
/*  53 */           locked = true;
/*  55 */           if (isScaling || isLocking)
/*  56 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 35, yo + 31, 28, 21); 
/*     */         } 
/*  59 */         if (isScaling && mapData.scale >= 4) {
/*  60 */           locked = true;
/*  61 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 35, yo + 31, 28, 21);
/*     */         } 
/*     */       } 
/*     */     } else {
/*  65 */       mapData = null;
/*     */     } 
/*  68 */     extractResultingMap(graphics, mapId, mapData, isDuplication, isScaling, isLocking, locked);
/*     */   }
/*     */   
/*     */   private void extractResultingMap(GuiGraphicsExtractor graphics, MapId id, MapItemSavedData data, boolean isDuplication, boolean isScaling, boolean isLocking, boolean locked) {
/*  72 */     int xo = this.leftPos;
/*  73 */     int yo = this.topPos;
/*  75 */     if (isScaling && !locked) {
/*  76 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCALED_MAP_SPRITE, xo + 67, yo + 13, 66, 66);
/*  77 */       extractMap(graphics, id, data, xo + 85, yo + 31, 0.226F);
/*  78 */     } else if (isDuplication) {
/*  79 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DUPLICATED_MAP_SPRITE, xo + 67 + 16, yo + 13, 50, 66);
/*  80 */       extractMap(graphics, id, data, xo + 86, yo + 16, 0.34F);
/*  81 */       graphics.nextStratum();
/*  82 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DUPLICATED_MAP_SPRITE, xo + 67, yo + 13 + 16, 50, 66);
/*  83 */       extractMap(graphics, id, data, xo + 70, yo + 32, 0.34F);
/*  84 */     } else if (isLocking) {
/*  85 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MAP_SPRITE, xo + 67, yo + 13, 66, 66);
/*  86 */       extractMap(graphics, id, data, xo + 71, yo + 17, 0.45F);
/*  88 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LOCKED_SPRITE, xo + 118, yo + 60, 10, 14);
/*     */     } else {
/*  90 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MAP_SPRITE, xo + 67, yo + 13, 66, 66);
/*  91 */       extractMap(graphics, id, data, xo + 71, yo + 17, 0.45F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractMap(GuiGraphicsExtractor graphics, MapId id, MapItemSavedData data, int x, int y, float scale) {
/*  96 */     if (id != null && data != null) {
/*  97 */       graphics.pose().pushMatrix();
/*  98 */       graphics.pose().translate(x, y);
/*  99 */       graphics.pose().scale(scale, scale);
/* 101 */       this.minecraft.getMapRenderer().extractRenderState(id, data, this.mapRenderState);
/* 102 */       graphics.map(this.mapRenderState);
/* 104 */       graphics.pose().popMatrix();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CartographyTableScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
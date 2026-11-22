/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ClientAdvancements;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class AdvancementsScreen extends Screen implements ClientAdvancements.Listener {
/*  28 */   private static final Identifier WINDOW_LOCATION = Identifier.withDefaultNamespace("textures/gui/advancements/window.png");
/*     */   
/*     */   public static final int WINDOW_WIDTH = 252;
/*     */   
/*     */   public static final int WINDOW_HEIGHT = 140;
/*     */   
/*     */   private static final int WINDOW_INSIDE_X = 9;
/*     */   
/*     */   private static final int WINDOW_INSIDE_Y = 18;
/*     */   
/*     */   public static final int WINDOW_INSIDE_WIDTH = 234;
/*     */   
/*     */   public static final int WINDOW_INSIDE_HEIGHT = 113;
/*     */   
/*     */   private static final int WINDOW_TITLE_X = 8;
/*     */   
/*     */   private static final int WINDOW_TITLE_Y = 6;
/*     */   
/*     */   private static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   
/*     */   private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*     */   
/*     */   public static final int BACKGROUND_TILE_WIDTH = 16;
/*     */   
/*     */   public static final int BACKGROUND_TILE_HEIGHT = 16;
/*     */   
/*     */   public static final int BACKGROUND_TILE_COUNT_X = 14;
/*     */   
/*     */   public static final int BACKGROUND_TILE_COUNT_Y = 7;
/*     */   
/*     */   private static final double SCROLL_SPEED = 16.0D;
/*     */   
/*  51 */   private static final Component VERY_SAD_LABEL = (Component)Component.translatable("advancements.sad_label");
/*     */   
/*  52 */   private static final Component NO_ADVANCEMENTS_LABEL = (Component)Component.translatable("advancements.empty");
/*     */   
/*  53 */   private static final Component TITLE = (Component)Component.translatable("gui.advancements");
/*     */   
/*  55 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private int leftPos;
/*     */   
/*     */   private int topPos;
/*     */   
/*     */   private final ClientAdvancements advancements;
/*     */   
/*  63 */   private final Map<AdvancementHolder, AdvancementTab> tabs = Maps.newLinkedHashMap();
/*     */   
/*     */   private AdvancementTab selectedTab;
/*     */   
/*     */   private boolean isScrolling;
/*     */   
/*     */   public AdvancementsScreen(ClientAdvancements advancements) {
/*  68 */     this(advancements, (Screen)null);
/*     */   }
/*     */   
/*     */   public AdvancementsScreen(ClientAdvancements advancements, Screen lastScreen) {
/*  72 */     super(TITLE);
/*  73 */     this.advancements = advancements;
/*  74 */     this.lastScreen = lastScreen;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  79 */     this.layout.addTitleHeader(TITLE, this.font);
/*  80 */     this.tabs.clear();
/*  81 */     this.selectedTab = null;
/*  82 */     this.advancements.setListener(this);
/*  83 */     if (this.selectedTab == null && !this.tabs.isEmpty()) {
/*  84 */       AdvancementTab firstTab = this.tabs.values().iterator().next();
/*  85 */       this.advancements.setSelectedTab(firstTab.getRootNode().holder(), true);
/*     */     } else {
/*  88 */       this.advancements.setSelectedTab((this.selectedTab == null) ? null : this.selectedTab.getRootNode().holder(), true);
/*     */     } 
/*  90 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*  92 */     AdvancementsScreen advancementsScreen = this;
/*  92 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  93 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  98 */     this.leftPos = (this.width - 252) / 2;
/*  99 */     this.topPos = (this.height - 140) / 2;
/* 100 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 105 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public void removed() {
/* 110 */     this.advancements.setListener(null);
/* 111 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 112 */     if (connection != null)
/* 113 */       connection.send((Packet)ServerboundSeenAdvancementsPacket.closedScreen()); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 119 */     super.tick();
/* 120 */     if (this.selectedTab != null) {
/* 121 */       int mouseX = (int)this.minecraft.mouseHandler.getScaledXPos(this.minecraft.getWindow());
/* 122 */       int mouseY = (int)this.minecraft.mouseHandler.getScaledYPos(this.minecraft.getWindow());
/* 123 */       this.selectedTab.tick(mouseX - this.leftPos - 9, mouseY - this.topPos - 18);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 129 */     if (event.button() == 0) {
/* 130 */       int xo = (this.width - 252) / 2;
/* 131 */       int yo = (this.height - 140) / 2;
/* 133 */       for (AdvancementTab tab : this.tabs.values()) {
/* 134 */         if (tab.isMouseOver(xo, yo, event.x(), event.y())) {
/* 135 */           this.advancements.setSelectedTab(tab.getRootNode().holder(), true);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 140 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 145 */     if (this.minecraft.options.keyAdvancements.matches(event)) {
/* 146 */       this.minecraft.gui.setScreen(null);
/* 147 */       this.minecraft.mouseHandler.grabMouse();
/* 148 */       return true;
/*     */     } 
/* 150 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 155 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 157 */     graphics.nextStratum();
/* 158 */     extractInside(graphics);
/* 159 */     graphics.nextStratum();
/* 160 */     extractWindow(graphics, mouseX, mouseY);
/* 161 */     if (this.isScrolling && this.selectedTab != null)
/* 162 */       if (this.selectedTab.canScrollHorizontally() && this.selectedTab.canScrollVertically()) {
/* 163 */         graphics.requestCursor(CursorTypes.RESIZE_ALL);
/* 164 */       } else if (this.selectedTab.canScrollHorizontally()) {
/* 165 */         graphics.requestCursor(CursorTypes.RESIZE_EW);
/* 166 */       } else if (this.selectedTab.canScrollVertically()) {
/* 167 */         graphics.requestCursor(CursorTypes.RESIZE_NS);
/*     */       }  
/* 170 */     extractTooltips(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 175 */     if (event.button() != 0) {
/* 176 */       this.isScrolling = false;
/* 177 */       return false;
/*     */     } 
/* 180 */     if (!this.isScrolling) {
/* 181 */       this.isScrolling = true;
/* 182 */     } else if (this.selectedTab != null) {
/* 183 */       this.selectedTab.scroll(dx, dy);
/*     */     } 
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 190 */     this.isScrolling = false;
/* 191 */     return super.mouseReleased(event);
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 196 */     if (this.selectedTab != null) {
/* 197 */       this.selectedTab.scroll(scrollX * 16.0D, scrollY * 16.0D);
/* 198 */       return true;
/*     */     } 
/* 200 */     return false;
/*     */   }
/*     */   
/*     */   private void extractInside(GuiGraphicsExtractor graphics) {
/* 204 */     AdvancementTab tab = this.selectedTab;
/* 205 */     if (tab == null) {
/* 206 */       graphics.fill(this.leftPos + 9, this.topPos + 18, this.leftPos + 9 + 234, this.topPos + 18 + 113, -16777216);
/* 207 */       int midX = this.leftPos + 9 + 117;
/* 208 */       Objects.requireNonNull(this.font);
/* 208 */       graphics.centeredText(this.font, NO_ADVANCEMENTS_LABEL, midX, this.topPos + 18 + 56 - 9 / 2, -1);
/* 209 */       Objects.requireNonNull(this.font);
/* 209 */       graphics.centeredText(this.font, VERY_SAD_LABEL, midX, this.topPos + 18 + 113 - 9, -1);
/*     */       return;
/*     */     } 
/* 213 */     tab.extractContents(graphics, this.leftPos + 9, this.topPos + 18);
/*     */   }
/*     */   
/*     */   public void extractWindow(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 217 */     graphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_LOCATION, this.leftPos, this.topPos, 0.0F, 0.0F, 252, 140, 256, 256);
/* 218 */     if (this.tabs.size() > 1) {
/* 219 */       for (AdvancementTab tab : this.tabs.values())
/* 220 */         tab.extractTab(graphics, this.leftPos, this.topPos, mouseX, mouseY, (tab == this.selectedTab)); 
/* 223 */       for (AdvancementTab tab : this.tabs.values())
/* 224 */         tab.extractIcon(graphics, this.leftPos, this.topPos); 
/*     */     } 
/* 227 */     graphics.text(this.font, (this.selectedTab != null) ? this.selectedTab.getTitle() : TITLE, this.leftPos + 8, this.topPos + 6, -12566464, false);
/*     */   }
/*     */   
/*     */   private void extractTooltips(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 231 */     if (this.selectedTab != null) {
/* 232 */       graphics.pose().pushMatrix();
/* 233 */       graphics.pose().translate((this.leftPos + 9), (this.topPos + 18));
/* 235 */       graphics.nextStratum();
/* 236 */       this.selectedTab.extractTooltips(graphics, this.leftPos, this.topPos);
/* 238 */       graphics.pose().popMatrix();
/*     */     } 
/* 241 */     if (this.tabs.size() > 1)
/* 242 */       for (AdvancementTab tab : this.tabs.values()) {
/* 243 */         if (tab.isMouseOver(this.leftPos, this.topPos, mouseX, mouseY))
/* 244 */           graphics.setTooltipForNextFrame(this.font, tab.getTitle(), mouseX, mouseY); 
/*     */       }  
/*     */   }
/*     */   
/*     */   public void onAddAdvancementRoot(AdvancementNode root) {
/* 252 */     AdvancementTab tab = AdvancementTab.create(this.minecraft, this, this.tabs.size(), root);
/* 253 */     if (tab == null)
/*     */       return; 
/* 256 */     this.tabs.put(root.holder(), tab);
/*     */   }
/*     */   
/*     */   public void onRemoveAdvancementRoot(AdvancementNode root) {}
/*     */   
/*     */   public void onAddAdvancementTask(AdvancementNode task) {
/* 265 */     AdvancementTab tab = getTab(task);
/* 266 */     if (tab != null)
/* 267 */       tab.addAdvancement(task); 
/*     */   }
/*     */   
/*     */   public void onRemoveAdvancementTask(AdvancementNode task) {}
/*     */   
/*     */   public void onUpdateAdvancementProgress(AdvancementNode advancement, AdvancementProgress progress) {
/* 277 */     AdvancementWidget widget = getAdvancementWidget(advancement);
/* 278 */     if (widget != null)
/* 279 */       widget.setProgress(progress); 
/*     */   }
/*     */   
/*     */   public void onSelectedTabChanged(AdvancementHolder selectedTab) {
/* 285 */     this.selectedTab = this.tabs.get(selectedTab);
/*     */   }
/*     */   
/*     */   public void onAdvancementsCleared() {
/* 290 */     this.tabs.clear();
/* 291 */     this.selectedTab = null;
/*     */   }
/*     */   
/*     */   public AdvancementWidget getAdvancementWidget(AdvancementNode node) {
/* 295 */     AdvancementTab tab = getTab(node);
/* 296 */     return (tab == null) ? null : tab.getWidget(node.holder());
/*     */   }
/*     */   
/*     */   private AdvancementTab getTab(AdvancementNode node) {
/* 301 */     AdvancementNode root = node.root();
/* 302 */     return this.tabs.get(root.holder());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\advancements\AdvancementsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
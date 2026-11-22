/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.DisplayInfo;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class AdvancementTab {
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final AdvancementsScreen screen;
/*     */   
/*     */   private final AdvancementTabType type;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   private final AdvancementNode rootNode;
/*     */   
/*     */   private final DisplayInfo display;
/*     */   
/*     */   private final ItemStack icon;
/*     */   
/*     */   private final Component title;
/*     */   
/*     */   private final AdvancementWidget root;
/*     */   
/*  32 */   private final Map<AdvancementHolder, AdvancementWidget> widgets = Maps.newLinkedHashMap();
/*     */   
/*     */   private double scrollX;
/*     */   
/*     */   private double scrollY;
/*     */   
/*  35 */   private int minX = Integer.MAX_VALUE;
/*     */   
/*  36 */   private int minY = Integer.MAX_VALUE;
/*     */   
/*  37 */   private int maxX = Integer.MIN_VALUE;
/*     */   
/*  38 */   private int maxY = Integer.MIN_VALUE;
/*     */   
/*     */   private float fade;
/*     */   
/*     */   private boolean centered;
/*     */   
/*     */   private AdvancementWidget hovered;
/*     */   
/*     */   public AdvancementTab(Minecraft minecraft, AdvancementsScreen screen, AdvancementTabType type, int index, AdvancementNode rootNode, DisplayInfo display) {
/*  44 */     this.minecraft = minecraft;
/*  45 */     this.screen = screen;
/*  46 */     this.type = type;
/*  47 */     this.index = index;
/*  48 */     this.rootNode = rootNode;
/*  49 */     this.display = display;
/*  50 */     this.icon = display.getIcon().create();
/*  51 */     this.title = display.getTitle();
/*  52 */     this.root = new AdvancementWidget(this, minecraft, rootNode, display);
/*  53 */     addWidget(this.root, rootNode.holder());
/*     */   }
/*     */   
/*     */   public AdvancementTabType getType() {
/*  57 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getIndex() {
/*  61 */     return this.index;
/*     */   }
/*     */   
/*     */   public AdvancementNode getRootNode() {
/*  65 */     return this.rootNode;
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/*  69 */     return this.title;
/*     */   }
/*     */   
/*     */   public DisplayInfo getDisplay() {
/*  73 */     return this.display;
/*     */   }
/*     */   
/*     */   public void tick(int relativeMouseX, int relativeMouseY) {
/*  77 */     boolean hovering = false;
/*  78 */     if (relativeMouseX > 0 && relativeMouseX < 234 && relativeMouseY > 0 && relativeMouseY < 113) {
/*  79 */       int intScrollX = Mth.floor(this.scrollX);
/*  80 */       int intScrollY = Mth.floor(this.scrollY);
/*  81 */       for (AdvancementWidget widget : this.widgets.values()) {
/*  82 */         if (widget.isMouseOver(intScrollX, intScrollY, relativeMouseX, relativeMouseY)) {
/*  83 */           hovering = true;
/*  84 */           this.hovered = widget;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  90 */     if (hovering) {
/*  91 */       this.fade = Mth.clamp(this.fade + 0.06F, 0.0F, 0.3F);
/*     */     } else {
/*  93 */       this.fade = Mth.clamp(this.fade - 0.12F, 0.0F, 1.0F);
/*  94 */       if (this.hovered != null)
/*  95 */         this.hovered = null; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractTab(GuiGraphicsExtractor graphics, int xo, int yo, int mouseX, int mouseY, boolean selected) {
/* 101 */     int tabX = xo + this.type.getX(this.index);
/* 102 */     int tabY = yo + this.type.getY(this.index);
/* 103 */     this.type.extractRenderState(graphics, tabX, tabY, selected, this.index);
/* 104 */     if (!selected && mouseX > tabX && mouseY > tabY && mouseX < tabX + this.type.getWidth() && mouseY < tabY + this.type.getHeight())
/* 105 */       graphics.requestCursor(CursorTypes.POINTING_HAND); 
/*     */   }
/*     */   
/*     */   public void extractIcon(GuiGraphicsExtractor graphics, int xo, int yo) {
/* 110 */     this.type.extractIcon(graphics, xo, yo, this.index, this.icon);
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int windowLeft, int windowTop) {
/* 114 */     if (!this.centered) {
/* 115 */       this.scrollX = (117 - (this.maxX + this.minX) / 2);
/* 116 */       this.scrollY = (56 - (this.maxY + this.minY) / 2);
/* 117 */       this.centered = true;
/*     */     } 
/* 120 */     graphics.enableScissor(windowLeft, windowTop, windowLeft + 234, windowTop + 113);
/* 121 */     graphics.pose().pushMatrix();
/* 122 */     graphics.pose().translate(windowLeft, windowTop);
/* 124 */     Identifier background = this.display.getBackground().map(ClientAsset.ResourceTexture::texturePath).orElse(TextureManager.INTENTIONAL_MISSING_TEXTURE);
/* 125 */     int intScrollX = Mth.floor(this.scrollX);
/* 126 */     int intScrollY = Mth.floor(this.scrollY);
/* 127 */     int left = intScrollX % 16;
/* 128 */     int top = intScrollY % 16;
/* 130 */     for (int x = -1; x <= 15; x++) {
/* 131 */       for (int y = -1; y <= 8; y++)
/* 132 */         graphics.blit(RenderPipelines.GUI_TEXTURED, background, left + 16 * x, top + 16 * y, 0.0F, 0.0F, 16, 16, 16, 16); 
/*     */     } 
/* 136 */     this.root.extractConnectivity(graphics, intScrollX, intScrollY, true);
/* 137 */     this.root.extractConnectivity(graphics, intScrollX, intScrollY, false);
/* 138 */     this.root.extractRenderState(graphics, intScrollX, intScrollY);
/* 140 */     graphics.pose().popMatrix();
/* 141 */     graphics.disableScissor();
/*     */   }
/*     */   
/*     */   public void extractTooltips(GuiGraphicsExtractor graphics, int xo, int yo) {
/* 145 */     graphics.fill(0, 0, 234, 113, Mth.floor(this.fade * 255.0F) << 24);
/* 147 */     if (this.hovered != null) {
/* 148 */       int intScrollX = Mth.floor(this.scrollX);
/* 149 */       int intScrollY = Mth.floor(this.scrollY);
/* 150 */       this.hovered.extractHover(graphics, intScrollX, intScrollY, this.fade, xo, yo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(int xo, int yo, double mx, double my) {
/* 155 */     return this.type.isMouseOver(xo, yo, this.index, mx, my);
/*     */   }
/*     */   
/*     */   public static AdvancementTab create(Minecraft minecraft, AdvancementsScreen screen, int index, AdvancementNode root) {
/* 159 */     Optional<DisplayInfo> display = root.advancement().display();
/* 160 */     if (display.isEmpty())
/* 161 */       return null; 
/* 163 */     for (AdvancementTabType type : AdvancementTabType.values()) {
/* 164 */       if (index >= type.getMax()) {
/* 165 */         index -= type.getMax();
/*     */       } else {
/* 168 */         return new AdvancementTab(minecraft, screen, type, index, root, display.get());
/*     */       } 
/*     */     } 
/* 170 */     return null;
/*     */   }
/*     */   
/*     */   public void scroll(double x, double y) {
/* 174 */     if (canScrollHorizontally())
/* 175 */       this.scrollX = Mth.clamp(this.scrollX + x, -(this.maxX - 234), 0.0D); 
/* 177 */     if (canScrollVertically())
/* 178 */       this.scrollY = Mth.clamp(this.scrollY + y, -(this.maxY - 113), 0.0D); 
/*     */   }
/*     */   
/*     */   public boolean canScrollHorizontally() {
/* 183 */     return (this.maxX - this.minX > 234);
/*     */   }
/*     */   
/*     */   public boolean canScrollVertically() {
/* 187 */     return (this.maxY - this.minY > 113);
/*     */   }
/*     */   
/*     */   public void addAdvancement(AdvancementNode node) {
/* 191 */     Optional<DisplayInfo> display = node.advancement().display();
/* 192 */     if (display.isEmpty())
/*     */       return; 
/* 196 */     AdvancementWidget widget = new AdvancementWidget(this, this.minecraft, node, display.get());
/* 197 */     addWidget(widget, node.holder());
/*     */   }
/*     */   
/*     */   private void addWidget(AdvancementWidget widget, AdvancementHolder advancement) {
/* 201 */     this.widgets.put(advancement, widget);
/* 202 */     int x0 = widget.getX();
/* 203 */     int x1 = x0 + 28;
/* 204 */     int y0 = widget.getY();
/* 205 */     int y1 = y0 + 27;
/* 206 */     this.minX = Math.min(this.minX, x0);
/* 207 */     this.maxX = Math.max(this.maxX, x1);
/* 208 */     this.minY = Math.min(this.minY, y0);
/* 209 */     this.maxY = Math.max(this.maxY, y1);
/* 211 */     for (AdvancementWidget other : this.widgets.values())
/* 212 */       other.attachToParent(); 
/*     */   }
/*     */   
/*     */   public AdvancementWidget getWidget(AdvancementHolder advancement) {
/* 217 */     return this.widgets.get(advancement);
/*     */   }
/*     */   
/*     */   public AdvancementsScreen getScreen() {
/* 221 */     return this.screen;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\advancements\AdvancementTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
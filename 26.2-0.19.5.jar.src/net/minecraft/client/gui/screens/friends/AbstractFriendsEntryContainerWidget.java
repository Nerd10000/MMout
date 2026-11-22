/*     */ package net.minecraft.client.gui.screens.friends;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractContainerWidget;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PlayerFaceWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ 
/*     */ abstract class AbstractFriendsEntryContainerWidget extends AbstractContainerWidget {
/*     */   static final int SPRITE_TEXTURE_SIZE = 18;
/*     */   
/*     */   static final int BUTTON_SIZE = 20;
/*     */   
/*     */   static final int FACE_SIZE = 24;
/*     */   
/*     */   static final int PADDING = 4;
/*     */   
/*     */   private static final int BACKGROUND_MARGIN = 4;
/*     */   
/*     */   protected final Minecraft minecraft;
/*     */   
/*     */   protected final FriendsOverlayScreen screen;
/*     */   
/*     */   protected final PlayerFaceWidget playerFaceWidget;
/*     */   
/*     */   protected final StringWidget nameWidget;
/*     */   
/*     */   protected final String playerName;
/*     */   
/*     */   protected final UUID playerId;
/*     */   
/*     */   protected final boolean showingStatus;
/*     */   
/*  40 */   private final List<AbstractWidget> children = new ArrayList<>();
/*     */   
/*     */   public AbstractFriendsEntryContainerWidget(Minecraft minecraft, FriendsOverlayScreen screen, int x, int y, int width, int height, PlayerSocialManager.PlayerData playerData) {
/*  43 */     this(minecraft, screen, x, y, width, height, playerData, false);
/*     */   }
/*     */   
/*     */   public AbstractFriendsEntryContainerWidget(Minecraft minecraft, FriendsOverlayScreen screen, int x, int y, int width, int height, PlayerSocialManager.PlayerData playerData, boolean showingStatus) {
/*  47 */     super(x, y, width, height, (Component)Component.empty());
/*  48 */     this.minecraft = minecraft;
/*  49 */     this.screen = screen;
/*  50 */     this.playerName = playerData.name();
/*  51 */     this.playerId = playerData.id();
/*  52 */     this.playerFaceWidget = new PlayerFaceWidget(24, ResolvableProfile.createUnresolved(this.playerId));
/*  53 */     this.nameWidget = new StringWidget((Component)Component.literal(this.playerName), minecraft.font);
/*  54 */     addChild((AbstractWidget)this.playerFaceWidget);
/*  55 */     addChild((AbstractWidget)this.nameWidget);
/*  56 */     this.showingStatus = showingStatus;
/*     */   }
/*     */   
/*     */   UUID playerId() {
/*  64 */     return this.playerId;
/*     */   }
/*     */   
/*     */   static Button.CreateNarration getSpriteIconNarration(Component actionDescription) {
/*  71 */     return paramSupplier -> Component.translatable("narrator.select", new Object[] { actionDescription });
/*     */   }
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {
/*  76 */     output.add(NarratedElementType.TITLE, this.nameWidget.getMessage());
/*  77 */     GuiEventListener focusedChild = getFocused();
/*  78 */     if (focusedChild instanceof AbstractWidget) {
/*  78 */       AbstractWidget focusedWidget = (AbstractWidget)focusedChild;
/*  79 */       focusedWidget.updateNarration(output.nest());
/*     */     } else {
/*  81 */       output.add(NarratedElementType.USAGE, getEntryNarration());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<? extends NarratableEntry> getNarratables() {
/*  87 */     List<NarratableEntry> narratables = new ArrayList<>(this.children.size() + 1);
/*  88 */     narratables.addAll(this.children);
/*  89 */     narratables.add(this);
/*  90 */     return narratables;
/*     */   }
/*     */   
/*     */   protected final void addChild(AbstractWidget child) {
/*  94 */     this.children.add(child);
/*     */   }
/*     */   
/*     */   protected final void removeChild(AbstractWidget child) {
/*  98 */     if (this.children.remove(child) && getFocused() == child)
/*  99 */       setFocused(null); 
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 105 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   protected int contentHeight() {
/* 110 */     return this.height;
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 115 */     if (((Boolean)this.minecraft.options.highContrast().get()).booleanValue())
/* 116 */       graphics.fill(getX() - 4, getY(), getX() + getWidth() + 4, getY() + getHeight(), -16777216); 
/* 118 */     this.playerFaceWidget.setPosition(getX(), getY() + (getHeight() - this.playerFaceWidget.getHeight()) / 2);
/* 119 */     this.playerFaceWidget.extractRenderState(graphics, mouseX, mouseY, a);
/* 120 */     int nameY = getY() + getHeight() / (this.showingStatus ? 3 : 2) - this.nameWidget.getHeight() / 2;
/* 121 */     this.nameWidget.setPosition(this.playerFaceWidget.getRight() + 4, nameY);
/* 122 */     this.nameWidget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   abstract void disable();
/*     */   
/*     */   protected abstract Component getEntryNarration();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\AbstractFriendsEntryContainerWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
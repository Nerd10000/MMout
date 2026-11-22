/*     */ package net.minecraft.client.gui.screens.multiplayer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.ScrollableLayout;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.multiplayer.chat.ChatAbilities;
/*     */ import net.minecraft.client.multiplayer.chat.ChatRestriction;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.server.permissions.Permission;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ 
/*     */ public class RestrictionsScreen extends Screen {
/*  26 */   private static final Component TITLE = (Component)Component.translatable("restrictions_screen.title");
/*     */   
/*     */   private final Screen previousScreen;
/*     */   
/*     */   private final ChatAbilities chatAbilities;
/*     */   
/*  31 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private ScrollableLayout bodyScroll;
/*     */   
/*     */   public RestrictionsScreen(Screen previousScreen, ChatAbilities chatAbilities) {
/*  35 */     super(TITLE);
/*  36 */     this.previousScreen = previousScreen;
/*  37 */     this.chatAbilities = chatAbilities;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  42 */     this.layout.addToHeader((LayoutElement)new StringWidget(TITLE, this.font), LayoutSettings::alignHorizontallyCenter);
/*  44 */     LinearLayout body = LinearLayout.vertical();
/*  45 */     body.defaultCellSetting().alignHorizontallyCenter();
/*  48 */     int textBoxWidth = 250;
/*  50 */     this.chatAbilities.restrictions().forEach(restriction -> {
/*     */           body.addChild((LayoutElement)FocusableTextWidget.builder(ComponentUtils.mergeStyles(restriction.display(), Style.EMPTY.withBold(Boolean.valueOf(true)).withColor(ChatFormatting.RED)), this.font).maxWidth(250).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build().setCentered(true));
/*     */           restriction.action().ifPresent(());
/*     */         });
/*  66 */     if (this.chatAbilities.hasAnyRestrictions())
/*  67 */       body.addChild((LayoutElement)new SpacerElement(10, 10)); 
/*  70 */     List<Component> permissionEntries = new ArrayList<>();
/*  71 */     permissionEntries.add(createPermissionStatus(this.chatAbilities, Permissions.CHAT_SEND_MESSAGES, "send_messages"));
/*  72 */     permissionEntries.add(createPermissionStatus(this.chatAbilities, Permissions.CHAT_SEND_COMMANDS, "send_commands"));
/*  73 */     permissionEntries.add(createPermissionStatus(this.chatAbilities, Permissions.CHAT_RECEIVE_SYSTEM_MESSAGES, "receive_system_messages"));
/*  74 */     permissionEntries.add(createPermissionStatus(this.chatAbilities, Permissions.CHAT_RECEIVE_PLAYER_MESSAGES, "receive_player_messages"));
/*  76 */     Component message = CommonComponents.joinLines(permissionEntries);
/*  77 */     body.addChild((LayoutElement)FocusableTextWidget.builder(message, this.font)
/*  78 */         .maxWidth(250)
/*  79 */         .alwaysShowBorder(false)
/*  80 */         .backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS)
/*  81 */         .build()
/*  82 */         .setCentered(true));
/*  85 */     this.bodyScroll = new ScrollableLayout(this.minecraft, (Layout)body, this.layout.getContentHeight());
/*  86 */     this.layout.addToContents((LayoutElement)this.bodyScroll);
/*  88 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  89 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, paramButton -> onClose()).width(200).build());
/*  91 */     RestrictionsScreen restrictionsScreen = this;
/*  91 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  92 */     repositionElements();
/*     */   }
/*     */   
/*     */   private static Component createPermissionStatus(ChatAbilities abilities, Permission permission, String permissionId) {
/*  96 */     boolean isAllowed = abilities.permissions().hasPermission(permission);
/*  97 */     String prefix = "restrictions_screen.permission." + permissionId;
/*  98 */     if (isAllowed)
/*  99 */       return (Component)Component.translatable(prefix + ".allowed").withStyle(ChatFormatting.GREEN); 
/* 101 */     return (Component)Component.translatable(prefix + ".denied").withStyle(ChatFormatting.RED);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 107 */     this.minecraft.gui.setScreen(this.previousScreen);
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 112 */     this.bodyScroll.arrangeElements();
/* 113 */     this.bodyScroll.setMaxHeight(this.layout.getContentHeight());
/* 114 */     this.layout.arrangeElements();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\multiplayer\RestrictionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
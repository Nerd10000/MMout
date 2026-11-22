/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.Ops;
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsConfirmScreen;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsPlayersTab extends GridLayoutTab implements RealmsConfigurationTab {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   public static final Component TITLE = (Component)Component.translatable("mco.configure.world.players.title");
/*     */   
/*  37 */   private static final Component QUESTION_TITLE = (Component)Component.translatable("mco.question");
/*     */   
/*     */   private static final int PADDING = 8;
/*     */   
/*     */   private final RealmsConfigureWorldScreen configurationScreen;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private RealmsServer serverData;
/*     */   
/*     */   private final InvitedObjectSelectionList invitedList;
/*     */   
/*     */   public RealmsPlayersTab(RealmsConfigureWorldScreen configurationScreen, Minecraft minecraft, RealmsServer serverData) {
/*  48 */     super(TITLE);
/*  49 */     this.configurationScreen = configurationScreen;
/*  50 */     this.minecraft = minecraft;
/*  51 */     this.font = configurationScreen.getFont();
/*  52 */     this.serverData = serverData;
/*  53 */     GridLayout.RowHelper helper = this.layout.spacing(8).createRowHelper(1);
/*  54 */     this.invitedList = (InvitedObjectSelectionList)helper.addChild((LayoutElement)new InvitedObjectSelectionList(configurationScreen.width, calculateListHeight()), LayoutSettings.defaults().alignVerticallyTop().alignHorizontallyCenter());
/*  55 */     helper.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.configure.world.buttons.invite"), paramButton -> minecraft.gui.setScreen((Screen)new RealmsInviteScreen(configurationScreen, serverData)))
/*     */         
/*  57 */         .build(), LayoutSettings.defaults().alignVerticallyBottom().alignHorizontallyCenter());
/*  59 */     updateData(serverData);
/*     */   }
/*     */   
/*     */   public int calculateListHeight() {
/*  63 */     return this.configurationScreen.getContentHeight() - 20 - 16;
/*     */   }
/*     */   
/*     */   public void doLayout(ScreenRectangle screenRectangle) {
/*  68 */     super.doLayout(screenRectangle);
/*  69 */     this.invitedList.updateSizeAndPosition(this.configurationScreen.width, calculateListHeight(), this.invitedList.getX(), this.invitedList.getY());
/*     */   }
/*     */   
/*     */   public void updateData(RealmsServer serverData) {
/*  74 */     this.serverData = serverData;
/*  75 */     this.invitedList.updateList(serverData);
/*     */   }
/*     */   
/*     */   private class InvitedObjectSelectionList extends ContainerObjectSelectionList<Entry> {
/*     */     private static final int PLAYER_ENTRY_HEIGHT = 36;
/*     */     
/*     */     public InvitedObjectSelectionList(int width, int height) {
/*  82 */       super(Minecraft.getInstance(), width, height, RealmsPlayersTab.this.configurationScreen.getHeaderHeight(), 36);
/*     */     }
/*     */     
/*     */     private void updateList(RealmsServer serverData) {
/*  86 */       clearEntries();
/*  87 */       populateList(serverData);
/*     */     }
/*     */     
/*     */     private void populateList(RealmsServer serverData) {
/*  91 */       RealmsPlayersTab.HeaderEntry entry = new RealmsPlayersTab.HeaderEntry();
/*  92 */       Objects.requireNonNull(RealmsPlayersTab.this.font);
/*  92 */       addEntry((AbstractSelectionList.Entry)entry, entry.height(9));
/*  93 */       for (RealmsPlayersTab.PlayerEntry newChild : serverData.players.stream().map(x$0 -> new RealmsPlayersTab.PlayerEntry(x$0)).toList())
/*  94 */         addEntry((AbstractSelectionList.Entry)newChild); 
/*     */     }
/*     */     
/*     */     protected void extractListBackground(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     protected void extractListSeparators(GuiGraphicsExtractor graphics) {}
/*     */     
/*     */     public int getRowWidth() {
/* 108 */       return 300;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {}
/*     */   
/*     */   private class PlayerEntry extends Entry {
/*     */     protected static final int SKIN_FACE_SIZE = 32;
/*     */     
/* 117 */     private static final Component NORMAL_USER_TEXT = (Component)Component.translatable("mco.configure.world.invites.normal.tooltip");
/*     */     
/* 118 */     private static final Component OP_TEXT = (Component)Component.translatable("mco.configure.world.invites.ops.tooltip");
/*     */     
/* 119 */     private static final Component REMOVE_TEXT = (Component)Component.translatable("mco.configure.world.invites.remove.tooltip");
/*     */     
/* 121 */     private static final Identifier MAKE_OP_SPRITE = Identifier.withDefaultNamespace("player_list/make_operator");
/*     */     
/* 122 */     private static final Identifier REMOVE_OP_SPRITE = Identifier.withDefaultNamespace("player_list/remove_operator");
/*     */     
/* 123 */     private static final Identifier REMOVE_PLAYER_SPRITE = Identifier.withDefaultNamespace("player_list/remove_player");
/*     */     
/*     */     private static final int ICON_WIDTH = 8;
/*     */     
/*     */     private static final int ICON_HEIGHT = 7;
/*     */     
/*     */     private final PlayerInfo playerInfo;
/*     */     
/*     */     private final Button removeButton;
/*     */     
/*     */     private final Button makeOpButton;
/*     */     
/*     */     private final Button removeOpButton;
/*     */     
/*     */     public PlayerEntry(PlayerInfo playerInfo) {
/* 134 */       this.playerInfo = playerInfo;
/* 135 */       int index = RealmsPlayersTab.this.serverData.players.indexOf(this.playerInfo);
/* 137 */       this
/*     */         
/* 144 */         .makeOpButton = (Button)SpriteIconButton.builder(NORMAL_USER_TEXT, button -> op(index), false).sprite(MAKE_OP_SPRITE, 8, 7).width(16 + RealmsPlayersTab.this.configurationScreen.getFont().width((FormattedText)NORMAL_USER_TEXT)).narration(defaultNarrationSupplier -> CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("mco.invited.player.narration", new Object[] { playerInfo.name }), defaultNarrationSupplier.get(), (Component)Component.translatable("narration.cycle_button.usage.focused", new Object[] { OP_TEXT }) })).build();
/* 146 */       this
/*     */         
/* 153 */         .removeOpButton = (Button)SpriteIconButton.builder(OP_TEXT, button -> deop(index), false).sprite(REMOVE_OP_SPRITE, 8, 7).width(16 + RealmsPlayersTab.this.configurationScreen.getFont().width((FormattedText)OP_TEXT)).narration(defaultNarrationSupplier -> CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("mco.invited.player.narration", new Object[] { playerInfo.name }), defaultNarrationSupplier.get(), (Component)Component.translatable("narration.cycle_button.usage.focused", new Object[] { NORMAL_USER_TEXT }) })).build();
/* 155 */       this
/*     */         
/* 161 */         .removeButton = (Button)SpriteIconButton.builder(REMOVE_TEXT, button -> uninvite(index), false).sprite(REMOVE_PLAYER_SPRITE, 8, 7).width(16 + RealmsPlayersTab.this.configurationScreen.getFont().width((FormattedText)REMOVE_TEXT)).narration(defaultNarrationSupplier -> CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("mco.invited.player.narration", new Object[] { playerInfo.name }), defaultNarrationSupplier.get() })).build();
/* 163 */       updateOpButtons();
/*     */     }
/*     */     
/*     */     private void op(int index) {
/* 167 */       UUID selectedInvite = ((PlayerInfo)RealmsPlayersTab.this.serverData.players.get(index)).uuid;
/* 168 */       RealmsUtil.supplyAsync(client -> client.op(RealmsPlayersTab.this.serverData.id, selectedInvite), e -> RealmsPlayersTab.LOGGER.error("Couldn't op the user", (Throwable)e))
/*     */         
/* 171 */         .thenAcceptAsync(ops -> {
/*     */             updateOps(ops);
/*     */             updateOpButtons();
/*     */             setFocused((GuiEventListener)this.removeOpButton);
/*     */           }(Executor)RealmsPlayersTab.this.minecraft);
/*     */     }
/*     */     
/*     */     private void deop(int index) {
/* 179 */       UUID selectedInvite = ((PlayerInfo)RealmsPlayersTab.this.serverData.players.get(index)).uuid;
/* 180 */       RealmsUtil.supplyAsync(client -> client.deop(RealmsPlayersTab.this.serverData.id, selectedInvite), e -> RealmsPlayersTab.LOGGER.error("Couldn't deop the user", (Throwable)e))
/*     */         
/* 183 */         .thenAcceptAsync(ops -> {
/*     */             updateOps(ops);
/*     */             updateOpButtons();
/*     */             setFocused((GuiEventListener)this.makeOpButton);
/*     */           }(Executor)RealmsPlayersTab.this.minecraft);
/*     */     }
/*     */     
/*     */     private void uninvite(int index) {
/* 191 */       if (index >= 0 && index < RealmsPlayersTab.this.serverData.players.size()) {
/* 192 */         PlayerInfo playerInfo = RealmsPlayersTab.this.serverData.players.get(index);
/* 203 */         RealmsConfirmScreen confirmScreen = new RealmsConfirmScreen(result -> {
/*     */               if (result) {
/*     */                 RealmsUtil.runAsync((), ());
/*     */                 RealmsPlayersTab.this.serverData.players.remove(index);
/*     */                 RealmsPlayersTab.this.updateData(RealmsPlayersTab.this.serverData);
/*     */               } 
/*     */               RealmsPlayersTab.this.minecraft.gui.setScreen((Screen)RealmsPlayersTab.this.configurationScreen);
/* 203 */             }RealmsPlayersTab.QUESTION_TITLE, (Component)Component.translatable("mco.configure.world.uninvite.player", new Object[] { playerInfo.name }));
/* 204 */         RealmsPlayersTab.this.minecraft.gui.setScreen((Screen)confirmScreen);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void updateOps(Ops ops) {
/* 209 */       for (PlayerInfo playerInfo : RealmsPlayersTab.this.serverData.players)
/* 210 */         playerInfo.operator = ops.ops().contains(playerInfo.name); 
/*     */     }
/*     */     
/*     */     private void updateOpButtons() {
/* 215 */       this.makeOpButton.visible = !this.playerInfo.operator;
/* 216 */       this.removeOpButton.visible = !this.makeOpButton.visible;
/*     */     }
/*     */     
/*     */     private Button activeOpButton() {
/* 220 */       if (this.makeOpButton.visible)
/* 221 */         return this.makeOpButton; 
/* 223 */       return this.removeOpButton;
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 229 */       return (List<? extends GuiEventListener>)ImmutableList.of(activeOpButton(), this.removeButton);
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 234 */       return (List<? extends NarratableEntry>)ImmutableList.of(activeOpButton(), this.removeButton);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       int inviteColor;
/* 241 */       if (!this.playerInfo.accepted) {
/* 242 */         inviteColor = -6250336;
/* 244 */       } else if (this.playerInfo.online) {
/* 245 */         inviteColor = -16711936;
/*     */       } else {
/* 247 */         inviteColor = -1;
/*     */       } 
/* 250 */       int skinYPos = getContentYMiddle() - 16;
/* 251 */       RealmsUtil.extractPlayerFace(graphics, getContentX(), skinYPos, 32, this.playerInfo.uuid);
/* 253 */       Objects.requireNonNull(RealmsPlayersTab.this.font);
/* 253 */       int textYPos = getContentYMiddle() - 9 / 2;
/* 254 */       graphics.text(RealmsPlayersTab.this.font, this.playerInfo.name, getContentX() + 8 + 32, textYPos, inviteColor);
/* 256 */       int iconYPos = getContentYMiddle() - 10;
/* 257 */       int removeButtonXPos = getContentRight() - this.removeButton.getWidth();
/* 258 */       this.removeButton.setPosition(removeButtonXPos, iconYPos);
/* 259 */       this.removeButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 260 */       int opButtonXPos = removeButtonXPos - activeOpButton().getWidth() - 8;
/* 261 */       this.makeOpButton.setPosition(opButtonXPos, iconYPos);
/* 262 */       this.makeOpButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 263 */       this.removeOpButton.setPosition(opButtonXPos, iconYPos);
/* 264 */       this.removeOpButton.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   private class HeaderEntry extends Entry {
/* 269 */     private String cachedNumberOfInvites = "";
/*     */     
/*     */     private final FocusableTextWidget invitedWidget;
/*     */     
/*     */     public HeaderEntry() {
/* 273 */       MutableComponent mutableComponent = Component.translatable("mco.configure.world.invited.number", new Object[] { "" }).withStyle(ChatFormatting.UNDERLINE);
/* 274 */       this.invitedWidget = FocusableTextWidget.builder((Component)mutableComponent, RealmsPlayersTab.this.font).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 279 */       String numberOfInvites = (RealmsPlayersTab.this.serverData.players != null) ? Integer.toString(RealmsPlayersTab.this.serverData.players.size()) : "0";
/* 280 */       if (!numberOfInvites.equals(this.cachedNumberOfInvites)) {
/* 281 */         this.cachedNumberOfInvites = numberOfInvites;
/* 282 */         MutableComponent mutableComponent = Component.translatable("mco.configure.world.invited.number", new Object[] { numberOfInvites }).withStyle(ChatFormatting.UNDERLINE);
/* 283 */         this.invitedWidget.setMessage((Component)mutableComponent);
/*     */       } 
/* 285 */       this.invitedWidget.setPosition(RealmsPlayersTab.this.invitedList.getRowLeft() + RealmsPlayersTab.this.invitedList.getRowWidth() / 2 - this.invitedWidget.getWidth() / 2, getY() + getHeight() / 2 - this.invitedWidget.getHeight() / 2);
/* 286 */       this.invitedWidget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/*     */     private int height(int lineHeight) {
/* 290 */       return lineHeight + this.invitedWidget.getPadding() * 2;
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 295 */       return (List)List.of(this.invitedWidget);
/*     */     }
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 300 */       return (List)List.of(this.invitedWidget);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsPlayersTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
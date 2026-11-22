/*     */ package net.minecraft.client.gui.screens.friends;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.FriendsService;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractContainerWidget;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.ImageWidget;
/*     */ import net.minecraft.client.gui.components.PlainTextButton;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*     */ import net.minecraft.client.gui.screens.social.RemoteFriendListUpdateHandler;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class AddFriendWidget extends AbstractContainerWidget {
/*  35 */   private static final WidgetSprites ADD_SPRITE = new WidgetSprites(
/*  36 */       Identifier.withDefaultNamespace("friends/send_request"));
/*     */   
/*  38 */   private static final Identifier LIST_SEPARATOR_TOP = Identifier.withDefaultNamespace("friends/list_separator_top");
/*     */   
/*  39 */   private static final Component ENTER_NICKNAME = (Component)Component.translatable("gui.friends.enter_nickname");
/*     */   
/*  40 */   private static final Component SEND_REQUEST = (Component)Component.translatable("gui.friends.send_request");
/*     */   
/*  41 */   private static final Component EMPTY_NICKNAME_MESSAGE = (Component)Component.translatable("gui.friends.empty_nickname");
/*     */   
/*  42 */   private static final Component COPY_TO_CLIPBOARD = (Component)Component.translatable("gui.friends.copy_to_clipboard");
/*     */   
/*  43 */   private static final Component PROFILE_NAME_LABEL = (Component)Component.translatable("gui.friends.my_profile_name").withColor(-6250336);
/*     */   
/*     */   private static final int WINDOW_MARGIN = 8;
/*     */   
/*     */   private static final int SEPARATOR_HEIGHT = 2;
/*     */   
/*     */   private static final int INPUT_SPACING = 3;
/*     */   
/*     */   private static final int ADD_BUTTON_SIZE = 20;
/*     */   
/*     */   private static final int SEPARATOR_PADDING = 4;
/*     */   
/*     */   private static final int PROFILE_NAME_HEIGHT = 9;
/*     */   
/*     */   private static final int PROFILE_NAME_MARGIN = 6;
/*     */   
/*     */   private final EditBox editBox;
/*     */   
/*     */   private final SpriteIconButton addButton;
/*     */   
/*     */   private final PlainTextButton profileNameButton;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final LinearLayout layout;
/*     */   
/*     */   enum State {
/*  55 */     EMPTY_INPUT, READY, SENDING, DISABLED;
/*     */   }
/*     */   
/*     */   AddFriendWidget(int width, Runnable afterSend) {
/*  69 */     super(0, 0, width, 0, (Component)Component.empty());
/*  70 */     this.minecraft = Minecraft.getInstance();
/*  71 */     this.editBox = new EditBox(this.minecraft.font, width - 20 - 3 - 16, 20, ENTER_NICKNAME) {
/*     */         public boolean keyPressed(KeyEvent event) {
/*  74 */           boolean enterPressed = (event.key() == 257 || event.key() == 335);
/*  75 */           boolean elementsActive = (isActive() && AddFriendWidget.this.addButton.active);
/*  76 */           if (elementsActive && isFocused() && enterPressed) {
/*  77 */             AddFriendWidget.this.addButton.playDownSound(AddFriendWidget.this.minecraft.getSoundManager());
/*  78 */             AddFriendWidget.this.addButton.onPress((InputWithModifiers)event);
/*  79 */             return true;
/*     */           } 
/*  81 */           return super.keyPressed(event);
/*     */         }
/*     */       };
/*  84 */     this.editBox.setHint(ENTER_NICKNAME);
/*  85 */     this.editBox.setResponder(this::editBoxResponder);
/*  86 */     this
/*     */       
/* 115 */       .addButton = SpriteIconButton.builder(SEND_REQUEST, paramButton -> {
/*     */           String name = getValue();
/*     */           if (name.isBlank()) {
/*     */             applyState(State.EMPTY_INPUT);
/*     */             return;
/*     */           } 
/*     */           Component invalidInputReason = getInvalidInputReason(name);
/*     */           if (invalidInputReason != null) {
/*     */             SystemToast.addOrUpdate(this.minecraft.gui.toastManager(), SystemToast.SystemToastId.FRIEND_SYSTEM_NOTIFICATION, invalidInputReason, null);
/*     */             applyState(State.READY);
/*     */             return;
/*     */           } 
/*     */           applyState(State.SENDING);
/*     */           this.minecraft.getPlayerSocialManager().sendFriendRequest(name).thenAcceptAsync((), (Executor)this.minecraft);
/* 115 */         }true).sprite(ADD_SPRITE, 15, 15).size(20, 20).tooltip(SEND_REQUEST).switchToLoadingAfterPress().build();
/* 116 */     applyState(State.EMPTY_INPUT);
/* 118 */     String profileName = this.minecraft.getUser().getName();
/* 119 */     final MutableComponent profileNameComponent = Component.literal(profileName);
/* 120 */     int profileNameWidth = this.minecraft.font.width((FormattedText)mutableComponent);
/* 121 */     this.profileNameButton = new PlainTextButton(this, 0, 0, profileNameWidth, 9, (Component)mutableComponent, paramButton -> this.minecraft.keyboardHandler.setClipboard(profileName), this.minecraft.font) {
/*     */         protected MutableComponent createNarrationMessage() {
/* 125 */           return wrapDefaultNarrationMessage(
/* 126 */               (Component)Component.translatable("gui.friends.my_profile_name.narration", new Object[] { this.val$profileNameComponent }));
/*     */         }
/*     */       };
/* 130 */     this.profileNameButton.setTooltip(Tooltip.create(COPY_TO_CLIPBOARD));
/* 132 */     this.layout = LinearLayout.vertical();
/* 134 */     LinearLayout inputRow = LinearLayout.horizontal().spacing(3);
/* 135 */     inputRow.addChild((LayoutElement)this.editBox);
/* 136 */     inputRow.addChild((LayoutElement)this.addButton);
/* 137 */     this.layout.addChild((LayoutElement)inputRow, settings -> settings.paddingLeft(8).paddingTop(3));
/* 138 */     this.layout.addChild((LayoutElement)createProfileRow(), settings -> settings.paddingLeft(8).paddingTop(6));
/* 140 */     this.layout.addChild((LayoutElement)ImageWidget.sprite(width, 2, LIST_SEPARATOR_TOP), settings -> settings.paddingTop(4));
/* 143 */     this.layout.arrangeElements();
/* 144 */     setHeight(this.layout.getHeight());
/*     */   }
/*     */   
/*     */   private LinearLayout createProfileRow() {
/* 148 */     LinearLayout profileRow = LinearLayout.horizontal();
/* 149 */     StringWidget profileNameLabel = new StringWidget(PROFILE_NAME_LABEL, this.minecraft.font);
/* 151 */     if (this.minecraft.font.isBidirectional()) {
/* 152 */       profileRow.addChild((LayoutElement)this.profileNameButton);
/* 153 */       profileRow.addChild((LayoutElement)profileNameLabel);
/*     */     } else {
/* 155 */       profileRow.addChild((LayoutElement)profileNameLabel);
/* 156 */       profileRow.addChild((LayoutElement)this.profileNameButton);
/*     */     } 
/* 158 */     return profileRow;
/*     */   }
/*     */   
/*     */   private void editBoxResponder(String value) {
/* 162 */     applyState(value.trim().isEmpty() ? State.EMPTY_INPUT : State.READY);
/*     */   }
/*     */   
/*     */   private Component getInvalidInputReason(String name) {
/* 166 */     PlayerSocialManager playerSocialManager = this.minecraft.getPlayerSocialManager();
/* 167 */     if (this.minecraft.getUser().getName().equalsIgnoreCase(name))
/* 168 */       return (Component)Component.translatable("gui.friends.validation.cannot_add_self"); 
/* 170 */     if (contains(playerSocialManager.getFriends(), name))
/* 171 */       return (Component)Component.translatable("gui.friends.validation.already_friend", new Object[] { name }); 
/* 173 */     if (contains(playerSocialManager.getOutgoingRequests(), name))
/* 174 */       return (Component)Component.translatable("gui.friends.validation.already_outgoing", new Object[] { name }); 
/* 176 */     if (contains(playerSocialManager.getIncomingRequests(), name))
/* 177 */       return (Component)Component.translatable("gui.friends.validation.already_incoming", new Object[] { name }); 
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean contains(List<PlayerSocialManager.PlayerData> players, String playerName) {
/* 183 */     for (PlayerSocialManager.PlayerData playerData : players) {
/* 184 */       if (playerData.name().equalsIgnoreCase(playerName))
/* 185 */         return true; 
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public void applyState(State newState) {
/*     */     RemoteFriendListUpdateHandler.State friendListState;
/*     */     boolean listReady;
/* 192 */     switch (newState.ordinal()) {
/*     */       case 0:
/* 194 */         this.editBox.setEditable(true);
/* 195 */         this.editBox.active = true;
/* 196 */         this.addButton.setLoading(false);
/* 197 */         this.addButton.active = false;
/* 198 */         this.addButton.setTooltip(Tooltip.create(EMPTY_NICKNAME_MESSAGE));
/*     */         break;
/*     */       case 1:
/* 201 */         friendListState = this.minecraft.getPlayerSocialManager().getFriendListState();
/* 202 */         listReady = (friendListState == RemoteFriendListUpdateHandler.State.SUCCESS);
/* 203 */         this.editBox.setEditable(true);
/* 204 */         this.editBox.active = true;
/* 205 */         this.addButton.setLoading(false);
/* 206 */         this.addButton.active = listReady;
/* 207 */         this.addButton.setTooltip(Tooltip.create(SEND_REQUEST));
/*     */         break;
/*     */       case 2:
/* 210 */         this.editBox.setEditable(false);
/* 211 */         this.editBox.active = false;
/* 212 */         this.editBox.setFocused(false);
/* 213 */         this.addButton.active = false;
/* 214 */         this.addButton.setLoading(true);
/*     */         break;
/*     */       case 3:
/* 217 */         this.editBox.setEditable(false);
/* 218 */         this.editBox.active = false;
/* 219 */         this.editBox.setFocused(false);
/* 220 */         this.addButton.active = false;
/* 221 */         this.addButton.setLoading(false);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public EditBox getEditBox() {
/* 227 */     return this.editBox;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 231 */     return this.editBox.getValue().trim();
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 235 */     this.editBox.setValue(value);
/*     */   }
/*     */   
/*     */   protected int contentHeight() {
/* 240 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 245 */     super.setX(x);
/* 246 */     this.layout.setX(x);
/* 247 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 252 */     super.setY(y);
/* 253 */     this.layout.setY(y);
/* 254 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 259 */     this.layout.visitWidgets(child -> child.extractRenderState(graphics, mouseX, mouseY, a));
/*     */   }
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*     */   
/*     */   public Collection<? extends NarratableEntry> getNarratables() {
/* 269 */     return (Collection)List.of(this.editBox, this.addButton, this.profileNameButton);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 274 */     return (List)List.of(this.editBox, this.addButton, this.profileNameButton);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\AddFriendWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
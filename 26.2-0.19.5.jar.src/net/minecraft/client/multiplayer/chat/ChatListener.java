/*     */ package net.minecraft.client.multiplayer.chat;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.time.Instant;
/*     */ import java.util.Deque;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FilterMask;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ import net.minecraft.network.chat.PlayerChatMessage;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class ChatListener {
/*  26 */   private static final Component CHAT_VALIDATION_ERROR = (Component)Component.translatable("chat.validation_error").withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.ITALIC });
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*  29 */   private final Deque<Message> delayedMessageQueue = Queues.newArrayDeque();
/*     */   
/*     */   private long messageDelay;
/*     */   
/*     */   private long previousMessageTime;
/*     */   
/*     */   public ChatListener(Minecraft minecraft) {
/*  35 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  39 */     if (this.minecraft.isPaused()) {
/*  40 */       if (this.messageDelay > 0L)
/*  41 */         this.previousMessageTime += 50L; 
/*     */       return;
/*     */     } 
/*  46 */     if (this.messageDelay == 0L) {
/*  47 */       if (!this.delayedMessageQueue.isEmpty())
/*  48 */         flushQueue(); 
/*  50 */     } else if (Util.getMillis() >= this.previousMessageTime + this.messageDelay) {
/*     */       Message message;
/*     */       do {
/*  54 */         message = this.delayedMessageQueue.poll();
/*  55 */       } while (message != null && !message.accept());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setMessageDelay(double messageDelaySeconds) {
/*  60 */     long messageDelay = (long)(messageDelaySeconds * 1000.0D);
/*  63 */     if (messageDelay == 0L && this.messageDelay > 0L && !this.minecraft.isPaused())
/*  65 */       flushQueue(); 
/*  68 */     this.messageDelay = messageDelay;
/*     */   }
/*     */   
/*     */   public void acceptNextDelayedMessage() {
/*  72 */     ((Message)this.delayedMessageQueue.remove()).accept();
/*     */   }
/*     */   
/*     */   public long queueSize() {
/*  76 */     return this.delayedMessageQueue.size();
/*     */   }
/*     */   
/*     */   public void flushQueue() {
/*  80 */     this.delayedMessageQueue.forEach(Message::accept);
/*  81 */     this.delayedMessageQueue.clear();
/*  82 */     this.previousMessageTime = 0L;
/*     */   }
/*     */   
/*     */   public boolean removeFromDelayedMessageQueue(MessageSignature signature) {
/*  86 */     return this.delayedMessageQueue.removeIf(message -> signature.equals(message.signature()));
/*     */   }
/*     */   
/*     */   private boolean willDelayMessages() {
/*  90 */     return (this.messageDelay > 0L && Util.getMillis() < this.previousMessageTime + this.messageDelay);
/*     */   }
/*     */   
/*     */   private void handleMessage(MessageSignature signature, BooleanSupplier handler) {
/*  94 */     if (willDelayMessages()) {
/*  95 */       this.delayedMessageQueue.add(new Message(signature, handler));
/*     */     } else {
/*  97 */       handler.getAsBoolean();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handlePlayerChatMessage(PlayerChatMessage message, GameProfile sender, ChatType.Bound boundChatType) {
/* 102 */     boolean onlyShowSecure = ((Boolean)this.minecraft.options.onlyShowSecureChat().get()).booleanValue();
/* 103 */     PlayerChatMessage displayedMessage = onlyShowSecure ? message.removeUnsignedContent() : message;
/* 104 */     Component decoratedMessage = boundChatType.decorate(displayedMessage.decoratedContent());
/* 106 */     Instant received = Instant.now();
/* 107 */     handleMessage(message.signature(), () -> {
/*     */           boolean wasShown = showMessageToPlayer(boundChatType, message, decoratedMessage, sender, onlyShowSecure, received);
/*     */           ClientPacketListener connection = this.minecraft.getConnection();
/*     */           if (connection != null && message.signature() != null)
/*     */             connection.markMessageAsProcessed(message.signature(), wasShown); 
/*     */           return wasShown;
/*     */         });
/*     */   }
/*     */   
/*     */   public void handleChatMessageError(UUID senderId, MessageSignature invalidSignature, ChatType.Bound boundChatType) {
/* 118 */     handleMessage(null, () -> {
/*     */           ClientPacketListener connection = this.minecraft.getConnection();
/*     */           if (connection != null && invalidSignature != null)
/*     */             connection.markMessageAsProcessed(invalidSignature, false); 
/*     */           if (this.minecraft.isBlocked(senderId))
/*     */             return false; 
/*     */           if (this.minecraft.isFriendOnlyRestricted(senderId))
/*     */             return false; 
/*     */           LocalPlayer receiver = this.minecraft.player;
/*     */           if (receiver == null || !receiver.chatAbilities().canReceivePlayerMessages())
/*     */             return false; 
/*     */           Component decoratedMessage = boundChatType.decorate(CHAT_VALIDATION_ERROR);
/*     */           this.minecraft.gui.hud.getChat().addPlayerMessage(decoratedMessage, null, GuiMessageTag.chatError());
/*     */           this.minecraft.getNarrator().saySystemChatQueued(boundChatType.decorateNarration(CHAT_VALIDATION_ERROR));
/*     */           this.previousMessageTime = Util.getMillis();
/*     */           return true;
/*     */         });
/*     */   }
/*     */   
/*     */   public void handleDisguisedChatMessage(Component message, ChatType.Bound boundChatType) {
/* 147 */     Instant received = Instant.now();
/* 149 */     handleMessage(null, () -> {
/*     */           LocalPlayer receiver = this.minecraft.player;
/*     */           if (receiver == null || !receiver.chatAbilities().canReceivePlayerMessages())
/*     */             return false; 
/*     */           Component decoratedMessage = boundChatType.decorate(message);
/*     */           this.minecraft.gui.hud.getChat().addPlayerMessage(decoratedMessage, null, GuiMessageTag.system());
/*     */           narrateChatMessage(boundChatType, message);
/*     */           logSystemMessage(decoratedMessage, received);
/*     */           this.previousMessageTime = Util.getMillis();
/*     */           return true;
/*     */         });
/*     */   }
/*     */   
/*     */   private boolean showMessageToPlayer(ChatType.Bound boundChatType, PlayerChatMessage message, Component decoratedMessage, GameProfile sender, boolean onlyShowSecure, Instant received) {
/* 168 */     ChatTrustLevel trustLevel = evaluateTrustLevel(message, decoratedMessage, received);
/* 169 */     if (onlyShowSecure && trustLevel.isNotSecure())
/* 170 */       return false; 
/* 173 */     if (this.minecraft.isBlocked(message.sender()) || message.isFullyFiltered())
/* 174 */       return false; 
/* 177 */     if (this.minecraft.isFriendOnlyRestricted(message.sender()))
/* 178 */       return false; 
/* 181 */     LocalPlayer receiver = this.minecraft.player;
/* 182 */     if (receiver == null || !receiver.chatAbilities().canReceivePlayerMessages())
/* 183 */       return false; 
/* 186 */     GuiMessageTag tag = trustLevel.createTag(message);
/* 188 */     MessageSignature signature = message.signature();
/* 189 */     FilterMask filterMask = message.filterMask();
/* 190 */     if (filterMask.isEmpty()) {
/* 191 */       this.minecraft.gui.hud.getChat().addPlayerMessage(decoratedMessage, signature, tag);
/* 192 */       narrateChatMessage(boundChatType, message.decoratedContent());
/*     */     } else {
/* 194 */       Component filteredContent = filterMask.applyWithFormatting(message.signedContent());
/* 195 */       if (filteredContent != null) {
/* 196 */         this.minecraft.gui.hud.getChat().addPlayerMessage(boundChatType.decorate(filteredContent), signature, tag);
/* 197 */         narrateChatMessage(boundChatType, filteredContent);
/*     */       } 
/*     */     } 
/* 201 */     logPlayerMessage(message, sender, trustLevel);
/* 203 */     this.previousMessageTime = Util.getMillis();
/* 204 */     return true;
/*     */   }
/*     */   
/*     */   private void narrateChatMessage(ChatType.Bound boundChatType, Component content) {
/* 208 */     this.minecraft.getNarrator().sayChatQueued(boundChatType.decorateNarration(content));
/*     */   }
/*     */   
/*     */   private ChatTrustLevel evaluateTrustLevel(PlayerChatMessage message, Component decoratedMessage, Instant received) {
/* 212 */     if (isSenderLocalPlayer(message.sender()))
/* 213 */       return ChatTrustLevel.SECURE; 
/* 216 */     return ChatTrustLevel.evaluate(message, decoratedMessage, received);
/*     */   }
/*     */   
/*     */   private void logPlayerMessage(PlayerChatMessage message, GameProfile sender, ChatTrustLevel trustLevel) {
/* 220 */     ChatLog chatLog = this.minecraft.getReportingContext().chatLog();
/* 221 */     chatLog.push(LoggedChatMessage.player(sender, message, trustLevel));
/*     */   }
/*     */   
/*     */   private void logSystemMessage(Component message, Instant timeStamp) {
/* 225 */     ChatLog chatLog = this.minecraft.getReportingContext().chatLog();
/* 226 */     chatLog.push(LoggedChatMessage.system(message, timeStamp));
/*     */   }
/*     */   
/*     */   public void handleSystemMessage(Component message, boolean remote) {
/* 230 */     UUID guessedUUID = guessChatUUID(message);
/* 231 */     if (((Boolean)this.minecraft.options.hideMatchedNames().get()).booleanValue() && this.minecraft.isBlocked(guessedUUID))
/*     */       return; 
/* 235 */     if (guessedUUID != Util.NIL_UUID && this.minecraft.isFriendOnlyRestricted(guessedUUID))
/*     */       return; 
/* 239 */     LocalPlayer receiver = this.minecraft.player;
/* 240 */     if (receiver == null || !receiver.chatAbilities().canReceiveSystemMessages())
/*     */       return; 
/* 244 */     if (remote) {
/* 245 */       this.minecraft.gui.hud.getChat().addServerSystemMessage(message);
/* 246 */       logSystemMessage(message, Instant.now());
/*     */     } else {
/* 248 */       this.minecraft.gui.hud.getChat().addClientSystemMessage(message);
/*     */     } 
/* 252 */     this.minecraft.getNarrator().saySystemChatQueued(message);
/*     */   }
/*     */   
/*     */   public void handleOverlay(Component message) {
/* 256 */     this.minecraft.gui.hud.setOverlayMessage(message, false);
/* 257 */     this.minecraft.getNarrator().saySystemQueued(message);
/*     */   }
/*     */   
/*     */   private UUID guessChatUUID(Component message) {
/* 261 */     String noFormatMessage = StringDecomposer.getPlainText((FormattedText)message);
/* 262 */     String possibleMention = StringUtils.substringBetween(noFormatMessage, "<", ">");
/* 263 */     if (possibleMention == null)
/* 264 */       return Util.NIL_UUID; 
/* 267 */     return this.minecraft.getPlayerSocialManager().getDiscoveredUUID(possibleMention);
/*     */   }
/*     */   
/*     */   private boolean isSenderLocalPlayer(UUID senderProfileId) {
/* 271 */     if (this.minecraft.isLocalServer() && this.minecraft.player != null) {
/* 272 */       UUID localProfileId = this.minecraft.player.getGameProfile().id();
/* 273 */       return localProfileId.equals(senderProfileId);
/*     */     } 
/* 275 */     return false;
/*     */   }
/*     */   
/*     */   private static final class Message extends Record {
/*     */     private final MessageSignature signature;
/*     */     
/*     */     private final BooleanSupplier handler;
/*     */     
/*     */     private Message(MessageSignature signature, BooleanSupplier handler) {
/* 278 */       this.signature = signature;
/* 278 */       this.handler = handler;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #278	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #278	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #278	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MessageSignature signature() {
/* 278 */       return this.signature;
/*     */     }
/*     */     
/*     */     public BooleanSupplier handler() {
/* 278 */       return this.handler;
/*     */     }
/*     */     
/*     */     public boolean accept() {
/* 280 */       return this.handler.getAsBoolean();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
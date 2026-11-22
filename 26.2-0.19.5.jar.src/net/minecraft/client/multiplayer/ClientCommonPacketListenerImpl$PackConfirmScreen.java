/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class PackConfirmScreen extends ConfirmScreen {
/*     */   private final List<PendingRequest> requests;
/*     */   
/*     */   private final Screen parentScreen;
/*     */   
/*     */   private static final class PendingRequest extends Record {
/*     */     private final UUID id;
/*     */     
/*     */     private final URL url;
/*     */     
/*     */     private final String hash;
/*     */     
/*     */     private PendingRequest(UUID id, URL url, String hash) {
/* 442 */       this.id = id;
/* 442 */       this.url = url;
/* 442 */       this.hash = hash;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public UUID id() {
/* 442 */       return this.id;
/*     */     }
/*     */     
/*     */     public URL url() {
/* 442 */       return this.url;
/*     */     }
/*     */     
/*     */     public String hash() {
/* 442 */       return this.hash;
/*     */     }
/*     */   }
/*     */   
/*     */   private PackConfirmScreen(Minecraft minecraft, Screen parentScreen, List<PendingRequest> requests, boolean required, Component prompt) {
/* 449 */     super(result -> {
/*     */           minecraft.gui.setScreen(parentScreen);
/*     */           DownloadedPackSource packSource = minecraft.getDownloadedPackSource();
/*     */           if (result) {
/*     */             if (ClientCommonPacketListenerImpl.this.serverData != null)
/*     */               ClientCommonPacketListenerImpl.this.serverData.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED); 
/*     */             packSource.allowServerPacks();
/*     */           } else {
/*     */             packSource.rejectServerPacks();
/*     */             if (required) {
/*     */               ClientCommonPacketListenerImpl.this.connection.disconnect((Component)Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
/*     */             } else if (ClientCommonPacketListenerImpl.this.serverData != null) {
/*     */               ClientCommonPacketListenerImpl.this.serverData.setResourcePackStatus(ServerData.ServerPackStatus.DISABLED);
/*     */             } 
/*     */           } 
/*     */           for (PendingRequest request : requests)
/*     */             packSource.pushPack(request.id, request.url, request.hash); 
/*     */           if (ClientCommonPacketListenerImpl.this.serverData != null)
/*     */             ServerList.saveSingleServer(ClientCommonPacketListenerImpl.this.serverData); 
/* 476 */         }required ? (Component)Component.translatable("multiplayer.requiredTexturePrompt.line1") : (Component)Component.translatable("multiplayer.texturePrompt.line1"), 
/* 477 */         ClientCommonPacketListenerImpl.preparePackPrompt(required ? (Component)Component.translatable("multiplayer.requiredTexturePrompt.line2").withStyle(new ChatFormatting[] { ChatFormatting.YELLOW, ChatFormatting.BOLD }, ) : (Component)Component.translatable("multiplayer.texturePrompt.line2"), prompt), 
/* 478 */         required ? CommonComponents.GUI_PROCEED : CommonComponents.GUI_YES, 
/* 479 */         required ? CommonComponents.GUI_DISCONNECT : CommonComponents.GUI_NO);
/* 481 */     this.requests = requests;
/* 482 */     this.parentScreen = parentScreen;
/*     */   }
/*     */   
/*     */   public PackConfirmScreen update(Minecraft minecraft, UUID id, URL url, String hash, boolean required, Component prompt) {
/* 489 */     ImmutableList immutableList = ImmutableList.builderWithExpectedSize(this.requests.size() + 1).addAll(this.requests).add(new PendingRequest(id, url, hash)).build();
/* 490 */     return new PackConfirmScreen(minecraft, this.parentScreen, (List<PendingRequest>)immutableList, required, prompt);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientCommonPacketListenerImpl$PackConfirmScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
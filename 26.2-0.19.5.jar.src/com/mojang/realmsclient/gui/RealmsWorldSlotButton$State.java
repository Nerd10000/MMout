/*    */ package com.mojang.realmsclient.gui;
/*    */ 
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import com.mojang.realmsclient.dto.RealmsSlot;
/*    */ 
/*    */ public class State {
/*    */   private final String slotName;
/*    */   
/*    */   private final String slotVersion;
/*    */   
/*    */   private final RealmsServer.Compatibility compatibility;
/*    */   
/*    */   private final long imageId;
/*    */   
/*    */   private final String image;
/*    */   
/*    */   public final boolean empty;
/*    */   
/*    */   public final boolean minigame;
/*    */   
/*    */   public final RealmsWorldSlotButton.Action action;
/*    */   
/*    */   public final boolean hardcore;
/*    */   
/*    */   public final boolean activeSlot;
/*    */   
/*    */   public State(RealmsServer serverData, int slotIndex) {
/* 69 */     this.minigame = (slotIndex == 4);
/* 70 */     if (this.minigame) {
/* 71 */       this.slotName = RealmsWorldSlotButton.MINIGAME.getString();
/* 72 */       this.imageId = serverData.minigameId;
/* 73 */       this.image = serverData.minigameImage;
/* 74 */       this.empty = (serverData.minigameId == -1);
/* 75 */       this.slotVersion = "";
/* 76 */       this.compatibility = RealmsServer.Compatibility.UNVERIFIABLE;
/* 77 */       this.hardcore = false;
/* 78 */       this.activeSlot = serverData.isMinigameActive();
/*    */     } else {
/* 80 */       RealmsSlot slot = (RealmsSlot)serverData.slots.get(Integer.valueOf(slotIndex));
/* 81 */       this.slotName = slot.options.getSlotName(slotIndex);
/* 82 */       this.imageId = slot.options.templateId;
/* 83 */       this.image = slot.options.templateImage;
/* 84 */       this.empty = slot.options.empty;
/* 85 */       this.slotVersion = slot.options.version;
/* 86 */       this.compatibility = slot.options.compatibility;
/* 87 */       this.hardcore = slot.isHardcore();
/* 88 */       this.activeSlot = (serverData.activeSlot == slotIndex && !serverData.isMinigameActive());
/*    */     } 
/* 90 */     this.action = RealmsWorldSlotButton.getAction(this.activeSlot, this.empty, serverData.expired);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\RealmsWorldSlotButton$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
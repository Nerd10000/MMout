/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.player.ChatVisiblity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public final class ClientInformation extends Record {
/*    */   private final String language;
/*    */   
/*    */   private final int viewDistance;
/*    */   
/*    */   private final ChatVisiblity chatVisibility;
/*    */   
/*    */   private final boolean chatColors;
/*    */   
/*    */   private final int modelCustomisation;
/*    */   
/*    */   private final HumanoidArm mainHand;
/*    */   
/*    */   private final boolean textFilteringEnabled;
/*    */   
/*    */   private final boolean allowsListing;
/*    */   
/*    */   private final ParticleStatus particleStatus;
/*    */   
/*    */   public static final int MAX_LANGUAGE_LENGTH = 16;
/*    */   
/*    */   public ClientInformation(String language, int viewDistance, ChatVisiblity chatVisibility, boolean chatColors, int modelCustomisation, HumanoidArm mainHand, boolean textFilteringEnabled, boolean allowsListing, ParticleStatus particleStatus) {
/*  9 */     this.language = language;
/*  9 */     this.viewDistance = viewDistance;
/*  9 */     this.chatVisibility = chatVisibility;
/*  9 */     this.chatColors = chatColors;
/*  9 */     this.modelCustomisation = modelCustomisation;
/*  9 */     this.mainHand = mainHand;
/*  9 */     this.textFilteringEnabled = textFilteringEnabled;
/*  9 */     this.allowsListing = allowsListing;
/*  9 */     this.particleStatus = particleStatus;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ClientInformation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ClientInformation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ClientInformation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ClientInformation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ClientInformation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ClientInformation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String language() {
/*  9 */     return this.language;
/*    */   }
/*    */   
/*    */   public int viewDistance() {
/*  9 */     return this.viewDistance;
/*    */   }
/*    */   
/*    */   public ChatVisiblity chatVisibility() {
/*  9 */     return this.chatVisibility;
/*    */   }
/*    */   
/*    */   public boolean chatColors() {
/*  9 */     return this.chatColors;
/*    */   }
/*    */   
/*    */   public int modelCustomisation() {
/*  9 */     return this.modelCustomisation;
/*    */   }
/*    */   
/*    */   public HumanoidArm mainHand() {
/*  9 */     return this.mainHand;
/*    */   }
/*    */   
/*    */   public boolean textFilteringEnabled() {
/*  9 */     return this.textFilteringEnabled;
/*    */   }
/*    */   
/*    */   public boolean allowsListing() {
/*  9 */     return this.allowsListing;
/*    */   }
/*    */   
/*    */   public ParticleStatus particleStatus() {
/*  9 */     return this.particleStatus;
/*    */   }
/*    */   
/*    */   public ClientInformation(FriendlyByteBuf input) {
/* 24 */     this(input
/* 25 */         .readUtf(16), input
/* 26 */         .readByte(), (ChatVisiblity)input
/* 27 */         .readEnum(ChatVisiblity.class), input
/* 28 */         .readBoolean(), input
/* 29 */         .readUnsignedByte(), (HumanoidArm)input
/* 30 */         .readEnum(HumanoidArm.class), input
/* 31 */         .readBoolean(), input
/* 32 */         .readBoolean(), (ParticleStatus)input
/* 33 */         .readEnum(ParticleStatus.class));
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf output) {
/* 38 */     output.writeUtf(this.language);
/* 39 */     output.writeByte(this.viewDistance);
/* 40 */     output.writeEnum((Enum)this.chatVisibility);
/* 41 */     output.writeBoolean(this.chatColors);
/* 42 */     output.writeByte(this.modelCustomisation);
/* 43 */     output.writeEnum((Enum)this.mainHand);
/* 44 */     output.writeBoolean(this.textFilteringEnabled);
/* 45 */     output.writeBoolean(this.allowsListing);
/* 46 */     output.writeEnum(this.particleStatus);
/*    */   }
/*    */   
/*    */   public static ClientInformation createDefault() {
/* 50 */     return new ClientInformation("en_us", 2, ChatVisiblity.FULL, true, 0, Player.DEFAULT_MAIN_HAND, false, false, ParticleStatus.ALL);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ClientInformation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
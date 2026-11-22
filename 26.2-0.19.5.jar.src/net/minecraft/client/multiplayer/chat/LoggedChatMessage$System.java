/*     */ package net.minecraft.client.multiplayer.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.time.Instant;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class System extends Record implements LoggedChatMessage {
/*     */   private final Component message;
/*     */   
/*     */   private final Instant timeStamp;
/*     */   
/*     */   public static final MapCodec<System> CODEC;
/*     */   
/*     */   public System(Component message, Instant timeStamp) {
/*  87 */     this.message = message;
/*  87 */     this.timeStamp = timeStamp;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/LoggedChatMessage$System;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component message() {
/*  87 */     return this.message;
/*     */   }
/*     */   
/*     */   public Instant timeStamp() {
/*  87 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */   static {
/*  88 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("message").forGetter(System::message), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("time_stamp").forGetter(System::timeStamp)).apply((Applicative)i, System::new));
/*     */   }
/*     */   
/*     */   public Component toContentComponent() {
/*  95 */     return this.message;
/*     */   }
/*     */   
/*     */   public boolean canReport(UUID reportedPlayerId) {
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   public LoggedChatEvent.Type type() {
/* 105 */     return LoggedChatEvent.Type.SYSTEM;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\LoggedChatMessage$System.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
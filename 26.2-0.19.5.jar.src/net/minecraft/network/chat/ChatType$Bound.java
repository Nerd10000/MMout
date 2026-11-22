/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class Bound extends Record {
/*     */   private final Holder<ChatType> chatType;
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   private final Optional<Component> targetName;
/*     */   
/*     */   public Bound(Holder<ChatType> chatType, Component name, Optional<Component> targetName) {
/*  94 */     this.chatType = chatType;
/*  94 */     this.name = name;
/*  94 */     this.targetName = targetName;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ChatType$Bound;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatType$Bound;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ChatType$Bound;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatType$Bound;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ChatType$Bound;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/ChatType$Bound;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<ChatType> chatType() {
/*  94 */     return this.chatType;
/*     */   }
/*     */   
/*     */   public Component name() {
/*  94 */     return this.name;
/*     */   }
/*     */   
/*     */   public Optional<Component> targetName() {
/*  94 */     return this.targetName;
/*     */   }
/*     */   
/*  95 */   public static final StreamCodec<RegistryFriendlyByteBuf, Bound> STREAM_CODEC = StreamCodec.composite(ChatType.STREAM_CODEC, Bound::chatType, ComponentSerialization.TRUSTED_STREAM_CODEC, Bound::name, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, Bound::targetName, Bound::new);
/*     */   
/*     */   private Bound(Holder<ChatType> chatType, Component name) {
/* 103 */     this(chatType, name, Optional.empty());
/*     */   }
/*     */   
/*     */   public Component decorate(Component content) {
/* 107 */     return ((ChatType)this.chatType.value()).chat().decorate(content, this);
/*     */   }
/*     */   
/*     */   public Component decorateNarration(Component content) {
/* 111 */     return ((ChatType)this.chatType.value()).narration().decorate(content, this);
/*     */   }
/*     */   
/*     */   public Bound withTargetName(Component targetName) {
/* 115 */     return new Bound(this.chatType, this.name, Optional.of(targetName));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ChatType$Bound.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
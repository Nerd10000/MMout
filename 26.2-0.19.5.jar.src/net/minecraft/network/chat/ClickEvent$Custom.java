/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Custom extends Record implements ClickEvent {
/*     */   private final Identifier id;
/*     */   
/*     */   private final Optional<Tag> payload;
/*     */   
/*     */   public static final MapCodec<Custom> CODEC;
/*     */   
/*     */   public Custom(Identifier id, Optional<Tag> payload) {
/* 115 */     this.id = id;
/* 115 */     this.payload = payload;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$Custom;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$Custom;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$Custom;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 115 */     return this.id;
/*     */   }
/*     */   
/*     */   public Optional<Tag> payload() {
/* 115 */     return this.payload;
/*     */   }
/*     */   
/*     */   static {
/* 116 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(Custom::id), (App)ExtraCodecs.NBT.optionalFieldOf("payload").forGetter(Custom::payload)).apply((Applicative)i, Custom::new));
/*     */   }
/*     */   
/*     */   public ClickEvent.Action action() {
/* 124 */     return ClickEvent.Action.CUSTOM;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ClickEvent$Custom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
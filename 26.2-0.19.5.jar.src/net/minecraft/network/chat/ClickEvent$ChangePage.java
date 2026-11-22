/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class ChangePage extends Record implements ClickEvent {
/*     */   private final int page;
/*     */   
/*     */   public static final MapCodec<ChangePage> CODEC;
/*     */   
/*     */   public ChangePage(int page) {
/*  93 */     this.page = page;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int page() {
/*  93 */     return this.page;
/*     */   }
/*     */   
/*     */   static {
/*  94 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("page").forGetter(ChangePage::page)).apply((Applicative)i, ChangePage::new));
/*     */   }
/*     */   
/*     */   public ClickEvent.Action action() {
/* 100 */     return ClickEvent.Action.CHANGE_PAGE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ClickEvent$ChangePage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
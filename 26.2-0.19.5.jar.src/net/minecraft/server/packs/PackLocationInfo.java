/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.server.packs.repository.PackSource;
/*    */ 
/*    */ public final class PackLocationInfo extends Record {
/*    */   private final String id;
/*    */   
/*    */   private final Component title;
/*    */   
/*    */   private final PackSource source;
/*    */   
/*    */   private final Optional<KnownPack> knownPackInfo;
/*    */   
/*    */   public PackLocationInfo(String id, Component title, PackSource source, Optional<KnownPack> knownPackInfo) {
/* 13 */     this.id = id;
/* 13 */     this.title = title;
/* 13 */     this.source = source;
/* 13 */     this.knownPackInfo = knownPackInfo;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/PackLocationInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/PackLocationInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/PackLocationInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/PackLocationInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/PackLocationInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/PackLocationInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String id() {
/* 13 */     return this.id;
/*    */   }
/*    */   
/*    */   public Component title() {
/* 13 */     return this.title;
/*    */   }
/*    */   
/*    */   public PackSource source() {
/* 13 */     return this.source;
/*    */   }
/*    */   
/*    */   public Optional<KnownPack> knownPackInfo() {
/* 13 */     return this.knownPackInfo;
/*    */   }
/*    */   
/*    */   public Component createChatLink(boolean enabled, Component description) {
/* 20 */     return (Component)ComponentUtils.wrapInSquareBrackets(this.source.decorate((Component)Component.literal(this.id))).withStyle(s -> s.withColor(enabled ? ChatFormatting.GREEN : ChatFormatting.RED).withInsertion(StringArgumentType.escapeIfRequired(this.id)).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.empty().append(this.title).append("\n").append(description))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\PackLocationInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
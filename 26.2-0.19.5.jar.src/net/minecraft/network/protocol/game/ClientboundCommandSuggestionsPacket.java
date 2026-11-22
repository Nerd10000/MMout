/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.StringRange;
/*    */ import com.mojang.brigadier.suggestion.Suggestion;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundCommandSuggestionsPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   
/*    */   private final int start;
/*    */   
/*    */   private final int length;
/*    */   
/*    */   private final List<Entry> suggestions;
/*    */   
/*    */   public ClientboundCommandSuggestionsPacket(int id, int start, int length, List<Entry> suggestions) {
/* 18 */     this.id = id;
/* 18 */     this.start = start;
/* 18 */     this.length = length;
/* 18 */     this.suggestions = suggestions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int id() {
/* 18 */     return this.id;
/*    */   }
/*    */   
/*    */   public int start() {
/* 18 */     return this.start;
/*    */   }
/*    */   
/*    */   public int length() {
/* 18 */     return this.length;
/*    */   }
/*    */   
/*    */   public List<Entry> suggestions() {
/* 18 */     return this.suggestions;
/*    */   }
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCommandSuggestionsPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundCommandSuggestionsPacket::id, ByteBufCodecs.VAR_INT, ClientboundCommandSuggestionsPacket::start, ByteBufCodecs.VAR_INT, ClientboundCommandSuggestionsPacket::length, Entry.STREAM_CODEC
/*    */       
/* 23 */       .apply(ByteBufCodecs.list()), ClientboundCommandSuggestionsPacket::suggestions, ClientboundCommandSuggestionsPacket::new);
/*    */   
/*    */   public ClientboundCommandSuggestionsPacket(int id, Suggestions suggestions) {
/* 28 */     this(id, suggestions.getRange().getStart(), suggestions.getRange().getLength(), suggestions.getList().stream()
/* 29 */         .map(suggestion -> new Entry(suggestion.getText(), Optional.<Message>ofNullable(suggestion.getTooltip()).map(ComponentUtils::fromMessage)))
/* 30 */         .toList());
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundCommandSuggestionsPacket> type() {
/* 36 */     return GamePacketTypes.CLIENTBOUND_COMMAND_SUGGESTIONS;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 41 */     listener.handleCommandSuggestions(this);
/*    */   }
/*    */   
/*    */   public Suggestions toSuggestions() {
/* 45 */     StringRange range = StringRange.between(this.start, this.start + this.length);
/* 46 */     return new Suggestions(range, this.suggestions.stream()
/* 47 */         .map(entry -> new Suggestion(range, entry.text(), (Message)entry.tooltip().orElse(null)))
/* 48 */         .toList());
/*    */   }
/*    */   
/*    */   public static final class Entry extends Record {
/*    */     private final String text;
/*    */     
/*    */     private final Optional<Component> tooltip;
/*    */     
/*    */     public Entry(String text, Optional<Component> tooltip) {
/* 52 */       this.text = text;
/* 52 */       this.tooltip = tooltip;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket$Entry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket$Entry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public String text() {
/* 52 */       return this.text;
/*    */     }
/*    */     
/*    */     public Optional<Component> tooltip() {
/* 52 */       return this.tooltip;
/*    */     }
/*    */     
/* 53 */     public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Entry::text, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, Entry::tooltip, Entry::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundCommandSuggestionsPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
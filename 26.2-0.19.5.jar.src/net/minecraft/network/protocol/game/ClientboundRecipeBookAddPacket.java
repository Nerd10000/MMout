/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*    */ 
/*    */ public final class ClientboundRecipeBookAddPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final List<Entry> entries;
/*    */   
/*    */   private final boolean replace;
/*    */   
/*    */   public ClientboundRecipeBookAddPacket(List<Entry> entries, boolean replace) {
/* 12 */     this.entries = entries;
/* 12 */     this.replace = replace;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Entry> entries() {
/* 12 */     return this.entries;
/*    */   }
/*    */   
/*    */   public boolean replace() {
/* 12 */     return this.replace;
/*    */   }
/*    */   
/* 16 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRecipeBookAddPacket> STREAM_CODEC = StreamCodec.composite(Entry.STREAM_CODEC
/* 17 */       .apply(ByteBufCodecs.list()), ClientboundRecipeBookAddPacket::entries, ByteBufCodecs.BOOL, ClientboundRecipeBookAddPacket::replace, ClientboundRecipeBookAddPacket::new);
/*    */   
/*    */   public PacketType<ClientboundRecipeBookAddPacket> type() {
/* 24 */     return GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_ADD;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 29 */     listener.handleRecipeBookAdd(this);
/*    */   }
/*    */   
/*    */   public static final class Entry extends Record {
/*    */     private final RecipeDisplayEntry contents;
/*    */     
/*    */     private final byte flags;
/*    */     
/*    */     public static final byte FLAG_NOTIFICATION = 1;
/*    */     
/*    */     public static final byte FLAG_HIGHLIGHT = 2;
/*    */     
/*    */     public Entry(RecipeDisplayEntry contents, byte flags) {
/* 32 */       this.contents = contents;
/* 32 */       this.flags = flags;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public RecipeDisplayEntry contents() {
/* 32 */       return this.contents;
/*    */     }
/*    */     
/*    */     public byte flags() {
/* 32 */       return this.flags;
/*    */     }
/*    */     
/* 39 */     public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(RecipeDisplayEntry.STREAM_CODEC, Entry::contents, ByteBufCodecs.BYTE, Entry::flags, Entry::new);
/*    */     
/*    */     public Entry(RecipeDisplayEntry contents, boolean notification, boolean highlight) {
/* 46 */       this(contents, (byte)((notification ? 1 : 0) | (highlight ? 2 : 0)));
/*    */     }
/*    */     
/*    */     public boolean notification() {
/* 50 */       return ((this.flags & 0x1) != 0);
/*    */     }
/*    */     
/*    */     public boolean highlight() {
/* 54 */       return ((this.flags & 0x2) != 0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundRecipeBookAddPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
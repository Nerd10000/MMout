/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*    */ 
/*    */ public final class ServerboundRecipeBookSeenRecipePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final RecipeDisplayId recipe;
/*    */   
/*    */   public ServerboundRecipeBookSeenRecipePacket(RecipeDisplayId recipe) {
/*  9 */     this.recipe = recipe;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundRecipeBookSeenRecipePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundRecipeBookSeenRecipePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundRecipeBookSeenRecipePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundRecipeBookSeenRecipePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundRecipeBookSeenRecipePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundRecipeBookSeenRecipePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RecipeDisplayId recipe() {
/*  9 */     return this.recipe;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundRecipeBookSeenRecipePacket> STREAM_CODEC = StreamCodec.composite(RecipeDisplayId.STREAM_CODEC, ServerboundRecipeBookSeenRecipePacket::recipe, ServerboundRecipeBookSeenRecipePacket::new);
/*    */   
/*    */   public PacketType<ServerboundRecipeBookSeenRecipePacket> type() {
/* 19 */     return GamePacketTypes.SERVERBOUND_RECIPE_BOOK_SEEN_RECIPE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 24 */     listener.handleRecipeBookSeenRecipePacket(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundRecipeBookSeenRecipePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
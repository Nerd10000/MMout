/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*    */ import net.minecraft.world.item.crafting.StonecutterRecipe;
/*    */ 
/*    */ public final class ClientboundUpdateRecipesPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets;
/*    */   
/*    */   private final SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes;
/*    */   
/*    */   public ClientboundUpdateRecipesPacket(Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets, SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes) {
/* 17 */     this.itemSets = itemSets;
/* 17 */     this.stonecutterRecipes = stonecutterRecipes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets() {
/* 17 */     return this.itemSets;
/*    */   }
/*    */   
/*    */   public SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes() {
/* 17 */     return this.stonecutterRecipes;
/*    */   }
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateRecipesPacket> STREAM_CODEC = StreamCodec.composite(
/* 22 */       ByteBufCodecs.map(java.util.HashMap::new, ResourceKey.streamCodec(RecipePropertySet.TYPE_KEY), RecipePropertySet.STREAM_CODEC), ClientboundUpdateRecipesPacket::itemSets, 
/* 23 */       SelectableRecipe.SingleInputSet.noRecipeCodec(), ClientboundUpdateRecipesPacket::stonecutterRecipes, ClientboundUpdateRecipesPacket::new);
/*    */   
/*    */   public PacketType<ClientboundUpdateRecipesPacket> type() {
/* 29 */     return GamePacketTypes.CLIENTBOUND_UPDATE_RECIPES;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 34 */     listener.handleUpdateRecipes(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundUpdateRecipesPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
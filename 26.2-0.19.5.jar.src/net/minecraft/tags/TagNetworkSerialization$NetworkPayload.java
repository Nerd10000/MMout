/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class NetworkPayload {
/* 58 */   public static final NetworkPayload EMPTY = new NetworkPayload(Map.of());
/*    */   
/*    */   private final Map<Identifier, IntList> tags;
/*    */   
/*    */   public NetworkPayload(Map<Identifier, IntList> tags) {
/* 63 */     this.tags = tags;
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf buf) {
/* 67 */     buf.writeMap(this.tags, FriendlyByteBuf::writeIdentifier, FriendlyByteBuf::writeIntIdList);
/*    */   }
/*    */   
/*    */   public static NetworkPayload read(FriendlyByteBuf buf) {
/* 71 */     return new NetworkPayload(buf.readMap(FriendlyByteBuf::readIdentifier, FriendlyByteBuf::readIntIdList));
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 75 */     return this.tags.isEmpty();
/*    */   }
/*    */   
/*    */   public int size() {
/* 79 */     return this.tags.size();
/*    */   }
/*    */   
/*    */   public <T> TagLoader.LoadResult<T> resolve(Registry<T> registry) {
/* 83 */     return TagNetworkSerialization.deserializeTagsFromNetwork(registry, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagNetworkSerialization$NetworkPayload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class Builder<B extends ByteBuf, V, T> {
/* 70 */   private final List<IdDispatchCodec.Entry<B, V, T>> entries = new ArrayList<>();
/*    */   
/*    */   private final Function<V, ? extends T> typeGetter;
/*    */   
/*    */   private Builder(Function<V, ? extends T> typeGetter) {
/* 74 */     this.typeGetter = typeGetter;
/*    */   }
/*    */   
/*    */   public Builder<B, V, T> add(T type, StreamCodec<? super B, ? extends V> serializer) {
/* 78 */     this.entries.add(new IdDispatchCodec.Entry<>(serializer, type));
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   public IdDispatchCodec<B, V, T> build() {
/* 83 */     Object2IntOpenHashMap<T> toId = new Object2IntOpenHashMap();
/* 84 */     toId.defaultReturnValue(-2);
/* 86 */     for (IdDispatchCodec.Entry<B, V, T> entry : this.entries) {
/* 87 */       int id = toId.size();
/* 88 */       int previous = toId.putIfAbsent(entry.type, id);
/* 89 */       if (previous != -2)
/* 90 */         throw new IllegalStateException("Duplicate registration for type " + String.valueOf(entry.type)); 
/*    */     } 
/* 94 */     return new IdDispatchCodec<>(this.typeGetter, List.copyOf(this.entries), (Object2IntMap<T>)toId);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\IdDispatchCodec$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
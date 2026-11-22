/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface EntityDataSerializer<T> {
/*    */   StreamCodec<? super RegistryFriendlyByteBuf, T> codec();
/*    */   
/*    */   default EntityDataAccessor<T> createAccessor(int id) {
/* 10 */     return new EntityDataAccessor<>(id, this);
/*    */   }
/*    */   
/*    */   T copy(T paramT);
/*    */   
/*    */   public static interface ForValueType<T> extends EntityDataSerializer<T> {
/*    */     default T copy(T value) {
/* 18 */       return value;
/*    */     }
/*    */   }
/*    */   
/*    */   static <T> EntityDataSerializer<T> forValueType(StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
/* 23 */     return () -> codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\EntityDataSerializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ class null implements StreamCodec<B, V> {
/*    */   public V decode(B input) {
/* 55 */     return (V)instance;
/*    */   }
/*    */   
/*    */   public void encode(B output, V value) {
/* 60 */     if (!value.equals(instance))
/* 61 */       throw new IllegalStateException("Can't encode '" + String.valueOf(value) + "', expected '" + String.valueOf(instance) + "'"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ class null implements StreamCodec<B, V> {
/*    */   public V decode(B input) {
/* 26 */     return decoder.decode(input);
/*    */   }
/*    */   
/*    */   public void encode(B output, V value) {
/* 31 */     encoder.encode(output, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
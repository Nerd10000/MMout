/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ class null implements StreamCodec<B, O> {
/*    */   public O decode(B input) {
/* 80 */     return (O)to.apply(StreamCodec.this.decode(input));
/*    */   }
/*    */   
/*    */   public void encode(B output, O value) {
/* 85 */     StreamCodec.this.encode(output, from.apply(value));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
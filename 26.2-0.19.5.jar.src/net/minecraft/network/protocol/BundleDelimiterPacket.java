/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ public abstract class BundleDelimiterPacket<T extends PacketListener> implements Packet<T> {
/*    */   public final void handle(T listener) {
/* 11 */     throw new AssertionError("This packet should be handled by pipeline");
/*    */   }
/*    */   
/*    */   public abstract PacketType<? extends BundleDelimiterPacket<T>> type();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\BundleDelimiterPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
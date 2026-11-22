/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ public abstract class BundlePacket<T extends PacketListener> implements Packet<T> {
/*    */   private final Iterable<Packet<? super T>> packets;
/*    */   
/*    */   protected BundlePacket(Iterable<Packet<? super T>> packets) {
/*  9 */     this.packets = packets;
/*    */   }
/*    */   
/*    */   public final Iterable<Packet<? super T>> subPackets() {
/* 13 */     return this.packets;
/*    */   }
/*    */   
/*    */   public abstract PacketType<? extends BundlePacket<T>> type();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\BundlePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
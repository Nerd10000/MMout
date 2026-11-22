/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ class null implements BundlerInfo {
/*    */   public void unbundlePacket(Packet<?> packet, Consumer<Packet<?>> output) {
/* 18 */     if (packet.type() == bundlePacketType) {
/* 19 */       BundlePacket<?> bundlePacket = (BundlePacket)packet;
/* 20 */       output.accept(delimiterPacket);
/* 21 */       bundlePacket.subPackets().forEach(output);
/* 22 */       output.accept(delimiterPacket);
/*    */     } else {
/* 24 */       output.accept(packet);
/*    */     } 
/*    */   }
/*    */   
/*    */   public BundlerInfo.Bundler startPacketBundling(Packet<?> packet) {
/* 30 */     if (packet == delimiterPacket)
/* 31 */       return new BundlerInfo.Bundler(this) {
/* 32 */           private final List<Packet<? super T>> bundlePackets = new ArrayList<>();
/*    */           
/*    */           public Packet<?> addPacket(Packet<?> packet) {
/* 36 */             if (packet == delimiterPacket)
/* 38 */               return constructor.apply(this.bundlePackets); 
/* 41 */             Packet<T> castPacket = (Packet)packet;
/* 42 */             if (this.bundlePackets.size() >= 4096)
/* 43 */               throw new IllegalStateException("Too many packets in a bundle"); 
/* 45 */             this.bundlePackets.add(castPacket);
/* 46 */             return null;
/*    */           }
/*    */         }; 
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\BundlerInfo$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
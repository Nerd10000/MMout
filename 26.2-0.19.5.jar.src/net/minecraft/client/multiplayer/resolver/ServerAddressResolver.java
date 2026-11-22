/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Optional;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ServerAddressResolver {
/* 13 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static final ServerAddressResolver SYSTEM;
/*    */   
/*    */   static {
/* 15 */     SYSTEM = (address -> {
/*    */         try {
/*    */           InetAddress resolvedAddress = InetAddress.getByName(address.getHost());
/*    */           return Optional.of(ResolvedServerAddress.from(new InetSocketAddress(resolvedAddress, address.getPort())));
/* 19 */         } catch (UnknownHostException e) {
/*    */           LOGGER.debug("Couldn't resolve server {} address", address.getHost(), e);
/*    */           return Optional.empty();
/*    */         } 
/*    */       });
/*    */   }
/*    */   
/*    */   Optional<ResolvedServerAddress> resolve(ServerAddress paramServerAddress);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\resolver\ServerAddressResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
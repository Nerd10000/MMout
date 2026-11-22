/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ class null implements AddressCheck {
/*    */   public boolean isAllowed(ResolvedServerAddress address) {
/* 25 */     String hostName = address.getHostName();
/* 26 */     String hostIp = address.getHostIp();
/* 27 */     return blockLists.stream().noneMatch(p -> (p.test(hostName) || p.test(hostIp)));
/*    */   }
/*    */   
/*    */   public boolean isAllowed(ServerAddress address) {
/* 32 */     String hostName = address.getHost();
/* 33 */     return blockLists.stream().noneMatch(p -> p.test(hostName));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\resolver\AddressCheck$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
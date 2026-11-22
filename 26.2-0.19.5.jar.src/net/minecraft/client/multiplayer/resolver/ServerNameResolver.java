/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ServerNameResolver {
/*  9 */   public static final ServerNameResolver DEFAULT = new ServerNameResolver(ServerAddressResolver.SYSTEM, ServerRedirectHandler.createDnsSrvRedirectHandler(), AddressCheck.createFromService());
/*    */   
/*    */   private final ServerAddressResolver resolver;
/*    */   
/*    */   private final ServerRedirectHandler redirectHandler;
/*    */   
/*    */   private final AddressCheck addressCheck;
/*    */   
/*    */   @VisibleForTesting
/*    */   ServerNameResolver(ServerAddressResolver resolver, ServerRedirectHandler redirectHandler, AddressCheck addressCheck) {
/* 18 */     this.resolver = resolver;
/* 19 */     this.redirectHandler = redirectHandler;
/* 20 */     this.addressCheck = addressCheck;
/*    */   }
/*    */   
/*    */   public Optional<ResolvedServerAddress> resolveAddress(ServerAddress address) {
/* 25 */     Optional<ResolvedServerAddress> resolvedAddress = this.resolver.resolve(address);
/* 28 */     if ((resolvedAddress.isPresent() && !this.addressCheck.isAllowed(resolvedAddress.get())) || 
/* 29 */       !this.addressCheck.isAllowed(address))
/* 30 */       return Optional.empty(); 
/* 33 */     Optional<ServerAddress> redirectedAddress = this.redirectHandler.lookupRedirect(address);
/* 34 */     if (redirectedAddress.isPresent()) {
/* 36 */       Objects.requireNonNull(this.addressCheck);
/* 36 */       resolvedAddress = this.resolver.resolve(redirectedAddress.get()).filter(this.addressCheck::isAllowed);
/*    */     } 
/* 39 */     return resolvedAddress;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\resolver\ServerNameResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
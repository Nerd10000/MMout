/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public enum ClientRegistryLayer {
/* 10 */   STATIC, REMOTE;
/*    */   
/*    */   private static final List<ClientRegistryLayer> VALUES;
/*    */   
/*    */   private static final RegistryAccess.Frozen STATIC_ACCESS;
/*    */   
/*    */   static {
/* 14 */     VALUES = List.of(values());
/* 16 */     STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */   }
/*    */   
/*    */   public static LayeredRegistryAccess<ClientRegistryLayer> createRegistryAccess() {
/* 19 */     return (new LayeredRegistryAccess(VALUES)).replaceFrom(STATIC, new RegistryAccess.Frozen[] { STATIC_ACCESS });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientRegistryLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public enum RegistryLayer {
/* 10 */   STATIC, WORLDGEN, DIMENSIONS, RELOADABLE;
/*    */   
/*    */   private static final List<RegistryLayer> VALUES;
/*    */   
/*    */   private static final RegistryAccess.Frozen STATIC_ACCESS;
/*    */   
/*    */   static {
/* 16 */     VALUES = List.of(values());
/* 18 */     STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */   }
/*    */   
/*    */   public static LayeredRegistryAccess<RegistryLayer> createRegistryAccess() {
/* 21 */     return (new LayeredRegistryAccess(VALUES)).replaceFrom(STATIC, new RegistryAccess.Frozen[] { STATIC_ACCESS });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\RegistryLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
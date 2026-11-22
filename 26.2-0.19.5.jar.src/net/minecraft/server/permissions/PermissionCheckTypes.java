/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PermissionCheckTypes {
/*    */   public static MapCodec<? extends PermissionCheck> bootstrap(Registry<MapCodec<? extends PermissionCheck>> registry) {
/*  9 */     Registry.register(registry, Identifier.withDefaultNamespace("always_pass"), PermissionCheck.AlwaysPass.MAP_CODEC);
/* 10 */     return (MapCodec<? extends PermissionCheck>)Registry.register(registry, Identifier.withDefaultNamespace("require"), PermissionCheck.Require.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\PermissionCheckTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
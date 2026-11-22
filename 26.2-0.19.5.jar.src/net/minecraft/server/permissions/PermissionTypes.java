/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PermissionTypes {
/*    */   public static MapCodec<? extends Permission> bootstrap(Registry<MapCodec<? extends Permission>> registry) {
/*  9 */     Registry.register(registry, Identifier.withDefaultNamespace("atom"), Permission.Atom.MAP_CODEC);
/* 10 */     return (MapCodec<? extends Permission>)Registry.register(registry, Identifier.withDefaultNamespace("command_level"), Permission.HasCommandLevel.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\PermissionTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
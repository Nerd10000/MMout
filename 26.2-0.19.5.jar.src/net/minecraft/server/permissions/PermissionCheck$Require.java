/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class Require extends Record implements PermissionCheck {
/*    */   private final Permission permission;
/*    */   
/*    */   public static final MapCodec<Require> MAP_CODEC;
/*    */   
/*    */   public Require(Permission permission) {
/* 36 */     this.permission = permission;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/PermissionCheck$Require;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/PermissionCheck$Require;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/PermissionCheck$Require;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/permissions/PermissionCheck$Require;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Permission permission() {
/* 36 */     return this.permission;
/*    */   }
/*    */   
/*    */   static {
/* 39 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Permission.CODEC.fieldOf("permission").forGetter(Require::permission)).apply((Applicative)i, Require::new));
/*    */   }
/*    */   
/*    */   public MapCodec<Require> codec() {
/* 45 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public boolean check(PermissionSet source) {
/* 50 */     return source.hasPermission(this.permission);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\PermissionCheck$Require.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
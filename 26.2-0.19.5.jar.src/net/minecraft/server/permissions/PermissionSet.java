/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ public interface PermissionSet {
/*    */   public static final PermissionSet NO_PERMISSIONS = permission -> false;
/*    */   
/*    */   public static final PermissionSet ALL_PERMISSIONS = permission -> true;
/*    */   
/*    */   default PermissionSet union(PermissionSet other) {
/* 11 */     if (other instanceof PermissionSetUnion)
/* 12 */       return other.union(this); 
/* 14 */     return new PermissionSetUnion(this, other);
/*    */   }
/*    */   
/*    */   boolean hasPermission(Permission paramPermission);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\PermissionSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
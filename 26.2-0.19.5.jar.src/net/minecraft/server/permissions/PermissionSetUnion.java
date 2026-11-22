/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceSet;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class PermissionSetUnion implements PermissionSet {
/*  9 */   private final ReferenceSet<PermissionSet> permissions = (ReferenceSet<PermissionSet>)new ReferenceArraySet();
/*    */   
/*    */   PermissionSetUnion(PermissionSet first, PermissionSet second) {
/* 13 */     this.permissions.add(first);
/* 14 */     this.permissions.add(second);
/* 15 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> oldPermissions, PermissionSet other) {
/* 19 */     this.permissions.addAll((Collection)oldPermissions);
/* 20 */     this.permissions.add(other);
/* 21 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> oldPermissions, ReferenceSet<PermissionSet> other) {
/* 25 */     this.permissions.addAll((Collection)oldPermissions);
/* 26 */     this.permissions.addAll((Collection)other);
/* 27 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   
/*    */   public boolean hasPermission(Permission permission) {
/* 32 */     for (ObjectIterator<PermissionSet> objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) {
/* 32 */       PermissionSet set = objectIterator.next();
/* 33 */       if (set.hasPermission(permission))
/* 34 */         return true; 
/*    */     } 
/* 37 */     return false;
/*    */   }
/*    */   
/*    */   public PermissionSet union(PermissionSet other) {
/* 42 */     if (other instanceof PermissionSetUnion) {
/* 42 */       PermissionSetUnion otherUnion = (PermissionSetUnion)other;
/* 43 */       return new PermissionSetUnion(this.permissions, otherUnion.permissions);
/*    */     } 
/* 45 */     return new PermissionSetUnion(this.permissions, other);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public ReferenceSet<PermissionSet> getPermissions() {
/* 50 */     return (ReferenceSet<PermissionSet>)new ReferenceArraySet(this.permissions);
/*    */   }
/*    */   
/*    */   private void ensureNoUnionsWithinUnions() {
/* 54 */     for (ObjectIterator<PermissionSet> objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) {
/* 54 */       PermissionSet set = objectIterator.next();
/* 55 */       if (set instanceof PermissionSetUnion)
/* 56 */         throw new IllegalArgumentException("Cannot have PermissionSetUnion within another PermissionSetUnion"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\PermissionSetUnion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
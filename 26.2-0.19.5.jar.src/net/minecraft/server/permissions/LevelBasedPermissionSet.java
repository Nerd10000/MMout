/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ public interface LevelBasedPermissionSet extends PermissionSet {
/*    */   @Deprecated
/* 10 */   public static final LevelBasedPermissionSet ALL = create(PermissionLevel.ALL);
/*    */   
/* 11 */   public static final LevelBasedPermissionSet MODERATOR = create(PermissionLevel.MODERATORS);
/*    */   
/* 12 */   public static final LevelBasedPermissionSet GAMEMASTER = create(PermissionLevel.GAMEMASTERS);
/*    */   
/* 13 */   public static final LevelBasedPermissionSet ADMIN = create(PermissionLevel.ADMINS);
/*    */   
/* 14 */   public static final LevelBasedPermissionSet OWNER = create(PermissionLevel.OWNERS);
/*    */   
/*    */   PermissionLevel level();
/*    */   
/*    */   default boolean hasPermission(Permission permission) {
/* 20 */     if (permission instanceof Permission.HasCommandLevel) {
/* 20 */       Permission.HasCommandLevel levelCheck = (Permission.HasCommandLevel)permission;
/* 21 */       return level().isEqualOrHigherThan(levelCheck.level());
/*    */     } 
/* 24 */     if (permission.equals(Permissions.COMMANDS_ENTITY_SELECTORS))
/* 25 */       return level().isEqualOrHigherThan(PermissionLevel.GAMEMASTERS); 
/* 27 */     return false;
/*    */   }
/*    */   
/*    */   default PermissionSet union(PermissionSet other) {
/* 32 */     if (other instanceof LevelBasedPermissionSet) {
/* 32 */       LevelBasedPermissionSet otherSet = (LevelBasedPermissionSet)other;
/* 33 */       if (level().isEqualOrHigherThan(otherSet.level()))
/* 34 */         return otherSet; 
/* 36 */       return this;
/*    */     } 
/* 39 */     return super.union(other);
/*    */   }
/*    */   
/*    */   static LevelBasedPermissionSet forLevel(PermissionLevel level) {
/* 43 */     switch (level) {
/*    */       default:
/* 43 */         throw new MatchException(null, null);
/*    */       case ALL:
/*    */       
/*    */       case MODERATORS:
/*    */       
/*    */       case GAMEMASTERS:
/*    */       
/*    */       case ADMINS:
/*    */       
/*    */       case OWNERS:
/*    */         break;
/*    */     } 
/* 43 */     return 
/*    */       
/* 48 */       OWNER;
/*    */   }
/*    */   
/*    */   private static LevelBasedPermissionSet create(final PermissionLevel level) {
/* 53 */     return new LevelBasedPermissionSet() {
/*    */         public PermissionLevel level() {
/* 56 */           return level;
/*    */         }
/*    */         
/*    */         public String toString() {
/* 61 */           return "permission level: " + level.name();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\LevelBasedPermissionSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
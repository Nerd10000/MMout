/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface OwnableEntity {
/*    */   EntityReference<LivingEntity> getOwnerReference();
/*    */   
/*    */   Level level();
/*    */   
/*    */   default LivingEntity getOwner() {
/* 15 */     return EntityReference.getLivingEntity(getOwnerReference(), level());
/*    */   }
/*    */   
/*    */   default LivingEntity getRootOwner() {
/* 20 */     ObjectArraySet<OwnableEntity> objectArraySet = new ObjectArraySet();
/* 21 */     LivingEntity owner = getOwner();
/* 22 */     objectArraySet.add(this);
/* 23 */     while (owner instanceof OwnableEntity) {
/* 23 */       OwnableEntity ownableOwner = (OwnableEntity)owner;
/* 24 */       LivingEntity ownersOwner = ownableOwner.getOwner();
/* 25 */       if (objectArraySet.contains(ownersOwner))
/* 26 */         return null; 
/* 28 */       objectArraySet.add(owner);
/* 29 */       owner = ownableOwner.getOwner();
/*    */     } 
/* 31 */     return owner;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\OwnableEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
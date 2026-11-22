/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*    */ 
/*    */ public interface ContainerUser {
/*    */   boolean hasContainerOpen(ContainerOpenersCounter paramContainerOpenersCounter, BlockPos paramBlockPos);
/*    */   
/*    */   double getContainerInteractionRange();
/*    */   
/*    */   default LivingEntity getLivingEntity() {
/* 13 */     ContainerUser containerUser = this;
/* 13 */     if (containerUser instanceof LivingEntity) {
/* 13 */       LivingEntity livingEntity = (LivingEntity)containerUser;
/* 14 */       return livingEntity;
/*    */     } 
/* 16 */     throw new IllegalStateException("A container user must be a LivingEntity");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ContainerUser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
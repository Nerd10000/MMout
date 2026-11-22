/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ class null implements EntityTypeTest<B, T> {
/*    */   public T tryCast(B entity) {
/* 26 */     return cls.equals(entity.getClass()) ? (T)entity : null;
/*    */   }
/*    */   
/*    */   public Class<? extends B> getBaseClass() {
/* 31 */     return cls;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\EntityTypeTest$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
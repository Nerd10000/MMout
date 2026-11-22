/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ class null implements EntityTypeTest<B, T> {
/*    */   public T tryCast(B entity) {
/* 11 */     return cls.isInstance(entity) ? (T)entity : null;
/*    */   }
/*    */   
/*    */   public Class<? extends B> getBaseClass() {
/* 16 */     return cls;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\EntityTypeTest$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
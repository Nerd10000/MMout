/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ public interface EntityTypeTest<B, T extends B> {
/*    */   static <B, T extends B> EntityTypeTest<B, T> forClass(final Class<T> cls) {
/*  7 */     return new EntityTypeTest<B, T>() {
/*    */         public T tryCast(B entity) {
/* 11 */           return cls.isInstance(entity) ? (T)entity : null;
/*    */         }
/*    */         
/*    */         public Class<? extends B> getBaseClass() {
/* 16 */           return cls;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static <B, T extends B> EntityTypeTest<B, T> forExactClass(final Class<T> cls) {
/* 22 */     return new EntityTypeTest<B, T>() {
/*    */         public T tryCast(B entity) {
/* 26 */           return cls.equals(entity.getClass()) ? (T)entity : null;
/*    */         }
/*    */         
/*    */         public Class<? extends B> getBaseClass() {
/* 31 */           return cls;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   T tryCast(B paramB);
/*    */   
/*    */   Class<? extends B> getBaseClass();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\EntityTypeTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
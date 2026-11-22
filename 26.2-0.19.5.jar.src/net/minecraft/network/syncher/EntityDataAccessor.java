/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ public final class EntityDataAccessor<T> extends Record {
/*    */   private final int id;
/*    */   
/*    */   private final EntityDataSerializer<T> serializer;
/*    */   
/*    */   public EntityDataAccessor(int id, EntityDataSerializer<T> serializer) {
/*  3 */     this.id = id;
/*  3 */     this.serializer = serializer;
/*    */   }
/*    */   
/*    */   public int id() {
/*  3 */     return this.id;
/*    */   }
/*    */   
/*    */   public EntityDataSerializer<T> serializer() {
/*  3 */     return this.serializer;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/*  6 */     if (this == o)
/*  7 */       return true; 
/*  9 */     if (o == null || getClass() != o.getClass())
/* 10 */       return false; 
/* 13 */     EntityDataAccessor<?> that = (EntityDataAccessor)o;
/* 15 */     return (this.id == that.id);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 20 */     return this.id;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 25 */     return "<entity data: " + this.id + ">";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\EntityDataAccessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
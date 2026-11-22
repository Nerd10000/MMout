/*    */ package net.minecraft.core;
/*    */ 
/*    */ public interface IdMap<T> extends Iterable<T> {
/*    */   public static final int DEFAULT = -1;
/*    */   
/*    */   int getId(T paramT);
/*    */   
/*    */   T byId(int paramInt);
/*    */   
/*    */   default T byIdOrThrow(int id) {
/* 14 */     T result = byId(id);
/* 15 */     if (result == null)
/* 16 */       throw new IllegalArgumentException("No value with id " + id); 
/* 18 */     return result;
/*    */   }
/*    */   
/*    */   default int getIdOrThrow(T value) {
/* 22 */     int id = getId(value);
/* 23 */     if (id == -1)
/* 24 */       throw new IllegalArgumentException("Can't find id for '" + String.valueOf(value) + "' in map " + String.valueOf(this)); 
/* 26 */     return id;
/*    */   }
/*    */   
/*    */   int size();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\IdMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
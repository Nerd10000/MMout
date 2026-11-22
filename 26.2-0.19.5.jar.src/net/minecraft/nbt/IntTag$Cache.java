/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ class Cache {
/*    */   private static final int HIGH = 1024;
/*    */   
/*    */   private static final int LOW = -128;
/*    */   
/* 18 */   private static final IntTag[] cache = new IntTag[1153];
/*    */   
/*    */   static {
/* 21 */     for (int i = 0; i < cache.length; i++)
/* 22 */       cache[i] = new IntTag(-128 + i); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\IntTag$Cache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
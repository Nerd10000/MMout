/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ class Cache {
/* 16 */   private static final ByteTag[] cache = new ByteTag[256];
/*    */   
/*    */   static {
/* 19 */     for (int i = 0; i < cache.length; i++)
/* 20 */       cache[i] = new ByteTag((byte)(i - 128)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\ByteTag$Cache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
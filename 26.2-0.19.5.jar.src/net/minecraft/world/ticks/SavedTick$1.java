/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.Hash;
/*    */ 
/*    */ class null implements Hash.Strategy<SavedTick<?>> {
/*    */   public int hashCode(SavedTick<?> o) {
/* 37 */     return 31 * o.pos().hashCode() + o.type().hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(SavedTick<?> a, SavedTick<?> b) {
/* 42 */     if (a == b)
/* 43 */       return true; 
/* 45 */     if (a == null || b == null)
/* 46 */       return false; 
/* 48 */     return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\SavedTick$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
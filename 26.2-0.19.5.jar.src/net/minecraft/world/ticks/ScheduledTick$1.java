/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.Hash;
/*    */ 
/*    */ class null implements Hash.Strategy<ScheduledTick<?>> {
/*    */   public int hashCode(ScheduledTick<?> o) {
/* 43 */     return 31 * o.pos().hashCode() + o.type().hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(ScheduledTick<?> a, ScheduledTick<?> b) {
/* 48 */     if (a == b)
/* 49 */       return true; 
/* 51 */     if (a == null || b == null)
/* 52 */       return false; 
/* 54 */     return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\ScheduledTick$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public enum TickPriority {
/*  6 */   EXTREMELY_HIGH(-3),
/*  7 */   VERY_HIGH(-2),
/*  8 */   HIGH(-1),
/*  9 */   NORMAL(0),
/* 10 */   LOW(1),
/* 11 */   VERY_LOW(2),
/* 12 */   EXTREMELY_LOW(3);
/*    */   
/*    */   public static final Codec<TickPriority> CODEC;
/*    */   
/*    */   private final int value;
/*    */   
/*    */   static {
/* 15 */     CODEC = Codec.INT.xmap(TickPriority::byValue, TickPriority::getValue);
/*    */   }
/*    */   
/*    */   TickPriority(int value) {
/* 20 */     this.value = value;
/*    */   }
/*    */   
/*    */   public static TickPriority byValue(int value) {
/* 24 */     for (TickPriority priority : values()) {
/* 25 */       if (priority.value == value)
/* 26 */         return priority; 
/*    */     } 
/* 29 */     if (value < EXTREMELY_HIGH.value)
/* 30 */       return EXTREMELY_HIGH; 
/* 32 */     return EXTREMELY_LOW;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 36 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\TickPriority.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
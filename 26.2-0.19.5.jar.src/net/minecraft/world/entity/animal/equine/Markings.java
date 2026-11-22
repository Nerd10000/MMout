/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum Markings {
/*  8 */   NONE(0),
/*  9 */   WHITE(1),
/* 10 */   WHITE_FIELD(2),
/* 11 */   WHITE_DOTS(3),
/* 12 */   BLACK_DOTS(4);
/*    */   
/*    */   private static final IntFunction<Markings> BY_ID;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 15 */     BY_ID = ByIdMap.continuous(Markings::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*    */   }
/*    */   
/*    */   Markings(int id) {
/* 19 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 23 */     return this.id;
/*    */   }
/*    */   
/*    */   public static Markings byId(int id) {
/* 27 */     return BY_ID.apply(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Markings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.client;
/*    */ 
/*    */ public enum CameraType {
/*  4 */   FIRST_PERSON(true, false),
/*  5 */   THIRD_PERSON_BACK(false, false),
/*  6 */   THIRD_PERSON_FRONT(false, true);
/*    */   
/*    */   private static final CameraType[] VALUES;
/*    */   
/*    */   private final boolean firstPerson;
/*    */   
/*    */   private final boolean mirrored;
/*    */   
/*    */   static {
/*  9 */     VALUES = values();
/*    */   }
/*    */   
/*    */   CameraType(boolean firstPerson, boolean mirrored) {
/* 15 */     this.firstPerson = firstPerson;
/* 16 */     this.mirrored = mirrored;
/*    */   }
/*    */   
/*    */   public boolean isFirstPerson() {
/* 20 */     return this.firstPerson;
/*    */   }
/*    */   
/*    */   public boolean isMirrored() {
/* 24 */     return this.mirrored;
/*    */   }
/*    */   
/*    */   public CameraType cycle() {
/* 28 */     return VALUES[(ordinal() + 1) % VALUES.length];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\CameraType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
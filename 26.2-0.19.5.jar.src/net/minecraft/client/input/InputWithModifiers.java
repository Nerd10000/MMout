/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ public interface InputWithModifiers {
/*    */   public static final int NOT_DIGIT = -1;
/*    */   
/*    */   int input();
/*    */   
/*    */   int modifiers();
/*    */   
/*    */   default boolean isSelection() {
/* 21 */     return (input() == 257 || 
/* 22 */       input() == 32 || 
/* 23 */       input() == 335);
/*    */   }
/*    */   
/*    */   default boolean isConfirmation() {
/* 27 */     return (input() == 257 || 
/* 28 */       input() == 335);
/*    */   }
/*    */   
/*    */   default boolean isEscape() {
/* 32 */     return (input() == 256);
/*    */   }
/*    */   
/*    */   default boolean isLeft() {
/* 36 */     return (input() == 263);
/*    */   }
/*    */   
/*    */   default boolean isRight() {
/* 40 */     return (input() == 262);
/*    */   }
/*    */   
/*    */   default boolean isUp() {
/* 44 */     return (input() == 265);
/*    */   }
/*    */   
/*    */   default boolean isDown() {
/* 48 */     return (input() == 264);
/*    */   }
/*    */   
/*    */   default boolean isCycleFocus() {
/* 52 */     return (input() == 258);
/*    */   }
/*    */   
/*    */   default int getDigit() {
/* 57 */     int value = input() - 48;
/* 58 */     if (value >= 0 && value <= 9)
/* 59 */       return value; 
/* 61 */     return -1;
/*    */   }
/*    */   
/*    */   default boolean hasAltDown() {
/* 65 */     return ((modifiers() & 0x4) != 0);
/*    */   }
/*    */   
/*    */   default boolean hasShiftDown() {
/* 69 */     return ((modifiers() & 0x1) != 0);
/*    */   }
/*    */   
/*    */   default boolean hasControlDown() {
/* 73 */     return ((modifiers() & 0x2) != 0);
/*    */   }
/*    */   
/*    */   default boolean hasControlDownWithQuirk() {
/* 77 */     return ((modifiers() & InputQuirks.EDIT_SHORTCUT_KEY_MODIFIER) != 0);
/*    */   }
/*    */   
/*    */   default boolean isSelectAll() {
/* 81 */     return (input() == 65 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   default boolean isCopy() {
/* 85 */     return (input() == 67 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   default boolean isPaste() {
/* 89 */     return (input() == 86 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   default boolean isCut() {
/* 93 */     return (input() == 88 && hasControlDownWithQuirk() && !hasShiftDown() && !hasAltDown());
/*    */   }
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.TYPE_USE})
/*    */   public static @interface Modifiers {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\input\InputWithModifiers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
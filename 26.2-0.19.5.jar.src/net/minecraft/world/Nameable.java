/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface Nameable {
/*    */   Component getName();
/*    */   
/*    */   default String getPlainTextName() {
/* 10 */     return getName().getString();
/*    */   }
/*    */   
/*    */   default boolean hasCustomName() {
/* 14 */     return (getCustomName() != null);
/*    */   }
/*    */   
/*    */   default Component getDisplayName() {
/* 18 */     return getName();
/*    */   }
/*    */   
/*    */   default Component getCustomName() {
/* 22 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\Nameable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*   */ package net.minecraft.world.entity;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface EntityProcessor {
/*   */   public static final EntityProcessor NOP;
/*   */   
/*   */   Entity process(Entity paramEntity);
/*   */   
/*   */   static {
/* 7 */     NOP = (input -> input);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ public enum OutlineProperty {
/*  89 */   NONE("none"),
/*  90 */   IS_OUTLINE("is_outline"),
/*  91 */   AFFECTS_OUTLINE("affects_outline");
/*     */   
/*     */   private final String name;
/*     */   
/*     */   OutlineProperty(String name) {
/*  97 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 102 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\RenderSetup$OutlineProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
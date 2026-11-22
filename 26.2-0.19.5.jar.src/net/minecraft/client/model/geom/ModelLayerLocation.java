/*   */ package net.minecraft.client.model.geom;
/*   */ 
/*   */ import net.minecraft.resources.Identifier;
/*   */ 
/*   */ public final class ModelLayerLocation extends Record {
/*   */   private final Identifier model;
/*   */   
/*   */   private final String layer;
/*   */   
/*   */   public ModelLayerLocation(Identifier model, String layer) {
/* 5 */     this.model = model;
/* 5 */     this.layer = layer;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelLayerLocation;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelLayerLocation;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelLayerLocation;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelLayerLocation;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Identifier model() {
/* 5 */     return this.model;
/*   */   }
/*   */   
/*   */   public String layer() {
/* 5 */     return this.layer;
/*   */   }
/*   */   
/*   */   public String toString() {
/* 8 */     return String.valueOf(this.model) + "#" + String.valueOf(this.model);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\ModelLayerLocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
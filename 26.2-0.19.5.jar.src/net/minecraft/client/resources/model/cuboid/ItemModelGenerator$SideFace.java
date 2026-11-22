/*     */ package net.minecraft.client.resources.model.cuboid;
/*     */ 
/*     */ final class SideFace extends Record {
/*     */   private final ItemModelGenerator.SideDirection facing;
/*     */   
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   private SideFace(ItemModelGenerator.SideDirection facing, int x, int y) {
/* 224 */     this.facing = facing;
/* 224 */     this.x = x;
/* 224 */     this.y = y;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #224	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #224	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #224	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$SideFace;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ItemModelGenerator.SideDirection facing() {
/* 224 */     return this.facing;
/*     */   }
/*     */   
/*     */   public int x() {
/* 224 */     return this.x;
/*     */   }
/*     */   
/*     */   public int y() {
/* 224 */     return this.y;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemModelGenerator$SideFace.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
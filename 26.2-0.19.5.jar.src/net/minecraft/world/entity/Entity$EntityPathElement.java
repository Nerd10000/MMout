/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ 
/*      */ final class EntityPathElement extends Record implements ProblemReporter.PathElement {
/*      */   private final Entity entity;
/*      */   
/*      */   private EntityPathElement(Entity entity) {
/* 4397 */     this.entity = entity;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Entity$EntityPathElement;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #4397	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Entity$EntityPathElement;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #4397	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Entity$EntityPathElement;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #4397	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public Entity entity() {
/* 4397 */     return this.entity;
/*      */   }
/*      */   
/*      */   public String get() {
/* 4400 */     return this.entity.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Entity$EntityPathElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
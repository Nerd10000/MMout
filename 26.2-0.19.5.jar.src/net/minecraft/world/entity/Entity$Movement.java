/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ final class Movement extends Record {
/*     */   private final Vec3 from;
/*     */   
/*     */   private final Vec3 to;
/*     */   
/*     */   private final Optional<Vec3> axisDependentOriginalMovement;
/*     */   
/*     */   private Movement(Vec3 from, Vec3 to, Optional<Vec3> axisDependentOriginalMovement) {
/* 340 */     this.from = from;
/* 340 */     this.to = to;
/* 340 */     this.axisDependentOriginalMovement = axisDependentOriginalMovement;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Entity$Movement;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #340	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Entity$Movement;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Entity$Movement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #340	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Entity$Movement;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Entity$Movement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #340	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Entity$Movement;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Vec3 from() {
/* 340 */     return this.from;
/*     */   }
/*     */   
/*     */   public Vec3 to() {
/* 340 */     return this.to;
/*     */   }
/*     */   
/*     */   public Optional<Vec3> axisDependentOriginalMovement() {
/* 340 */     return this.axisDependentOriginalMovement;
/*     */   }
/*     */   
/*     */   public Movement(Vec3 from, Vec3 to, Vec3 axisDependentOriginalMovement) {
/* 342 */     this(from, to, Optional.of(axisDependentOriginalMovement));
/*     */   }
/*     */   
/*     */   public Movement(Vec3 from, Vec3 to) {
/* 346 */     this(from, to, Optional.empty());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Entity$Movement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
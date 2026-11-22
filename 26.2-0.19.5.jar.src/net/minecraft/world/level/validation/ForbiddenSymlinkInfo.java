/*   */ package net.minecraft.world.level.validation;
/*   */ 
/*   */ import java.nio.file.Path;
/*   */ 
/*   */ public final class ForbiddenSymlinkInfo extends Record {
/*   */   private final Path link;
/*   */   
/*   */   private final Path target;
/*   */   
/*   */   public ForbiddenSymlinkInfo(Path link, Path target) {
/* 5 */     this.link = link;
/* 5 */     this.target = target;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Path link() {
/* 5 */     return this.link;
/*   */   }
/*   */   
/*   */   public Path target() {
/* 5 */     return this.target;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\validation\ForbiddenSymlinkInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
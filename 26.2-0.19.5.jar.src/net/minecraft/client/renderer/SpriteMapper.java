/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class SpriteMapper extends Record {
/*    */   private final Identifier sheet;
/*    */   
/*    */   private final String prefix;
/*    */   
/*    */   public SpriteMapper(Identifier sheet, String prefix) {
/*  6 */     this.sheet = sheet;
/*  6 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SpriteMapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/SpriteMapper;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SpriteMapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/SpriteMapper;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SpriteMapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/SpriteMapper;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier sheet() {
/*  6 */     return this.sheet;
/*    */   }
/*    */   
/*    */   public String prefix() {
/*  6 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public SpriteId apply(Identifier path) {
/* 11 */     return new SpriteId(this.sheet, path.withPrefix(this.prefix + "/"));
/*    */   }
/*    */   
/*    */   public SpriteId defaultNamespaceApply(String path) {
/* 15 */     return apply(Identifier.withDefaultNamespace(path));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SpriteMapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
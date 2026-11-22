/*    */ package net.minecraft.client.resources.model.sprite;
/*    */ 
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ 
/*    */ public final class Baked extends Record {
/*    */   private final TextureAtlasSprite sprite;
/*    */   
/*    */   private final boolean forceTranslucent;
/*    */   
/*    */   public Baked(TextureAtlasSprite sprite, boolean forceTranslucent) {
/* 33 */     this.sprite = sprite;
/* 33 */     this.forceTranslucent = forceTranslucent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/Material$Baked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/Material$Baked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/Material$Baked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/Material$Baked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/Material$Baked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/Material$Baked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite sprite() {
/* 33 */     return this.sprite;
/*    */   }
/*    */   
/*    */   public boolean forceTranslucent() {
/* 33 */     return this.forceTranslucent;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\Material$Baked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
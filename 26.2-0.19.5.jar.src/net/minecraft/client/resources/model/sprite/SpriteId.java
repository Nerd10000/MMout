/*    */ package net.minecraft.client.resources.model.sprite;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class SpriteId extends Record {
/*    */   private final Identifier atlasLocation;
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   public SpriteId(Identifier atlasLocation, Identifier texture) {
/*  8 */     this.atlasLocation = atlasLocation;
/*  8 */     this.texture = texture;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/SpriteId;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/SpriteId;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/SpriteId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/SpriteId;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/SpriteId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/SpriteId;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier atlasLocation() {
/*  8 */     return this.atlasLocation;
/*    */   }
/*    */   
/*    */   public Identifier texture() {
/*  8 */     return this.texture;
/*    */   }
/*    */   
/*    */   public RenderType renderType(Function<Identifier, RenderType> renderType) {
/* 13 */     return renderType.apply(this.atlasLocation);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\SpriteId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
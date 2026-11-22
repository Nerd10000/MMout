/*    */ package net.minecraft.client.resources.model.sprite;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class Data extends Record {
/*    */   private final Map<String, TextureSlots.SlotContents> values;
/*    */   
/*    */   public Data(Map<String, TextureSlots.SlotContents> values) {
/* 72 */     this.values = values;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, TextureSlots.SlotContents> values() {
/* 72 */     return this.values;
/*    */   }
/*    */   
/* 73 */   public static final Data EMPTY = new Data(Map.of());
/*    */   
/*    */   public static class Builder {
/* 76 */     private final Map<String, TextureSlots.SlotContents> textureMap = new HashMap<>();
/*    */     
/*    */     public Builder addReference(String slot, String reference) {
/* 79 */       this.textureMap.put(slot, new TextureSlots.Reference(reference));
/* 80 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addTexture(String slot, Material material) {
/* 84 */       this.textureMap.put(slot, new TextureSlots.Value(material));
/* 85 */       return this;
/*    */     }
/*    */     
/*    */     public TextureSlots.Data build() {
/* 89 */       if (this.textureMap.isEmpty())
/* 90 */         return TextureSlots.Data.EMPTY; 
/* 93 */       return new TextureSlots.Data(Map.copyOf(this.textureMap));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\TextureSlots$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
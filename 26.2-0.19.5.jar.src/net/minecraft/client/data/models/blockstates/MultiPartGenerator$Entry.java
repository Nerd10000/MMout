/*    */ package net.minecraft.client.data.models.blockstates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.data.models.MultiVariant;
/*    */ import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
/*    */ import net.minecraft.client.renderer.block.dispatch.multipart.Selector;
/*    */ 
/*    */ final class Entry extends Record {
/*    */   private final Optional<Condition> condition;
/*    */   
/*    */   private final MultiVariant variants;
/*    */   
/*    */   private Entry(Optional<Condition> condition, MultiVariant variants) {
/* 57 */     this.condition = condition;
/* 57 */     this.variants = variants;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/data/models/blockstates/MultiPartGenerator$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Condition> condition() {
/* 57 */     return this.condition;
/*    */   }
/*    */   
/*    */   public MultiVariant variants() {
/* 57 */     return this.variants;
/*    */   }
/*    */   
/*    */   public Selector toUnbaked() {
/* 62 */     return new Selector(this.condition, this.variants.toUnbaked());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\MultiPartGenerator$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
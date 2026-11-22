/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.data.models.MultiVariant;
/*     */ import net.minecraft.client.renderer.block.dispatch.VariantMutator;
/*     */ 
/*     */ final class Entry extends Record {
/*     */   private final PropertyValueList properties;
/*     */   
/*     */   private final MultiVariant variant;
/*     */   
/*     */   private Entry(PropertyValueList properties, MultiVariant variant) {
/* 106 */     this.properties = properties;
/* 106 */     this.variant = variant;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/data/models/blockstates/MultiVariantGenerator$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public PropertyValueList properties() {
/* 106 */     return this.properties;
/*     */   }
/*     */   
/*     */   public MultiVariant variant() {
/* 106 */     return this.variant;
/*     */   }
/*     */   
/*     */   public Stream<Entry> apply(PropertyDispatch<VariantMutator> stage) {
/* 112 */     return stage.getEntries().entrySet().stream().map(property -> {
/*     */           PropertyValueList newSelector = this.properties.extend((PropertyValueList)property.getKey());
/*     */           MultiVariant newVariants = this.variant.with((VariantMutator)property.getValue());
/*     */           return new Entry(newSelector, newVariants);
/*     */         });
/*     */   }
/*     */   
/*     */   public Stream<Entry> apply(VariantMutator mutator) {
/* 120 */     return Stream.of(new Entry(this.properties, this.variant.with(mutator)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\MultiVariantGenerator$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
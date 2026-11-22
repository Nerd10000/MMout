/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Comparator;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public final class Entry extends Record {
/*     */   private final float threshold;
/*     */   
/*     */   private final ItemModel.Unbaked model;
/*     */   
/*     */   public static final Codec<Entry> CODEC;
/*     */   
/*     */   public Entry(float threshold, ItemModel.Unbaked model) {
/* 119 */     this.threshold = threshold;
/* 119 */     this.model = model;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #119	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #119	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #119	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/RangeSelectItemModel$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float threshold() {
/* 119 */     return this.threshold;
/*     */   }
/*     */   
/*     */   public ItemModel.Unbaked model() {
/* 119 */     return this.model;
/*     */   }
/*     */   
/*     */   static {
/* 120 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("threshold").forGetter(Entry::threshold), (App)ItemModels.CODEC.fieldOf("model").forGetter(Entry::model)).apply((Applicative)i, Entry::new));
/*     */   }
/*     */   
/* 125 */   public static final Comparator<Entry> BY_THRESHOLD = Comparator.comparingDouble(Entry::threshold);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\RangeSelectItemModel$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
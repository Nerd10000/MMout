/*     */ package net.minecraft.client.renderer.item;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class SwitchCase<T> extends Record {
/*     */   private final List<T> values;
/*     */   
/*     */   private final ItemModel.Unbaked model;
/*     */   
/*     */   public SwitchCase(List<T> values, ItemModel.Unbaked model) {
/* 127 */     this.values = values;
/* 127 */     this.model = model;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$SwitchCase<TT;>;
/*     */   }
/*     */   
/*     */   public List<T> values() {
/* 127 */     return this.values;
/*     */   }
/*     */   
/*     */   public ItemModel.Unbaked model() {
/* 127 */     return this.model;
/*     */   }
/*     */   
/*     */   public static <T> Codec<SwitchCase<T>> codec(Codec<T> valueCodec) {
/* 132 */     return RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(valueCodec)).fieldOf("when").forGetter(SwitchCase::values), (App)ItemModels.CODEC.fieldOf("model").forGetter(SwitchCase::model)).apply((Applicative)i, SwitchCase::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\SelectItemModel$SwitchCase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
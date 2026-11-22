/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ 
/*     */ final class Slot extends Record {
/*     */   private final int index;
/*     */   
/*     */   private final ItemStackTemplate item;
/*     */   
/*     */   public static final Codec<Slot> CODEC;
/*     */   
/*     */   private Slot(int index, ItemStackTemplate item) {
/* 173 */     this.index = index;
/* 173 */     this.item = item;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #173	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #173	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemContainerContents$Slot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #173	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/ItemContainerContents$Slot;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int index() {
/* 173 */     return this.index;
/*     */   }
/*     */   
/*     */   public ItemStackTemplate item() {
/* 173 */     return this.item;
/*     */   }
/*     */   
/*     */   static {
/* 174 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, 255).fieldOf("slot").forGetter(Slot::index), (App)ItemStackTemplate.CODEC.fieldOf("item").forGetter(Slot::item)).apply((Applicative)i, Slot::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ItemContainerContents$Slot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
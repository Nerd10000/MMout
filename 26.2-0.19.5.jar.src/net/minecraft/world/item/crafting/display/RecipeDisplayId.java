/*   */ package net.minecraft.world.item.crafting.display;
/*   */ 
/*   */ import io.netty.buffer.ByteBuf;
/*   */ import java.util.function.Function;
/*   */ import net.minecraft.network.codec.ByteBufCodecs;
/*   */ import net.minecraft.network.codec.StreamCodec;
/*   */ 
/*   */ public final class RecipeDisplayId extends Record {
/*   */   private final int index;
/*   */   
/*   */   public RecipeDisplayId(int index) {
/* 7 */     this.index = index;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public int index() {
/* 7 */     return this.index;
/*   */   }
/*   */   
/* 8 */   public static final StreamCodec<ByteBuf, RecipeDisplayId> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, RecipeDisplayId::index, RecipeDisplayId::new);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\RecipeDisplayId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
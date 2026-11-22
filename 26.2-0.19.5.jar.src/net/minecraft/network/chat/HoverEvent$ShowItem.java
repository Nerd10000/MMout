/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ 
/*    */ public final class ShowItem extends Record implements HoverEvent {
/*    */   private final ItemStackTemplate item;
/*    */   
/*    */   public ShowItem(ItemStackTemplate item) {
/* 36 */     this.item = item;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStackTemplate item() {
/* 36 */     return this.item;
/*    */   }
/*    */   
/* 37 */   public static final MapCodec<ShowItem> CODEC = ItemStackTemplate.MAP_CODEC.xmap(ShowItem::new, ShowItem::item);
/*    */   
/*    */   public HoverEvent.Action action() {
/* 41 */     return HoverEvent.Action.SHOW_ITEM;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\HoverEvent$ShowItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
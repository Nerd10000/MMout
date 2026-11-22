/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ 
/*    */ public final class SulfurCubeContent extends Record implements TooltipProvider {
/*    */   private final ItemStackTemplate absorbedBlockItemStack;
/*    */   
/*    */   public SulfurCubeContent(ItemStackTemplate absorbedBlockItemStack) {
/* 16 */     this.absorbedBlockItemStack = absorbedBlockItemStack;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SulfurCubeContent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SulfurCubeContent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SulfurCubeContent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SulfurCubeContent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SulfurCubeContent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SulfurCubeContent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStackTemplate absorbedBlockItemStack() {
/* 16 */     return this.absorbedBlockItemStack;
/*    */   }
/*    */   
/* 18 */   public static final Codec<SulfurCubeContent> CODEC = ItemStackTemplate.CODEC.xmap(SulfurCubeContent::new, SulfurCubeContent::absorbedBlockItemStack);
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, SulfurCubeContent> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.map(SulfurCubeContent::new, SulfurCubeContent::absorbedBlockItemStack);
/*    */   
/*    */   public static SulfurCubeContent ofNonEmpty(ItemStack itemStack) {
/* 22 */     return new SulfurCubeContent(ItemStackTemplate.fromNonEmptyStack(itemStack));
/*    */   }
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 27 */     ItemStack currentStack = this.absorbedBlockItemStack.create();
/* 28 */     consumer.accept(Component.translatable("entity.minecraft.sulfur_cube.content", new Object[] { currentStack.getHoverName() }).withStyle(new ChatFormatting[] { ChatFormatting.ITALIC, ChatFormatting.GRAY }));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\SulfurCubeContent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
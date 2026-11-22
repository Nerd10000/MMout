/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ 
/*    */ public final class InstrumentComponent extends Record implements TooltipProvider {
/*    */   private final Holder<Instrument> instrument;
/*    */   
/*    */   public InstrumentComponent(Holder<Instrument> instrument) {
/* 18 */     this.instrument = instrument;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/InstrumentComponent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/InstrumentComponent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/InstrumentComponent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/InstrumentComponent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/InstrumentComponent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/InstrumentComponent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Instrument> instrument() {
/* 18 */     return this.instrument;
/*    */   }
/*    */   
/* 21 */   public static final Codec<InstrumentComponent> CODEC = Instrument.CODEC.xmap(InstrumentComponent::new, InstrumentComponent::instrument);
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, InstrumentComponent> STREAM_CODEC = Instrument.STREAM_CODEC.map(InstrumentComponent::new, InstrumentComponent::instrument);
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 26 */     consumer.accept(ComponentUtils.mergeStyles(((Instrument)this.instrument.value()).description(), Style.EMPTY.withColor(ChatFormatting.GRAY)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\InstrumentComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ 
/*    */ public final class Bees extends Record implements TooltipProvider {
/*    */   private final List<BeehiveBlockEntity.Occupant> bees;
/*    */   
/*    */   public Bees(List<BeehiveBlockEntity.Occupant> bees) {
/* 17 */     this.bees = bees;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Bees;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Bees;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Bees;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Bees;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Bees;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/Bees;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<BeehiveBlockEntity.Occupant> bees() {
/* 17 */     return this.bees;
/*    */   }
/*    */   
/* 18 */   public static final Codec<Bees> CODEC = BeehiveBlockEntity.Occupant.LIST_CODEC.xmap(Bees::new, Bees::bees);
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, Bees> STREAM_CODEC = BeehiveBlockEntity.Occupant.STREAM_CODEC.apply(ByteBufCodecs.list()).map(Bees::new, Bees::bees);
/*    */   
/* 21 */   public static final Bees EMPTY = new Bees(List.of());
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 25 */     consumer.accept(Component.translatable("container.beehive.bees", new Object[] { Integer.valueOf(this.bees.size()), Integer.valueOf(3) }).withStyle(ChatFormatting.GRAY));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\Bees.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ 
/*    */ public final class DyedItemColor extends Record implements TooltipProvider {
/*    */   private final int rgb;
/*    */   
/*    */   public DyedItemColor(int rgb) {
/* 23 */     this.rgb = rgb;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/DyedItemColor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DyedItemColor;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/DyedItemColor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DyedItemColor;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/DyedItemColor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/DyedItemColor;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int rgb() {
/* 23 */     return this.rgb;
/*    */   }
/*    */   
/* 24 */   public static final Codec<DyedItemColor> CODEC = ExtraCodecs.RGB_COLOR_CODEC.xmap(DyedItemColor::new, DyedItemColor::rgb);
/*    */   
/* 26 */   public static final StreamCodec<ByteBuf, DyedItemColor> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, DyedItemColor::rgb, DyedItemColor::new);
/*    */   
/*    */   public static final int LEATHER_COLOR = -6265536;
/*    */   
/*    */   public static int getOrDefault(ItemStack itemStack, int defaultColor) {
/* 34 */     DyedItemColor color = (DyedItemColor)itemStack.get(DataComponents.DYED_COLOR);
/* 35 */     return (color != null) ? ARGB.opaque(color.rgb()) : defaultColor;
/*    */   }
/*    */   
/*    */   public static ItemStack applyDyes(ItemStack itemStack, List<DyeColor> dyes) {
/* 39 */     DyedItemColor currentDye = (DyedItemColor)itemStack.get(DataComponents.DYED_COLOR);
/* 40 */     DyedItemColor newDyedColor = applyDyes(currentDye, dyes);
/* 42 */     ItemStack result = itemStack.copyWithCount(1);
/* 43 */     result.set(DataComponents.DYED_COLOR, newDyedColor);
/* 44 */     return result;
/*    */   }
/*    */   
/*    */   public static DyedItemColor applyDyes(DyedItemColor currentDye, List<DyeColor> dyes) {
/* 48 */     int redTotal = 0;
/* 49 */     int greenTotal = 0;
/* 50 */     int blueTotal = 0;
/* 51 */     int intensityTotal = 0;
/* 52 */     int colorCount = 0;
/* 54 */     if (currentDye != null) {
/* 55 */       int i = ARGB.red(currentDye.rgb());
/* 56 */       int j = ARGB.green(currentDye.rgb());
/* 57 */       int k = ARGB.blue(currentDye.rgb());
/* 58 */       intensityTotal += Math.max(i, Math.max(j, k));
/* 59 */       redTotal += i;
/* 60 */       greenTotal += j;
/* 61 */       blueTotal += k;
/* 62 */       colorCount++;
/*    */     } 
/* 65 */     for (DyeColor dye : dyes) {
/* 66 */       int color = dye.getTextureDiffuseColor();
/* 67 */       int i = ARGB.red(color);
/* 68 */       int j = ARGB.green(color);
/* 69 */       int k = ARGB.blue(color);
/* 71 */       intensityTotal += Math.max(i, Math.max(j, k));
/* 73 */       redTotal += i;
/* 74 */       greenTotal += j;
/* 75 */       blueTotal += k;
/* 76 */       colorCount++;
/*    */     } 
/* 79 */     int red = redTotal / colorCount;
/* 80 */     int green = greenTotal / colorCount;
/* 81 */     int blue = blueTotal / colorCount;
/* 83 */     float averageIntensity = intensityTotal / colorCount;
/* 84 */     float resultIntensity = Math.max(red, Math.max(green, blue));
/* 86 */     red = (int)(red * averageIntensity / resultIntensity);
/* 87 */     green = (int)(green * averageIntensity / resultIntensity);
/* 88 */     blue = (int)(blue * averageIntensity / resultIntensity);
/* 90 */     int rgb = ARGB.color(0, red, green, blue);
/* 91 */     return new DyedItemColor(rgb);
/*    */   }
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 96 */     if (flag.isAdvanced()) {
/* 97 */       consumer.accept(Component.translatable("item.color", new Object[] { String.format(Locale.ROOT, "#%06X", new Object[] { Integer.valueOf(this.rgb) }) }).withStyle(ChatFormatting.GRAY));
/*    */     } else {
/* 99 */       consumer.accept(Component.translatable("item.dyed").withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\DyedItemColor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class ChargedProjectiles extends Record implements TooltipProvider {
/*    */   private final List<ItemStackTemplate> items;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ChargedProjectiles;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/ChargedProjectiles;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ChargedProjectiles;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/ChargedProjectiles;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ChargedProjectiles;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/ChargedProjectiles;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<ItemStackTemplate> items() {
/* 22 */     return this.items;
/*    */   }
/*    */   
/* 25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final int MAX_SIZE = 1024;
/*    */   
/* 28 */   public static final ChargedProjectiles EMPTY = new ChargedProjectiles(List.of());
/*    */   
/*    */   public static final Codec<ChargedProjectiles> CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, ChargedProjectiles> STREAM_CODEC;
/*    */   
/*    */   static {
/* 30 */     CODEC = ItemStackTemplate.CODEC.sizeLimitedListOf(1024).xmap(ChargedProjectiles::new, projectiles -> projectiles.items);
/* 33 */     STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list(1024)).map(ChargedProjectiles::new, projectiles -> projectiles.items);
/*    */   }
/*    */   
/*    */   public ChargedProjectiles(List<ItemStackTemplate> items) {
/* 36 */     if (items.size() > 1024)
/* 37 */       throw new IllegalArgumentException("Got " + items.size() + " items, but maximum is 1024"); 
/*    */     this.items = items;
/*    */   }
/*    */   
/*    */   public static ChargedProjectiles of(ItemStackTemplate stack) {
/* 42 */     return new ChargedProjectiles(List.of(stack));
/*    */   }
/*    */   
/*    */   public static ChargedProjectiles ofNonEmpty(List<ItemStack> items) {
/* 46 */     List<ItemStackTemplate> list = items.stream().filter(i -> !i.isEmpty()).map(ItemStackTemplate::fromStack).limit(1024L).toList();
/* 47 */     if (list.size() != items.size())
/* 48 */       LOGGER.warn("Tried to load invalid items as charged projectiles"); 
/* 50 */     return new ChargedProjectiles(list);
/*    */   }
/*    */   
/*    */   public boolean contains(Item item) {
/* 54 */     for (ItemStackTemplate projectile : this.items) {
/* 55 */       if (projectile.is(item))
/* 56 */         return true; 
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */   
/*    */   public List<ItemStack> itemCopies() {
/* 63 */     return Lists.transform(this.items, ItemStackTemplate::create);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 67 */     return this.items.isEmpty();
/*    */   }
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 72 */     ItemStack current = null;
/* 73 */     int count = 0;
/* 74 */     for (ItemStackTemplate projectileTemplate : this.items) {
/* 75 */       ItemStack projectile = projectileTemplate.create();
/* 76 */       if (current == null) {
/* 77 */         current = projectile;
/* 78 */         count = 1;
/*    */         continue;
/*    */       } 
/* 79 */       if (ItemStack.matches(current, projectile)) {
/* 80 */         count++;
/*    */         continue;
/*    */       } 
/* 82 */       addProjectileTooltip(context, consumer, current, count);
/* 83 */       current = projectile;
/* 84 */       count = 1;
/*    */     } 
/* 87 */     if (current != null)
/* 88 */       addProjectileTooltip(context, consumer, current, count); 
/*    */   }
/*    */   
/*    */   private static void addProjectileTooltip(Item.TooltipContext context, Consumer<Component> consumer, ItemStack projectile, int count) {
/* 93 */     if (count == 1) {
/* 94 */       consumer.accept(Component.translatable("item.minecraft.crossbow.projectile.single", new Object[] { projectile.getDisplayName() }));
/*    */     } else {
/* 96 */       consumer.accept(Component.translatable("item.minecraft.crossbow.projectile.multiple", new Object[] { Integer.valueOf(count), projectile.getDisplayName() }));
/*    */     } 
/* 98 */     TooltipDisplay projectileDisplay = (TooltipDisplay)projectile.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/* 99 */     projectile.addDetailsToTooltip(context, projectileDisplay, null, (TooltipFlag)TooltipFlag.NORMAL, line -> consumer.accept(Component.literal("  ").append(line).withStyle(ChatFormatting.GRAY)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ChargedProjectiles.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
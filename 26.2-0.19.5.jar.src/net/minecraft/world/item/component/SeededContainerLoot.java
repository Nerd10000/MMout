/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class SeededContainerLoot extends Record implements TooltipProvider {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   
/*    */   private final long seed;
/*    */   
/*    */   public SeededContainerLoot(ResourceKey<LootTable> lootTable, long seed) {
/* 14 */     this.lootTable = lootTable;
/* 14 */     this.seed = seed;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SeededContainerLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SeededContainerLoot;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SeededContainerLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SeededContainerLoot;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SeededContainerLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SeededContainerLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<LootTable> lootTable() {
/* 14 */     return this.lootTable;
/*    */   }
/*    */   
/*    */   public long seed() {
/* 14 */     return this.seed;
/*    */   }
/*    */   
/* 15 */   private static final Component UNKNOWN_CONTENTS = (Component)Component.translatable("item.container.loot_table.unknown");
/*    */   
/*    */   public static final Codec<SeededContainerLoot> CODEC;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(SeededContainerLoot::lootTable), (App)Codec.LONG.optionalFieldOf("seed", Long.valueOf(0L)).forGetter(SeededContainerLoot::seed)).apply((Applicative)i, SeededContainerLoot::new));
/*    */   }
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 24 */     consumer.accept(UNKNOWN_CONTENTS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\SeededContainerLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
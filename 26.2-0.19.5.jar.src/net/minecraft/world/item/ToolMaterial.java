/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*    */ import net.minecraft.world.item.component.Tool;
/*    */ import net.minecraft.world.item.component.Weapon;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public final class ToolMaterial extends Record {
/*    */   private final TagKey<Block> incorrectBlocksForDrops;
/*    */   
/*    */   private final int durability;
/*    */   
/*    */   private final float speed;
/*    */   
/*    */   private final float attackDamageBonus;
/*    */   
/*    */   private final int enchantmentValue;
/*    */   
/*    */   private final TagKey<Item> repairItems;
/*    */   
/*    */   public ToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
/* 22 */     this.incorrectBlocksForDrops = incorrectBlocksForDrops;
/* 22 */     this.durability = durability;
/* 22 */     this.speed = speed;
/* 22 */     this.attackDamageBonus = attackDamageBonus;
/* 22 */     this.enchantmentValue = enchantmentValue;
/* 22 */     this.repairItems = repairItems;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/ToolMaterial;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/ToolMaterial;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/ToolMaterial;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/ToolMaterial;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/ToolMaterial;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/ToolMaterial;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public TagKey<Block> incorrectBlocksForDrops() {
/* 22 */     return this.incorrectBlocksForDrops;
/*    */   }
/*    */   
/*    */   public int durability() {
/* 22 */     return this.durability;
/*    */   }
/*    */   
/*    */   public float speed() {
/* 22 */     return this.speed;
/*    */   }
/*    */   
/*    */   public float attackDamageBonus() {
/* 22 */     return this.attackDamageBonus;
/*    */   }
/*    */   
/*    */   public int enchantmentValue() {
/* 22 */     return this.enchantmentValue;
/*    */   }
/*    */   
/*    */   public TagKey<Item> repairItems() {
/* 22 */     return this.repairItems;
/*    */   }
/*    */   
/* 30 */   public static final ToolMaterial WOOD = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 59, 2.0F, 0.0F, 15, ItemTags.WOODEN_TOOL_MATERIALS);
/*    */   
/* 31 */   public static final ToolMaterial STONE = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 5, ItemTags.STONE_TOOL_MATERIALS);
/*    */   
/* 32 */   public static final ToolMaterial COPPER = new ToolMaterial(BlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 1.0F, 13, ItemTags.COPPER_TOOL_MATERIALS);
/*    */   
/* 33 */   public static final ToolMaterial IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0F, 2.0F, 14, ItemTags.IRON_TOOL_MATERIALS);
/*    */   
/* 34 */   public static final ToolMaterial DIAMOND = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0F, 3.0F, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
/*    */   
/* 35 */   public static final ToolMaterial GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_GOLD_TOOL, 32, 12.0F, 0.0F, 22, ItemTags.GOLD_TOOL_MATERIALS);
/*    */   
/* 36 */   public static final ToolMaterial NETHERITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);
/*    */   
/*    */   private Item.Properties applyCommonProperties(Item.Properties properties) {
/* 39 */     return properties
/* 40 */       .durability(this.durability)
/* 41 */       .repairable(this.repairItems)
/* 42 */       .enchantable(this.enchantmentValue);
/*    */   }
/*    */   
/*    */   public Item.Properties applyToolProperties(Item.Properties properties, TagKey<Block> minesEfficiently, float attackDamageBaseline, float attackSpeedBaseline, float disableBlockingSeconds) {
/* 47 */     HolderGetter<Block> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.BLOCK);
/* 48 */     return applyCommonProperties(properties)
/* 49 */       .<Tool>component(DataComponents.TOOL, new Tool(
/* 50 */           List.of(
/* 51 */             Tool.Rule.deniesDrops((HolderSet)registrationLookup.getOrThrow(this.incorrectBlocksForDrops)), 
/* 52 */             Tool.Rule.minesAndDrops((HolderSet)registrationLookup.getOrThrow(minesEfficiently), this.speed)), 1.0F, 1, true))
/*    */       
/* 58 */       .attributes(createToolAttributes(attackDamageBaseline, attackSpeedBaseline))
/* 59 */       .component(DataComponents.WEAPON, new Weapon(2, disableBlockingSeconds));
/*    */   }
/*    */   
/*    */   private ItemAttributeModifiers createToolAttributes(float attackDamageBaseline, float attackSpeedBaseline) {
/* 63 */     return ItemAttributeModifiers.builder()
/* 64 */       .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (attackDamageBaseline + this.attackDamageBonus), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 65 */       .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedBaseline, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 66 */       .build();
/*    */   }
/*    */   
/*    */   public Item.Properties applySwordProperties(Item.Properties properties, float attackDamageBaseline, float attackSpeedBaseline) {
/* 71 */     HolderGetter<Block> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.BLOCK);
/* 72 */     return applyCommonProperties(properties)
/* 73 */       .<Tool>component(DataComponents.TOOL, new Tool(
/* 74 */           List.of(
/* 75 */             Tool.Rule.minesAndDrops((HolderSet)HolderSet.direct(new Holder[] { (Holder)Blocks.COBWEB.builtInRegistryHolder() }, ), 15.0F), 
/* 76 */             Tool.Rule.overrideSpeed((HolderSet)registrationLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), 
/* 77 */             Tool.Rule.overrideSpeed((HolderSet)registrationLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)), 1.0F, 2, false))
/*    */       
/* 83 */       .attributes(createSwordAttributes(attackDamageBaseline, attackSpeedBaseline))
/* 84 */       .component(DataComponents.WEAPON, new Weapon(1));
/*    */   }
/*    */   
/*    */   private ItemAttributeModifiers createSwordAttributes(float attackDamageBaseline, float attackSpeedBaseline) {
/* 88 */     return ItemAttributeModifiers.builder()
/* 89 */       .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (attackDamageBaseline + this.attackDamageBonus), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 90 */       .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedBaseline, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 91 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ToolMaterial.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
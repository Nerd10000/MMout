/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.slot.SlotSource;
/*    */ import net.minecraft.world.item.slot.SlotSources;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SlotLoot extends LootPoolSingletonContainer {
/*    */   public static final MapCodec<SlotLoot> MAP_CODEC;
/*    */   
/*    */   private final SlotSource slotSource;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotSources.CODEC.fieldOf("slot_source").forGetter(())).and(singletonFields(i)).apply((Applicative)i, SlotLoot::new));
/*    */   }
/*    */   
/*    */   private SlotLoot(SlotSource slotSource, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 25 */     super(weight, quality, conditions, functions);
/* 26 */     this.slotSource = slotSource;
/*    */   }
/*    */   
/*    */   public MapCodec<SlotLoot> codec() {
/* 31 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 36 */     this.slotSource.provide(context).itemCopies()
/* 37 */       .filter(stack -> !stack.isEmpty())
/* 38 */       .forEach(output);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 43 */     super.validate(context);
/* 45 */     Validatable.validate(context, "slot_source", (Validatable)this.slotSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\SlotLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
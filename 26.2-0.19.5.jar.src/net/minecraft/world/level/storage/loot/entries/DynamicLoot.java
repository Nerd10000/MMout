/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DynamicLoot extends LootPoolSingletonContainer {
/*    */   public static final MapCodec<DynamicLoot> MAP_CODEC;
/*    */   
/*    */   private final Identifier name;
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("name").forGetter(())).and(singletonFields(i)).apply((Applicative)i, DynamicLoot::new));
/*    */   }
/*    */   
/*    */   private DynamicLoot(Identifier name, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 22 */     super(weight, quality, conditions, functions);
/* 23 */     this.name = name;
/*    */   }
/*    */   
/*    */   public MapCodec<DynamicLoot> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 33 */     context.addDynamicDrops(this.name, output);
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> dynamicEntry(Identifier name) {
/* 37 */     return simpleBuilder((weight, quality, conditions, functions) -> new DynamicLoot(name, weight, quality, conditions, functions));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\DynamicLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
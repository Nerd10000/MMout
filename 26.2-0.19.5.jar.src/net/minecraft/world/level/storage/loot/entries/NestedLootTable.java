/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class NestedLootTable extends LootPoolSingletonContainer {
/*    */   public static final MapCodec<NestedLootTable> MAP_CODEC;
/*    */   
/*    */   static {
/* 22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.either(LootTable.KEY_CODEC, LootTable.DIRECT_CODEC).fieldOf("value").forGetter(())).and(singletonFields(i)).apply((Applicative)i, NestedLootTable::new));
/*    */   }
/*    */   
/* 29 */   public static final ProblemReporter.PathElement INLINE_LOOT_TABLE_PATH_ELEMENT = new ProblemReporter.PathElement() {
/*    */       public String get() {
/* 32 */         return "->{inline}";
/*    */       }
/*    */     };
/*    */   
/*    */   private final Either<ResourceKey<LootTable>, LootTable> contents;
/*    */   
/*    */   private NestedLootTable(Either<ResourceKey<LootTable>, LootTable> contents, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 39 */     super(weight, quality, conditions, functions);
/* 40 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public MapCodec<NestedLootTable> codec() {
/* 45 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 50 */     ((LootTable)this.contents.map(name -> (LootTable)context.getResolver().get(name).map(Holder::value).orElse(LootTable.EMPTY), table -> table))
/*    */       
/* 53 */       .getRandomItemsRaw(context, output);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 58 */     super.validate(context);
/* 60 */     this.contents
/* 61 */       .ifLeft(id -> Validatable.validateReference(context, id))
/*    */       
/* 64 */       .ifRight(lootTable -> lootTable.validate(context.forChild(INLINE_LOOT_TABLE_PATH_ELEMENT)));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> lootTableReference(ResourceKey<LootTable> name) {
/* 70 */     return simpleBuilder((weight, quality, conditions, functions) -> new NestedLootTable(Either.left(name), weight, quality, conditions, functions));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> inlineLootTable(LootTable table) {
/* 74 */     return simpleBuilder((weight, quality, conditions, functions) -> new NestedLootTable(Either.right(table), weight, quality, conditions, functions));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\NestedLootTable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.Nameable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class CopyNameFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<CopyNameFunction> MAP_CODEC;
/*    */   
/*    */   private final LootContextArg<Object> source;
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)LootContextArg.ENTITY_OR_BLOCK.fieldOf("source").forGetter(())).apply((Applicative)i, CopyNameFunction::new));
/*    */   }
/*    */   
/*    */   private CopyNameFunction(List<LootItemCondition> predicates, LootContextArg<?> source) {
/* 24 */     super(predicates);
/* 25 */     this.source = LootContextArg.cast(source);
/*    */   }
/*    */   
/*    */   public MapCodec<CopyNameFunction> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 35 */     return Set.of(this.source.contextParam());
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 40 */     Object maybeNameable = this.source.get(context);
/* 42 */     if (maybeNameable instanceof Nameable) {
/* 42 */       Nameable nameable = (Nameable)maybeNameable;
/* 43 */       itemStack.set(DataComponents.CUSTOM_NAME, nameable.getCustomName());
/*    */     } 
/* 45 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> copyName(LootContextArg<?> target) {
/* 49 */     return simpleBuilder(conditions -> new CopyNameFunction(conditions, target));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyNameFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
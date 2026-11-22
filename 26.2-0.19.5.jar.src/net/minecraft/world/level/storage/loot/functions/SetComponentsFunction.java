/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetComponentsFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetComponentsFunction> MAP_CODEC;
/*    */   
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)DataComponentPatch.CODEC.fieldOf("components").forGetter(())).apply((Applicative)i, SetComponentsFunction::new));
/*    */   }
/*    */   
/*    */   private SetComponentsFunction(List<LootItemCondition> predicates, DataComponentPatch components) {
/* 21 */     super(predicates);
/* 22 */     this.components = components;
/*    */   }
/*    */   
/*    */   public MapCodec<SetComponentsFunction> codec() {
/* 27 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 32 */     itemStack.applyComponentsAndValidate(this.components);
/* 33 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static <T> LootItemConditionalFunction.Builder<?> setComponent(DataComponentType<T> type, T value) {
/* 37 */     return simpleBuilder(conditions -> new SetComponentsFunction(conditions, DataComponentPatch.builder().set(type, value).build()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetComponentsFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
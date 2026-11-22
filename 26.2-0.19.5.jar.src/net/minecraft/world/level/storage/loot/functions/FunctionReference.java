/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FunctionReference extends LootItemConditionalFunction {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static final MapCodec<FunctionReference> MAP_CODEC;
/*    */   
/*    */   private final ResourceKey<LootItemFunction> name;
/*    */   
/*    */   static {
/* 21 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)ResourceKey.codec(Registries.ITEM_MODIFIER).fieldOf("name").forGetter(())).apply((Applicative)i, FunctionReference::new));
/*    */   }
/*    */   
/*    */   private FunctionReference(List<LootItemCondition> predicates, ResourceKey<LootItemFunction> name) {
/* 28 */     super(predicates);
/* 29 */     this.name = name;
/*    */   }
/*    */   
/*    */   public MapCodec<FunctionReference> codec() {
/* 34 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 39 */     super.validate(context);
/* 41 */     Validatable.validateReference(context, this.name);
/*    */   }
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 46 */     LootItemFunction function = context.getResolver().get(this.name).map(Holder::value).orElse(null);
/* 47 */     if (function == null) {
/* 48 */       LOGGER.warn("Unknown function: {}", this.name.identifier());
/* 49 */       return itemStack;
/*    */     } 
/* 51 */     LootContext.VisitedEntry<?> breadcrumb = LootContext.createVisitedEntry(function);
/* 52 */     if (context.pushVisitedElement(breadcrumb))
/*    */       try {
/* 54 */         return function.apply(itemStack, context);
/*    */       } finally {
/* 56 */         context.popVisitedElement(breadcrumb);
/*    */       }  
/* 59 */     LOGGER.warn("Detected infinite loop in loot tables");
/* 60 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> functionReference(ResourceKey<LootItemFunction> name) {
/* 65 */     return simpleBuilder(conditions -> new FunctionReference(conditions, name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\FunctionReference.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
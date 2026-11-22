/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetCustomModelDataFunction extends LootItemConditionalFunction {
/* 23 */   private static final Codec<NumberProvider> COLOR_PROVIDER_CODEC = Codec.withAlternative(NumberProviders.CODEC, ExtraCodecs.RGB_COLOR_CODEC, net.minecraft.world.level.storage.loot.providers.number.ConstantValue::new);
/*    */   
/*    */   public static final MapCodec<SetCustomModelDataFunction> MAP_CODEC;
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<NumberProvider>> floats;
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<Boolean>> flags;
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<String>> strings;
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<NumberProvider>> colors;
/*    */   
/*    */   static {
/* 28 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ListOperation.StandAlone.<T>codec(NumberProviders.CODEC, 2147483647).optionalFieldOf("floats").forGetter(()), (App)ListOperation.StandAlone.<T>codec((Codec<T>)Codec.BOOL, 2147483647).optionalFieldOf("flags").forGetter(()), (App)ListOperation.StandAlone.<T>codec((Codec<T>)Codec.STRING, 2147483647).optionalFieldOf("strings").forGetter(()), (App)ListOperation.StandAlone.<T>codec((Codec)COLOR_PROVIDER_CODEC, 2147483647).optionalFieldOf("colors").forGetter(()))).apply((Applicative)i, SetCustomModelDataFunction::new));
/*    */   }
/*    */   
/*    */   public SetCustomModelDataFunction(List<LootItemCondition> predicates, Optional<ListOperation.StandAlone<NumberProvider>> floats, Optional<ListOperation.StandAlone<Boolean>> flags, Optional<ListOperation.StandAlone<String>> strings, Optional<ListOperation.StandAlone<NumberProvider>> colors) {
/* 41 */     super(predicates);
/* 42 */     this.floats = floats;
/* 43 */     this.flags = flags;
/* 44 */     this.strings = strings;
/* 45 */     this.colors = colors;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 50 */     super.validate(context);
/* 52 */     this.floats.ifPresent(f -> Validatable.validate(context, "floats", f.value()));
/* 53 */     this.colors.ifPresent(c -> Validatable.validate(context, "colors", c.value()));
/*    */   }
/*    */   
/*    */   public MapCodec<SetCustomModelDataFunction> codec() {
/* 58 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   private static <T> List<T> apply(Optional<ListOperation.StandAlone<T>> operation, List<T> current) {
/* 62 */     return operation.<List<T>>map(o -> o.apply(current)).orElse(current);
/*    */   }
/*    */   
/*    */   private static <T, E> List<E> apply(Optional<ListOperation.StandAlone<T>> operation, List<E> current, Function<T, E> mapper) {
/* 66 */     return operation.<List<E>>map(o -> {
/*    */           List<E> transformedReplacement = o.value().stream().map(mapper).toList();
/*    */           return o.operation().apply(current, transformedReplacement);
/* 69 */         }).orElse(current);
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 74 */     CustomModelData component = (CustomModelData)itemStack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY);
/* 76 */     itemStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(
/* 77 */           apply(this.floats, component.floats(), provider -> Float.valueOf(provider.getFloat(context))), 
/* 78 */           apply(this.flags, component.flags()), 
/* 79 */           apply(this.strings, component.strings()), 
/* 80 */           apply(this.colors, component.colors(), provider -> Integer.valueOf(provider.getInt(context)))));
/* 82 */     return itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetCustomModelDataFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
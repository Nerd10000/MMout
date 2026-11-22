/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public class SequenceFunction implements LootItemFunction {
/*    */   public static final MapCodec<SequenceFunction> MAP_CODEC;
/*    */   
/*    */   public static final Codec<SequenceFunction> INLINE_CODEC;
/*    */   
/*    */   private final List<LootItemFunction> functions;
/*    */   
/*    */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootItemFunctions.TYPED_CODEC.listOf().fieldOf("functions").forGetter(())).apply((Applicative)i, SequenceFunction::new));
/* 19 */     INLINE_CODEC = LootItemFunctions.TYPED_CODEC.listOf().xmap(SequenceFunction::new, f -> f.functions);
/*    */   }
/*    */   
/*    */   private SequenceFunction(List<LootItemFunction> functions) {
/* 25 */     this.functions = functions;
/* 26 */     this.compositeFunction = LootItemFunctions.compose((List)functions);
/*    */   }
/*    */   
/*    */   public static SequenceFunction of(List<LootItemFunction> functions) {
/* 30 */     return new SequenceFunction(List.copyOf(functions));
/*    */   }
/*    */   
/*    */   public ItemStack apply(ItemStack stack, LootContext context) {
/* 35 */     return this.compositeFunction.apply(stack, context);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 40 */     super.validate(output);
/* 42 */     Validatable.validate(output, "functions", this.functions);
/*    */   }
/*    */   
/*    */   public MapCodec<SequenceFunction> codec() {
/* 47 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SequenceFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
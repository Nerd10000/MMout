/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.DyedItemColor;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetRandomDyesFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetRandomDyesFunction> MAP_CODEC;
/*    */   
/*    */   private final NumberProvider numberOfDyes;
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)NumberProviders.CODEC.fieldOf("number_of_dyes").forGetter(())).apply((Applicative)i, SetRandomDyesFunction::new));
/*    */   }
/*    */   
/*    */   private SetRandomDyesFunction(List<LootItemCondition> predicates, NumberProvider numberOfDyes) {
/* 26 */     super(predicates);
/* 27 */     this.numberOfDyes = numberOfDyes;
/*    */   }
/*    */   
/*    */   public MapCodec<SetRandomDyesFunction> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 37 */     RandomSource random = context.getRandom();
/* 38 */     int rolls = this.numberOfDyes.getInt(context);
/* 39 */     if (rolls <= 0)
/* 40 */       return itemStack; 
/* 42 */     List<DyeColor> dyes = new ArrayList<>(rolls);
/* 43 */     for (int i = 0; i < rolls; i++)
/* 44 */       dyes.add((DyeColor)Util.getRandom(DyeColor.VALUES, random)); 
/* 46 */     return DyedItemColor.applyDyes(itemStack, dyes);
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> withCount(NumberProvider numberOfDyes) {
/* 50 */     return simpleBuilder(conditions -> new SetRandomDyesFunction(conditions, numberOfDyes));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetRandomDyesFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
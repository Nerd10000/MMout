/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetPotionFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetPotionFunction> MAP_CODEC;
/*    */   
/*    */   private final Holder<Potion> potion;
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)Potion.CODEC.fieldOf("id").forGetter(())).apply((Applicative)i, SetPotionFunction::new));
/*    */   }
/*    */   
/*    */   private SetPotionFunction(List<LootItemCondition> predicates, Holder<Potion> potion) {
/* 23 */     super(predicates);
/* 24 */     this.potion = potion;
/*    */   }
/*    */   
/*    */   public MapCodec<SetPotionFunction> codec() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     itemStack.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, this.potion, PotionContents::withPotion);
/* 35 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setPotion(Holder<Potion> value) {
/* 39 */     return simpleBuilder(conditions -> new SetPotionFunction(conditions, value));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetPotionFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
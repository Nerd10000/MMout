/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetRandomPotionFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetRandomPotionFunction> MAP_CODEC;
/*    */   
/*    */   private final Optional<HolderSet<Potion>> options;
/*    */   
/*    */   static {
/* 20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)RegistryCodecs.homogeneousList(Registries.POTION).optionalFieldOf("options").forGetter(())).apply((Applicative)i, SetRandomPotionFunction::new));
/*    */   }
/*    */   
/*    */   private SetRandomPotionFunction(List<LootItemCondition> predicates, Optional<HolderSet<Potion>> options) {
/* 27 */     super(predicates);
/* 28 */     this.options = options;
/*    */   }
/*    */   
/*    */   public MapCodec<SetRandomPotionFunction> codec() {
/* 33 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*    */     Optional<? extends Holder<Potion>> potion;
/* 39 */     if (this.options.isPresent()) {
/* 40 */       potion = ((HolderSet)this.options.get()).getRandomElement(context.getRandom());
/*    */     } else {
/* 42 */       potion = context.getLevel().registryAccess().lookupOrThrow(Registries.POTION).getRandom(context.getRandom());
/*    */     } 
/* 44 */     if (potion.isPresent())
/* 45 */       itemStack.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, potion.get(), PotionContents::withPotion); 
/* 47 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> fromTagKey(HolderSet<Potion> tagKey) {
/* 51 */     return simpleBuilder(conditions -> new SetRandomPotionFunction(conditions, Optional.of(tagKey)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetRandomPotionFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
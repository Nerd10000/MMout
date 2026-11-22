/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetOminousBottleAmplifierFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetOminousBottleAmplifierFunction> MAP_CODEC;
/*    */   
/*    */   private final NumberProvider amplifier;
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)NumberProviders.CODEC.fieldOf("amplifier").forGetter(())).apply((Applicative)i, SetOminousBottleAmplifierFunction::new));
/*    */   }
/*    */   
/*    */   private SetOminousBottleAmplifierFunction(List<LootItemCondition> predicates, NumberProvider amplifier) {
/* 26 */     super(predicates);
/* 27 */     this.amplifier = amplifier;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 32 */     super.validate(context);
/* 34 */     Validatable.validate(context, "amplifier", (Validatable)this.amplifier);
/*    */   }
/*    */   
/*    */   public MapCodec<SetOminousBottleAmplifierFunction> codec() {
/* 39 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 44 */     int amplifierValue = Mth.clamp(this.amplifier.getInt(context), 0, 4);
/* 45 */     itemStack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(amplifierValue));
/* 46 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setAmplifier(NumberProvider amplifier) {
/* 50 */     return simpleBuilder(conditions -> new SetOminousBottleAmplifierFunction(conditions, amplifier));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetOminousBottleAmplifierFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
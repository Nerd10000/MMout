/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.InstrumentComponent;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetInstrumentFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetInstrumentFunction> MAP_CODEC;
/*    */   
/*    */   private final HolderSet<Instrument> options;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)RegistryCodecs.homogeneousList(Registries.INSTRUMENT).fieldOf("options").forGetter(())).apply((Applicative)i, SetInstrumentFunction::new));
/*    */   }
/*    */   
/*    */   private SetInstrumentFunction(List<LootItemCondition> predicates, HolderSet<Instrument> options) {
/* 25 */     super(predicates);
/* 26 */     this.options = options;
/*    */   }
/*    */   
/*    */   public MapCodec<SetInstrumentFunction> codec() {
/* 31 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 36 */     this.options.getRandomElement(context.getRandom())
/* 37 */       .ifPresent(instrumentHolder -> itemStack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrumentHolder)));
/* 38 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setInstrumentOptions(HolderSet<Instrument> options) {
/* 42 */     return simpleBuilder(conditions -> new SetInstrumentFunction(conditions, options));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetInstrumentFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
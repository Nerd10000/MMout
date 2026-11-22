/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetCustomDataFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetCustomDataFunction> MAP_CODEC;
/*    */   
/*    */   private final CompoundTag tag;
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)TagParser.LENIENT_CODEC.fieldOf("tag").forGetter(())).apply((Applicative)i, SetCustomDataFunction::new));
/*    */   }
/*    */   
/*    */   private SetCustomDataFunction(List<LootItemCondition> predicates, CompoundTag tag) {
/* 23 */     super(predicates);
/* 24 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   public MapCodec<SetCustomDataFunction> codec() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.merge(this.tag));
/* 35 */     return itemStack;
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public static LootItemConditionalFunction.Builder<?> setCustomData(CompoundTag value) {
/* 41 */     return simpleBuilder(conditions -> new SetCustomDataFunction(conditions, value));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetCustomDataFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
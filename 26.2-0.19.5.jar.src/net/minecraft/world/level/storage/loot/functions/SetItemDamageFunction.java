/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SetItemDamageFunction extends LootItemConditionalFunction {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static final MapCodec<SetItemDamageFunction> MAP_CODEC;
/*    */   
/*    */   private final NumberProvider damage;
/*    */   
/*    */   private final boolean add;
/*    */   
/*    */   static {
/* 22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)NumberProviders.CODEC.fieldOf("damage").forGetter(()), (App)Codec.BOOL.optionalFieldOf("add", Boolean.valueOf(false)).forGetter(()))).apply((Applicative)i, SetItemDamageFunction::new));
/*    */   }
/*    */   
/*    */   private SetItemDamageFunction(List<LootItemCondition> predicates, NumberProvider damage, boolean add) {
/* 31 */     super(predicates);
/* 32 */     this.damage = damage;
/* 33 */     this.add = add;
/*    */   }
/*    */   
/*    */   public MapCodec<SetItemDamageFunction> codec() {
/* 38 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 43 */     super.validate(context);
/* 45 */     Validatable.validate(context, "damage", (Validatable)this.damage);
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 50 */     if (itemStack.isDamageableItem()) {
/* 51 */       int maxDamage = itemStack.getMaxDamage();
/* 52 */       float base = this.add ? (1.0F - itemStack.getDamageValue() / maxDamage) : 0.0F;
/* 53 */       float pct = 1.0F - Mth.clamp(this.damage.getFloat(context) + base, 0.0F, 1.0F);
/* 54 */       itemStack.setDamageValue(Mth.floor(pct * maxDamage));
/*    */     } else {
/* 56 */       LOGGER.warn("Couldn't set damage of loot item {}", itemStack);
/*    */     } 
/* 58 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider value) {
/* 62 */     return simpleBuilder(conditions -> new SetItemDamageFunction(conditions, value, false));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider value, boolean add) {
/* 66 */     return simpleBuilder(conditions -> new SetItemDamageFunction(conditions, value, add));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetItemDamageFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
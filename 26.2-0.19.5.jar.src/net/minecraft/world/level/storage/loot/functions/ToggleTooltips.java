/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ToggleTooltips extends LootItemConditionalFunction {
/*    */   public static final MapCodec<ToggleTooltips> MAP_CODEC;
/*    */   
/*    */   private final Map<DataComponentType<?>, Boolean> values;
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)Codec.unboundedMap(DataComponentType.CODEC, (Codec)Codec.BOOL).fieldOf("toggles").forGetter(())).apply((Applicative)i, ToggleTooltips::new));
/*    */   }
/*    */   
/*    */   private ToggleTooltips(List<LootItemCondition> predicates, Map<DataComponentType<?>, Boolean> values) {
/* 24 */     super(predicates);
/* 25 */     this.values = values;
/*    */   }
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 30 */     itemStack.update(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT, display -> {
/*    */           for (Map.Entry<DataComponentType<?>, Boolean> entry : this.values.entrySet()) {
/*    */             boolean shown = ((Boolean)entry.getValue()).booleanValue();
/*    */             display = display.withHidden(entry.getKey(), !shown);
/*    */           } 
/*    */           return display;
/*    */         });
/* 37 */     return itemStack;
/*    */   }
/*    */   
/*    */   public MapCodec<ToggleTooltips> codec() {
/* 42 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ToggleTooltips.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
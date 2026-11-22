/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class SlotDisplays {
/*    */   public static SlotDisplay.Type<?> bootstrap(Registry<SlotDisplay.Type<?>> registry) {
/*  7 */     Registry.register(registry, "empty", SlotDisplay.Empty.TYPE);
/*  8 */     Registry.register(registry, "any_fuel", SlotDisplay.AnyFuel.TYPE);
/*  9 */     Registry.register(registry, "with_any_potion", SlotDisplay.WithAnyPotion.TYPE);
/* 10 */     Registry.register(registry, "only_with_component", SlotDisplay.OnlyWithComponent.TYPE);
/* 11 */     Registry.register(registry, "item", SlotDisplay.ItemSlotDisplay.TYPE);
/* 12 */     Registry.register(registry, "item_stack", SlotDisplay.ItemStackSlotDisplay.TYPE);
/* 13 */     Registry.register(registry, "tag", SlotDisplay.TagSlotDisplay.TYPE);
/* 14 */     Registry.register(registry, "dyed", SlotDisplay.DyedSlotDemo.TYPE);
/* 15 */     Registry.register(registry, "smithing_trim", SlotDisplay.SmithingTrimDemoSlotDisplay.TYPE);
/* 16 */     Registry.register(registry, "with_remainder", SlotDisplay.WithRemainder.TYPE);
/* 17 */     return (SlotDisplay.Type)Registry.register(registry, "composite", SlotDisplay.Composite.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SlotDisplays.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
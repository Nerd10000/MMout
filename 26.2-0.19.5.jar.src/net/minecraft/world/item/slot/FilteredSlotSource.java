/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class FilteredSlotSource extends TransformedSlotSource {
/*    */   public static final MapCodec<FilteredSlotSource> MAP_CODEC;
/*    */   
/*    */   private final ItemPredicate filter;
/*    */   
/*    */   static {
/*  8 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)ItemPredicate.CODEC.fieldOf("item_filter").forGetter(())).apply((Applicative)i, FilteredSlotSource::new));
/*    */   }
/*    */   
/*    */   private FilteredSlotSource(SlotSource slotSource, ItemPredicate filter) {
/* 15 */     super(slotSource);
/* 16 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public MapCodec<FilteredSlotSource> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   protected SlotCollection transform(SlotCollection slots) {
/* 26 */     return slots.filter((Predicate<? super ItemStack>)this.filter);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\FilteredSlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
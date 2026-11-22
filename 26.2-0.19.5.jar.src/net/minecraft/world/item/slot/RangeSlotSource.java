/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.SlotProvider;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ import net.minecraft.world.inventory.SlotRanges;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ 
/*    */ public class RangeSlotSource implements SlotSource {
/*    */   public static final MapCodec<RangeSlotSource> MAP_CODEC;
/*    */   
/*    */   private final LootContextArg<Object> source;
/*    */   
/*    */   private final SlotRange slotRange;
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootContextArg.ENTITY_OR_BLOCK.fieldOf("source").forGetter(()), (App)SlotRanges.CODEC.fieldOf("slots").forGetter(())).apply((Applicative)i, RangeSlotSource::new));
/*    */   }
/*    */   
/*    */   private RangeSlotSource(LootContextArg<Object> source, SlotRange slotRange) {
/* 24 */     this.source = source;
/* 25 */     this.slotRange = slotRange;
/*    */   }
/*    */   
/*    */   public MapCodec<RangeSlotSource> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 35 */     return Set.of(this.source.contextParam());
/*    */   }
/*    */   
/*    */   public final SlotCollection provide(LootContext context) {
/* 40 */     Object maybeProvider = this.source.get(context);
/* 42 */     if (maybeProvider instanceof SlotProvider) {
/* 42 */       SlotProvider slotProvider = (SlotProvider)maybeProvider;
/* 43 */       return slotProvider.getSlotsFromRange(this.slotRange.slots());
/*    */     } 
/* 45 */     return SlotCollection.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\RangeSlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
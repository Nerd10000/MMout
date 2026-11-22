/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class TransformedSlotSource implements SlotSource {
/*    */   protected final SlotSource slotSource;
/*    */   
/*    */   protected TransformedSlotSource(SlotSource slotSource) {
/* 14 */     this.slotSource = slotSource;
/*    */   }
/*    */   
/*    */   protected static <T extends TransformedSlotSource> Products.P1<RecordCodecBuilder.Mu<T>, SlotSource> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 21 */     return i.group((App)SlotSources.CODEC
/* 22 */         .fieldOf("slot_source").forGetter(t -> t.slotSource));
/*    */   }
/*    */   
/*    */   public final SlotCollection provide(LootContext context) {
/* 30 */     return transform(this.slotSource.provide(context));
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 35 */     super.validate(context);
/* 37 */     Validatable.validate(context, "slot_source", (Validatable)this.slotSource);
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends TransformedSlotSource> codec();
/*    */   
/*    */   protected abstract SlotCollection transform(SlotCollection paramSlotCollection);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\TransformedSlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
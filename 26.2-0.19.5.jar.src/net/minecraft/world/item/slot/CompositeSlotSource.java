/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class CompositeSlotSource implements SlotSource {
/*    */   protected final List<SlotSource> terms;
/*    */   
/*    */   private final Function<LootContext, SlotCollection> compositeSlotSource;
/*    */   
/*    */   protected CompositeSlotSource(List<SlotSource> terms) {
/* 18 */     this.terms = terms;
/* 19 */     this.compositeSlotSource = SlotSources.group(terms);
/*    */   }
/*    */   
/*    */   protected static <T extends CompositeSlotSource> MapCodec<T> createCodec(Function<List<SlotSource>, T> factory) {
/* 23 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)SlotSources.CODEC.listOf().fieldOf("terms").forGetter(())).apply((Applicative)i, factory));
/*    */   }
/*    */   
/*    */   protected static <T extends CompositeSlotSource> Codec<T> createInlineCodec(Function<List<SlotSource>, T> factory) {
/* 29 */     return SlotSources.CODEC.listOf().xmap(factory, t -> t.terms);
/*    */   }
/*    */   
/*    */   public SlotCollection provide(LootContext context) {
/* 37 */     return this.compositeSlotSource.apply(context);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 42 */     super.validate(context);
/* 44 */     Validatable.validate(context, "terms", this.terms);
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends CompositeSlotSource> codec();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\CompositeSlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
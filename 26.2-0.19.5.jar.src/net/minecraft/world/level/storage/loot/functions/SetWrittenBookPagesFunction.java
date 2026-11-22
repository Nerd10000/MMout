/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetWrittenBookPagesFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetWrittenBookPagesFunction> MAP_CODEC;
/*    */   
/*    */   private final List<Filterable<Component>> pages;
/*    */   
/*    */   private final ListOperation pageOperation;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)WrittenBookContent.PAGES_CODEC.fieldOf("pages").forGetter(()), (App)ListOperation.UNLIMITED_CODEC.forGetter(()))).apply((Applicative)i, SetWrittenBookPagesFunction::new));
/*    */   }
/*    */   
/*    */   protected SetWrittenBookPagesFunction(List<LootItemCondition> predicates, List<Filterable<Component>> pages, ListOperation pageOperation) {
/* 27 */     super(predicates);
/* 28 */     this.pages = pages;
/* 29 */     this.pageOperation = pageOperation;
/*    */   }
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     itemStack.update(DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY, this::apply);
/* 35 */     return itemStack;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public WrittenBookContent apply(WrittenBookContent original) {
/* 40 */     List<Filterable<Component>> newPages = this.pageOperation.apply(original.pages(), this.pages);
/* 41 */     return original.withReplacedPages(newPages);
/*    */   }
/*    */   
/*    */   public MapCodec<SetWrittenBookPagesFunction> codec() {
/* 46 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetWrittenBookPagesFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
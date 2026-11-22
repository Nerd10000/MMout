/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WritableBookContent;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetWritableBookPagesFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetWritableBookPagesFunction> MAP_CODEC;
/*    */   
/*    */   private final List<Filterable<String>> pages;
/*    */   
/*    */   private final ListOperation pageOperation;
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)WritableBookContent.PAGES_CODEC.fieldOf("pages").forGetter(()), (App)ListOperation.codec(100).forGetter(()))).apply((Applicative)i, SetWritableBookPagesFunction::new));
/*    */   }
/*    */   
/*    */   protected SetWritableBookPagesFunction(List<LootItemCondition> predicates, List<Filterable<String>> pages, ListOperation pageOperation) {
/* 25 */     super(predicates);
/* 26 */     this.pages = pages;
/* 27 */     this.pageOperation = pageOperation;
/*    */   }
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 32 */     itemStack.update(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY, this::apply);
/* 33 */     return itemStack;
/*    */   }
/*    */   
/*    */   public WritableBookContent apply(WritableBookContent original) {
/* 37 */     List<Filterable<String>> newPages = this.pageOperation.apply(original.pages(), this.pages, 100);
/* 38 */     return original.withReplacedPages(newPages);
/*    */   }
/*    */   
/*    */   public MapCodec<SetWritableBookPagesFunction> codec() {
/* 43 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetWritableBookPagesFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
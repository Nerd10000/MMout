/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class CompositeEntryBase extends LootPoolEntryContainer {
/* 15 */   public static final ProblemReporter.Problem NO_CHILDREN_PROBLEM = new ProblemReporter.Problem() {
/*    */       public String description() {
/* 18 */         return "Empty children list";
/*    */       }
/*    */     };
/*    */   
/*    */   protected final List<LootPoolEntryContainer> children;
/*    */   
/*    */   private final ComposableEntryContainer composedChildren;
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface CompositeEntryConstructor<T extends CompositeEntryBase> {
/*    */     T create(List<LootPoolEntryContainer> param1List, List<LootItemCondition> param1List1);
/*    */   }
/*    */   
/*    */   protected CompositeEntryBase(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) {
/* 26 */     super(conditions);
/* 27 */     this.children = children;
/* 28 */     this.composedChildren = compose((List)children);
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends CompositeEntryBase> codec();
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 36 */     super.validate(context);
/* 38 */     if (this.children.isEmpty())
/* 39 */       context.reportProblem(NO_CHILDREN_PROBLEM); 
/* 41 */     Validatable.validate(context, "children", this.children);
/*    */   }
/*    */   
/*    */   protected abstract ComposableEntryContainer compose(List<? extends ComposableEntryContainer> paramList);
/*    */   
/*    */   public final boolean expand(LootContext context, Consumer<LootPoolEntry> output) {
/* 48 */     if (!canRun(context))
/* 49 */       return false; 
/* 52 */     return this.composedChildren.expand(context, output);
/*    */   }
/*    */   
/*    */   public static <T extends CompositeEntryBase> MapCodec<T> createCodec(CompositeEntryConstructor<T> constructor) {
/* 61 */     return RecordCodecBuilder.mapCodec(i -> {
/*    */           Objects.requireNonNull(constructor);
/*    */           return i.group((App)LootPoolEntries.CODEC.listOf().optionalFieldOf("children", List.of()).forGetter(())).and(commonFields(i).t1()).apply((Applicative)i, constructor::create);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\CompositeEntryBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
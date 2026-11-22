/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class AlternativesEntry extends CompositeEntryBase {
/* 14 */   public static final MapCodec<AlternativesEntry> MAP_CODEC = createCodec(AlternativesEntry::new);
/*    */   
/* 16 */   public static final ProblemReporter.Problem UNREACHABLE_PROBLEM = new ProblemReporter.Problem() {
/*    */       public String description() {
/* 19 */         return "Unreachable entry!";
/*    */       }
/*    */     };
/*    */   
/*    */   public AlternativesEntry(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) {
/* 24 */     super(children, conditions);
/*    */   }
/*    */   
/*    */   public MapCodec<AlternativesEntry> codec() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries) {
/* 34 */     switch (entries.size()) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */     } 
/* 34 */     return (context, output) -> {
/*    */         for (ComposableEntryContainer entry : entries) {
/*    */           if (entry.expand(context, output))
/*    */             return true; 
/*    */         } 
/*    */         return false;
/*    */       };
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 51 */     super.validate(context);
/* 53 */     for (int i = 0; i < this.children.size() - 1; i++) {
/* 55 */       if (((LootPoolEntryContainer)this.children.get(i)).conditions.isEmpty())
/* 56 */         context.reportProblem(UNREACHABLE_PROBLEM); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Builder extends LootPoolEntryContainer.Builder<Builder> {
/* 62 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... entries) {
/* 65 */       for (LootPoolEntryContainer.Builder<?> entry : entries)
/* 66 */         this.entries.add(entry.build()); 
/*    */     }
/*    */     
/*    */     protected Builder getThis() {
/* 72 */       return this;
/*    */     }
/*    */     
/*    */     public Builder otherwise(LootPoolEntryContainer.Builder<?> other) {
/* 77 */       this.entries.add(other.build());
/* 78 */       return this;
/*    */     }
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 83 */       return new AlternativesEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder alternatives(LootPoolEntryContainer.Builder<?>... entries) {
/* 88 */     return new Builder(entries);
/*    */   }
/*    */   
/*    */   public static <E> Builder alternatives(Collection<E> items, Function<E, LootPoolEntryContainer.Builder<?>> provider) {
/* 92 */     Objects.requireNonNull(provider);
/* 92 */     return new Builder((LootPoolEntryContainer.Builder<?>[])items.stream().map(provider::apply).toArray(x$0 -> new LootPoolEntryContainer.Builder[x$0]));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\AlternativesEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EntryGroup extends CompositeEntryBase {
/* 10 */   public static final MapCodec<EntryGroup> MAP_CODEC = createCodec(EntryGroup::new);
/*    */   
/*    */   public EntryGroup(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) {
/* 13 */     super(children, conditions);
/*    */   }
/*    */   
/*    */   public MapCodec<EntryGroup> codec() {
/* 18 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries) {
/*    */     ComposableEntryContainer first;
/*    */     ComposableEntryContainer second;
/* 23 */     switch (entries.size()) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/* 27 */         first = entries.get(0);
/* 28 */         second = entries.get(1);
/*    */     } 
/*    */     return (context, output) -> {
/*    */         for (ComposableEntryContainer entry : entries)
/*    */           entry.expand(context, output); 
/*    */         return true;
/*    */       };
/*    */   }
/*    */   
/*    */   public static class Builder extends LootPoolEntryContainer.Builder<Builder> {
/* 45 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... entries) {
/* 48 */       for (LootPoolEntryContainer.Builder<?> entry : entries)
/* 49 */         this.entries.add(entry.build()); 
/*    */     }
/*    */     
/*    */     protected Builder getThis() {
/* 55 */       return this;
/*    */     }
/*    */     
/*    */     public Builder append(LootPoolEntryContainer.Builder<?> other) {
/* 60 */       this.entries.add(other.build());
/* 61 */       return this;
/*    */     }
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 66 */       return new EntryGroup((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder list(LootPoolEntryContainer.Builder<?>... entries) {
/* 71 */     return new Builder(entries);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\EntryGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
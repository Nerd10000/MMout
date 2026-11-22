/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder extends LootPoolEntryContainer.Builder<SequentialEntry.Builder> {
/* 39 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(LootPoolEntryContainer.Builder<?>... entries) {
/* 42 */     for (LootPoolEntryContainer.Builder<?> entry : entries)
/* 43 */       this.entries.add(entry.build()); 
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Builder then(LootPoolEntryContainer.Builder<?> other) {
/* 54 */     this.entries.add(other.build());
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public LootPoolEntryContainer build() {
/* 60 */     return new SequentialEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\SequentialEntry$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder extends LootPoolEntryContainer.Builder<AlternativesEntry.Builder> {
/* 62 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(LootPoolEntryContainer.Builder<?>... entries) {
/* 65 */     for (LootPoolEntryContainer.Builder<?> entry : entries)
/* 66 */       this.entries.add(entry.build()); 
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public Builder otherwise(LootPoolEntryContainer.Builder<?> other) {
/* 77 */     this.entries.add(other.build());
/* 78 */     return this;
/*    */   }
/*    */   
/*    */   public LootPoolEntryContainer build() {
/* 83 */     return new AlternativesEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\AlternativesEntry$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
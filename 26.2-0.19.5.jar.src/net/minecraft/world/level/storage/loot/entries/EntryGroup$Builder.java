/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder extends LootPoolEntryContainer.Builder<EntryGroup.Builder> {
/* 45 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(LootPoolEntryContainer.Builder<?>... entries) {
/* 48 */     for (LootPoolEntryContainer.Builder<?> entry : entries)
/* 49 */       this.entries.add(entry.build()); 
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public Builder append(LootPoolEntryContainer.Builder<?> other) {
/* 60 */     this.entries.add(other.build());
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public LootPoolEntryContainer build() {
/* 66 */     return new EntryGroup((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\EntryGroup$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ 
/*    */ public class Builder extends LootItemConditionalFunction.Builder<SetContainerContents.Builder> {
/* 61 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   public Builder(ContainerComponentManipulator<?> component) {
/* 65 */     this.component = component;
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withEntry(LootPoolEntryContainer.Builder<?> entry) {
/* 74 */     this.entries.add(entry.build());
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 80 */     return new SetContainerContents(getConditions(), this.component, (List<LootPoolEntryContainer>)this.entries.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerContents$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
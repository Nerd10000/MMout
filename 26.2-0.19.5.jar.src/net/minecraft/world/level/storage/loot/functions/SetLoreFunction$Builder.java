/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class Builder extends LootItemConditionalFunction.Builder<SetLoreFunction.Builder> {
/* 66 */   private Optional<LootContext.EntityTarget> resolutionContext = Optional.empty();
/*    */   
/* 67 */   private final ImmutableList.Builder<Component> lore = ImmutableList.builder();
/*    */   
/* 68 */   private ListOperation mode = ListOperation.Append.INSTANCE;
/*    */   
/*    */   public Builder setMode(ListOperation mode) {
/* 71 */     this.mode = mode;
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setResolutionContext(LootContext.EntityTarget resolutionContext) {
/* 76 */     this.resolutionContext = Optional.of(resolutionContext);
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addLine(Component line) {
/* 81 */     this.lore.add(line);
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 92 */     return new SetLoreFunction(getConditions(), (List<Component>)this.lore.build(), this.mode, this.resolutionContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetLoreFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
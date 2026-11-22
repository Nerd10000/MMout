/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public class ValidationContextSource {
/*    */   private final ProblemReporter reporter;
/*    */   
/*    */   private final HolderGetter.Provider lootData;
/*    */   
/*    */   private ValidationContext entityContext;
/*    */   
/*    */   public ValidationContextSource(ProblemReporter reporter, HolderGetter.Provider lootData) {
/* 16 */     this.reporter = reporter;
/* 17 */     this.lootData = lootData;
/*    */   }
/*    */   
/*    */   public ValidationContext context(ContextKeySet params) {
/* 21 */     return new ValidationContext(this.reporter, params, this.lootData);
/*    */   }
/*    */   
/*    */   public ValidationContext entityContext() {
/* 25 */     if (this.entityContext == null)
/* 26 */       this.entityContext = context(LootContextParamSets.ADVANCEMENT_ENTITY); 
/* 28 */     return this.entityContext;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\ValidationContextSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
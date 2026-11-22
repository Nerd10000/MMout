/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface AdvancementSubProvider {
/*    */   void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer);
/*    */   
/*    */   static AdvancementHolder createPlaceholder(String id) {
/* 14 */     return Advancement.Builder.advancement().build(Identifier.parse(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\advancements\AdvancementSubProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
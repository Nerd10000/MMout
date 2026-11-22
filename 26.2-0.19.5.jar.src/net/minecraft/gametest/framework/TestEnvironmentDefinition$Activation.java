/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public class Activation<T> {
/*    */   private final T value;
/*    */   
/*    */   private final TestEnvironmentDefinition<T> definition;
/*    */   
/*    */   private final ServerLevel level;
/*    */   
/*    */   private Activation(T value, TestEnvironmentDefinition<T> definition, ServerLevel level) {
/* 64 */     this.value = value;
/* 65 */     this.definition = definition;
/* 66 */     this.level = level;
/*    */   }
/*    */   
/*    */   public void teardown() {
/* 70 */     this.definition.teardown(this.level, this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Activation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
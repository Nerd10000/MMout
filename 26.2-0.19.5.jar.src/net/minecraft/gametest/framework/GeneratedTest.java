/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class GeneratedTest extends Record {
/*    */   private final Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition<?>>>> tests;
/*    */   
/*    */   private final ResourceKey<Consumer<GameTestHelper>> functionKey;
/*    */   
/*    */   private final Consumer<GameTestHelper> function;
/*    */   
/*    */   public GeneratedTest(Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition<?>>>> tests, ResourceKey<Consumer<GameTestHelper>> functionKey, Consumer<GameTestHelper> function) {
/* 10 */     this.tests = tests;
/* 10 */     this.functionKey = functionKey;
/* 10 */     this.function = function;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/GeneratedTest;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GeneratedTest;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/GeneratedTest;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GeneratedTest;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/GeneratedTest;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/GeneratedTest;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition<?>>>> tests() {
/* 10 */     return this.tests;
/*    */   }
/*    */   
/*    */   public ResourceKey<Consumer<GameTestHelper>> functionKey() {
/* 10 */     return this.functionKey;
/*    */   }
/*    */   
/*    */   public Consumer<GameTestHelper> function() {
/* 10 */     return this.function;
/*    */   }
/*    */   
/*    */   public GeneratedTest(Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition<?>>>> tests, Identifier functionId, Consumer<GameTestHelper> function) {
/* 12 */     this(tests, ResourceKey.create(Registries.TEST_FUNCTION, functionId), function);
/*    */   }
/*    */   
/*    */   public GeneratedTest(Identifier id, TestData<ResourceKey<TestEnvironmentDefinition<?>>> testData, Consumer<GameTestHelper> function) {
/* 16 */     this(Map.of(id, testData), id, function);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GeneratedTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
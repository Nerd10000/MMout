/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface GameTestEnvironments {
/*    */   public static final String DEFAULT = "default";
/*    */   
/* 14 */   public static final ResourceKey<TestEnvironmentDefinition<?>> DEFAULT_KEY = create("default");
/*    */   
/*    */   private static ResourceKey<TestEnvironmentDefinition<?>> create(String name) {
/* 17 */     return ResourceKey.create(Registries.TEST_ENVIRONMENT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<TestEnvironmentDefinition<?>> context) {
/* 21 */     context.register(DEFAULT_KEY, new TestEnvironmentDefinition.AllOf(List.of()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestEnvironments.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
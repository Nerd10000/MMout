/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public abstract class TestFunctionLoader {
/* 12 */   private static final List<TestFunctionLoader> loaders = new ArrayList<>();
/*    */   
/*    */   public static void registerLoader(TestFunctionLoader loader) {
/* 15 */     loaders.add(loader);
/*    */   }
/*    */   
/*    */   public static void runLoaders(Registry<Consumer<GameTestHelper>> registry) {
/* 19 */     for (Iterator<TestFunctionLoader> iterator = loaders.iterator(); iterator.hasNext(); ) {
/* 19 */       TestFunctionLoader loader = iterator.next();
/* 20 */       loader.load((key, function) -> Registry.register(registry, key, function));
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void load(BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> paramBiConsumer);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestFunctionLoader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
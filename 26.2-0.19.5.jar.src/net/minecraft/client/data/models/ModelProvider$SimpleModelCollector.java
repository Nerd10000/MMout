/*    */ package net.minecraft.client.data.models;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.data.models.model.ModelInstance;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class SimpleModelCollector implements BiConsumer<Identifier, ModelInstance> {
/* 43 */   private final Map<Identifier, ModelInstance> models = new HashMap<>();
/*    */   
/*    */   public void accept(Identifier id, ModelInstance contents) {
/* 47 */     Supplier<JsonElement> prev = (Supplier<JsonElement>)this.models.put(id, contents);
/* 48 */     if (prev != null)
/* 49 */       throw new IllegalStateException("Duplicate model definition for " + String.valueOf(id)); 
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/* 54 */     Objects.requireNonNull(pathProvider);
/* 54 */     return DataProvider.saveAll(cache, Supplier::get, pathProvider::json, this.models);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\ModelProvider$SimpleModelCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentInitializers;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ 
/*    */ public class RegistryComponentsReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public RegistryComponentsReport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 25 */     this.output = output;
/* 26 */     this.registries = registries;
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 31 */     return this.registries.thenCompose(registries -> {
/*    */           RegistryOps<JsonElement> registryOps = registries.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */           List<CompletableFuture<?>> writes = new ArrayList<>();
/*    */           BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build(registries).forEach(());
/*    */           return CompletableFuture.allOf((CompletableFuture<?>[])writes.toArray(()));
/*    */         });
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 58 */     return "Default Components";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\info\RegistryComponentsReport.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
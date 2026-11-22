/*     */ package net.minecraft.client.data.models;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.client.data.models.model.ItemModelUtils;
/*     */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ class ItemInfoCollector implements ItemModelOutput {
/*  89 */   private final Map<Item, ClientItem> itemInfos = new HashMap<>();
/*     */   
/*  90 */   private final Map<Item, Item> copies = new HashMap<>();
/*     */   
/*     */   public void accept(Item item, ItemModel.Unbaked model, ClientItem.Properties properties) {
/*  94 */     register(item, new ClientItem(model, properties));
/*     */   }
/*     */   
/*     */   private void register(Item item, ClientItem itemInfo) {
/*  98 */     ClientItem prev = this.itemInfos.put(item, itemInfo);
/*  99 */     if (prev != null)
/* 100 */       throw new IllegalStateException("Duplicate item model definition for " + String.valueOf(item)); 
/*     */   }
/*     */   
/*     */   public void copy(Item donor, Item acceptor) {
/* 106 */     this.copies.put(acceptor, donor);
/*     */   }
/*     */   
/*     */   public void finalizeAndValidate() {
/* 112 */     BuiltInRegistries.ITEM.forEach(item -> {
/*     */           if (this.copies.containsKey(item))
/*     */             return; 
/*     */           if (item instanceof BlockItem) {
/*     */             BlockItem blockItem = (BlockItem)item;
/*     */             if (!this.itemInfos.containsKey(blockItem)) {
/*     */               Identifier targetModel = ModelLocationUtils.getModelLocation(blockItem.getBlock());
/*     */               accept((Item)blockItem, ItemModelUtils.plainModel(targetModel));
/*     */             } 
/*     */           } 
/*     */         });
/* 123 */     this.copies.forEach((acceptor, donor) -> {
/*     */           ClientItem donorInfo = this.itemInfos.get(donor);
/*     */           if (donorInfo == null)
/*     */             throw new IllegalStateException("Missing donor: " + String.valueOf(donor) + " -> " + String.valueOf(acceptor)); 
/*     */           register(acceptor, donorInfo);
/*     */         });
/* 134 */     List<Identifier> missingDefinitions = BuiltInRegistries.ITEM.listElements().filter(e -> !this.itemInfos.containsKey(e.value())).map(e -> e.key().identifier()).toList();
/* 136 */     if (!missingDefinitions.isEmpty())
/* 137 */       throw new IllegalStateException("Missing item model definitions for: " + String.valueOf(missingDefinitions)); 
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
/* 142 */     return DataProvider.saveAll(cache, ClientItem.CODEC, item -> pathProvider.json(item.builtInRegistryHolder().key().identifier()), this.itemInfos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\ModelProvider$ItemInfoCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
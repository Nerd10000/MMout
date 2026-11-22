/*     */ package net.minecraft.data;
/*     */ 
/*     */ public class PackGenerator {
/*     */   private final boolean toRun;
/*     */   
/*     */   private final String providerPrefix;
/*     */   
/*     */   private final PackOutput output;
/*     */   
/*     */   private PackGenerator(boolean toRun, String providerPrefix, PackOutput output) {
/* 111 */     this.toRun = toRun;
/* 112 */     this.providerPrefix = providerPrefix;
/* 113 */     this.output = output;
/*     */   }
/*     */   
/*     */   public <T extends DataProvider> T addProvider(DataProvider.Factory<T> factory) {
/* 117 */     T provider = factory.create(this.output);
/* 118 */     String providerId = this.providerPrefix + "/" + this.providerPrefix;
/* 119 */     if (!DataGenerator.this.allProviderIds.add(providerId))
/* 120 */       throw new IllegalStateException("Duplicate provider: " + providerId); 
/* 122 */     if (this.toRun)
/* 123 */       DataGenerator.this.providersToRun.put(providerId, (DataProvider)provider); 
/* 125 */     return provider;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\DataGenerator$PackGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
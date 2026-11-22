/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public final class RegistryData<T> extends Record {
/*    */   private final ResourceKey<? extends Registry<T>> key;
/*    */   
/*    */   private final Codec<T> elementCodec;
/*    */   
/*    */   private final RegistryValidator<T> validator;
/*    */   
/*    */   public RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec, RegistryValidator<T> validator) {
/* 85 */     this.key = key;
/* 85 */     this.elementCodec = elementCodec;
/* 85 */     this.validator = validator;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>;
/*    */   }
/*    */   
/*    */   public ResourceKey<? extends Registry<T>> key() {
/* 85 */     return this.key;
/*    */   }
/*    */   
/*    */   public Codec<T> elementCodec() {
/* 85 */     return this.elementCodec;
/*    */   }
/*    */   
/*    */   public RegistryValidator<T> validator() {
/* 85 */     return this.validator;
/*    */   }
/*    */   
/*    */   private RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec) {
/* 87 */     this(key, elementCodec, RegistryValidator.none());
/*    */   }
/*    */   
/*    */   public void runWithArguments(BiConsumer<ResourceKey<? extends Registry<T>>, Codec<T>> output) {
/* 91 */     output.accept(this.key, this.elementCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryDataLoader$RegistryData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
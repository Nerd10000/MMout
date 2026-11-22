/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ class null implements Codec<Component> {
/*    */   public <T> DataResult<Pair<Component, T>> decode(DynamicOps<T> ops, T input) {
/* 53 */     return ComponentSerialization.CODEC.decode(ops, input).flatMap(pair -> isTooLarge(ops, (Component)pair.getFirst()) ? DataResult.error(()) : DataResult.success(pair));
/*    */   }
/*    */   
/*    */   public <T> DataResult<T> encode(Component input, DynamicOps<T> ops, T prefix) {
/* 63 */     return ComponentSerialization.CODEC.encodeStart(ops, input);
/*    */   }
/*    */   
/*    */   private <T> boolean isTooLarge(DynamicOps<T> ops, Component input) {
/* 67 */     DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(asJsonOps(ops), input);
/* 68 */     return (json.isSuccess() && GsonHelper.encodesLongerThan((JsonElement)json.getOrThrow(), maxFlatSize));
/*    */   }
/*    */   
/*    */   private static <T> DynamicOps<JsonElement> asJsonOps(DynamicOps<T> ops) {
/* 72 */     if (ops instanceof RegistryOps) {
/* 72 */       RegistryOps<T> registryOps = (RegistryOps<T>)ops;
/* 73 */       return (DynamicOps<JsonElement>)registryOps.withParent((DynamicOps)JsonOps.INSTANCE);
/*    */     } 
/* 75 */     return (DynamicOps<JsonElement>)JsonOps.INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ComponentSerialization$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
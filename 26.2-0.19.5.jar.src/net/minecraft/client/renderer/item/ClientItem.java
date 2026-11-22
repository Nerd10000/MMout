/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RegistryContextSwapper;
/*    */ 
/*    */ public final class ClientItem extends Record {
/*    */   private final ItemModel.Unbaked model;
/*    */   
/*    */   private final Properties properties;
/*    */   
/*    */   private final RegistryContextSwapper registrySwapper;
/*    */   
/*    */   public static final Codec<ClientItem> CODEC;
/*    */   
/*    */   public ClientItem(ItemModel.Unbaked model, Properties properties, RegistryContextSwapper registrySwapper) {
/* 10 */     this.model = model;
/* 10 */     this.properties = properties;
/* 10 */     this.registrySwapper = registrySwapper;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ClientItem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ClientItem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ClientItem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/ClientItem;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemModel.Unbaked model() {
/* 10 */     return this.model;
/*    */   }
/*    */   
/*    */   public Properties properties() {
/* 10 */     return this.properties;
/*    */   }
/*    */   
/*    */   public RegistryContextSwapper registrySwapper() {
/* 10 */     return this.registrySwapper;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ItemModels.CODEC.fieldOf("model").forGetter(ClientItem::model), (App)Properties.MAP_CODEC.forGetter(ClientItem::properties)).apply((Applicative)i, ClientItem::new));
/*    */   }
/*    */   
/*    */   public ClientItem(ItemModel.Unbaked model, Properties properties) {
/* 21 */     this(model, properties, null);
/*    */   }
/*    */   
/*    */   public ClientItem withRegistrySwapper(RegistryContextSwapper registrySwapper) {
/* 25 */     return new ClientItem(this.model, this.properties, registrySwapper);
/*    */   }
/*    */   
/*    */   public static final class Properties extends Record {
/*    */     private final boolean handAnimationOnSwap;
/*    */     
/*    */     private final boolean oversizedInGui;
/*    */     
/*    */     private final float swapAnimationScale;
/*    */     
/*    */     public Properties(boolean handAnimationOnSwap, boolean oversizedInGui, float swapAnimationScale) {
/* 29 */       this.handAnimationOnSwap = handAnimationOnSwap;
/* 29 */       this.oversizedInGui = oversizedInGui;
/* 29 */       this.swapAnimationScale = swapAnimationScale;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public boolean handAnimationOnSwap() {
/* 29 */       return this.handAnimationOnSwap;
/*    */     }
/*    */     
/*    */     public boolean oversizedInGui() {
/* 29 */       return this.oversizedInGui;
/*    */     }
/*    */     
/*    */     public float swapAnimationScale() {
/* 29 */       return this.swapAnimationScale;
/*    */     }
/*    */     
/* 34 */     public static final Properties DEFAULT = new Properties(true, false, 1.0F);
/*    */     
/*    */     public static final MapCodec<Properties> MAP_CODEC;
/*    */     
/*    */     static {
/* 36 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("hand_animation_on_swap", Boolean.valueOf(true)).forGetter(Properties::handAnimationOnSwap), (App)Codec.BOOL.optionalFieldOf("oversized_in_gui", Boolean.valueOf(false)).forGetter(Properties::oversizedInGui), (App)Codec.FLOAT.optionalFieldOf("swap_animation_scale", Float.valueOf(1.0F)).forGetter(Properties::swapAnimationScale)).apply((Applicative)i, Properties::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ClientItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
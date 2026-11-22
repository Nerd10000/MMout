/*    */ package net.minecraft.client.renderer.item.properties.conditional;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.KeyMapping;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class IsKeybindDown extends Record implements ConditionalItemModelProperty {
/*    */   private final KeyMapping keybind;
/*    */   
/*    */   private static final Codec<KeyMapping> KEYBIND_CODEC;
/*    */   
/*    */   public static final MapCodec<IsKeybindDown> MAP_CODEC;
/*    */   
/*    */   public IsKeybindDown(KeyMapping keybind) {
/* 14 */     this.keybind = keybind;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public KeyMapping keybind() {
/* 14 */     return this.keybind;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     KEYBIND_CODEC = Codec.STRING.comapFlatMap(id -> {
/*    */           KeyMapping mapping = KeyMapping.get(id);
/*    */           return (mapping != null) ? DataResult.success(mapping) : DataResult.error(());
/* 15 */         }KeyMapping::getName);
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)KEYBIND_CODEC.fieldOf("keybind").forGetter(IsKeybindDown::keybind)).apply((Applicative)i, IsKeybindDown::new));
/*    */   }
/*    */   
/*    */   public boolean get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 29 */     return this.keybind.isDown();
/*    */   }
/*    */   
/*    */   public MapCodec<IsKeybindDown> type() {
/* 34 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\conditional\IsKeybindDown.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
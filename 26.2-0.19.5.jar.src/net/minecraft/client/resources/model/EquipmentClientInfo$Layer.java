/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class Layer extends Record {
/*    */   private final Identifier textureId;
/*    */   
/*    */   private final Optional<EquipmentClientInfo.Dyeable> dyeable;
/*    */   
/*    */   private final boolean usePlayerTexture;
/*    */   
/*    */   public static final Codec<Layer> CODEC;
/*    */   
/*    */   public Layer(Identifier textureId, Optional<EquipmentClientInfo.Dyeable> dyeable, boolean usePlayerTexture) {
/* 33 */     this.textureId = textureId;
/* 33 */     this.dyeable = dyeable;
/* 33 */     this.usePlayerTexture = usePlayerTexture;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/EquipmentClientInfo$Layer;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier textureId() {
/* 33 */     return this.textureId;
/*    */   }
/*    */   
/*    */   public Optional<EquipmentClientInfo.Dyeable> dyeable() {
/* 33 */     return this.dyeable;
/*    */   }
/*    */   
/*    */   public boolean usePlayerTexture() {
/* 33 */     return this.usePlayerTexture;
/*    */   }
/*    */   
/*    */   static {
/* 39 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Layer::textureId), (App)EquipmentClientInfo.Dyeable.CODEC.optionalFieldOf("dyeable").forGetter(Layer::dyeable), (App)Codec.BOOL.optionalFieldOf("use_player_texture", Boolean.valueOf(false)).forGetter(Layer::usePlayerTexture)).apply((Applicative)i, Layer::new));
/*    */   }
/*    */   
/*    */   public Layer(Identifier textureId) {
/* 46 */     this(textureId, Optional.empty(), false);
/*    */   }
/*    */   
/*    */   public static Layer leatherDyeable(Identifier textureId, boolean dyeable) {
/* 50 */     return new Layer(textureId, dyeable ? Optional.<EquipmentClientInfo.Dyeable>of(new EquipmentClientInfo.Dyeable(Optional.of(Integer.valueOf(-6265536)))) : Optional.<EquipmentClientInfo.Dyeable>empty(), false);
/*    */   }
/*    */   
/*    */   public static Layer onlyIfDyed(Identifier textureId, boolean dyeable) {
/* 54 */     return new Layer(textureId, dyeable ? Optional.<EquipmentClientInfo.Dyeable>of(new EquipmentClientInfo.Dyeable(Optional.empty())) : Optional.<EquipmentClientInfo.Dyeable>empty(), false);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(EquipmentClientInfo.LayerType type) {
/* 58 */     return this.textureId.withPath(path -> "textures/entity/equipment/" + type.getSerializedName() + "/" + path + ".png");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\EquipmentClientInfo$Layer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
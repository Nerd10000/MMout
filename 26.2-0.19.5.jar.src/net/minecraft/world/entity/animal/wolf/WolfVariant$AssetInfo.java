/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ 
/*    */ public final class AssetInfo extends Record {
/*    */   private final ClientAsset.ResourceTexture wild;
/*    */   
/*    */   private final ClientAsset.ResourceTexture tame;
/*    */   
/*    */   private final ClientAsset.ResourceTexture angry;
/*    */   
/*    */   public static final Codec<AssetInfo> CODEC;
/*    */   
/*    */   public AssetInfo(ClientAsset.ResourceTexture wild, ClientAsset.ResourceTexture tame, ClientAsset.ResourceTexture angry) {
/* 48 */     this.wild = wild;
/* 48 */     this.tame = tame;
/* 48 */     this.angry = angry;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture wild() {
/* 48 */     return this.wild;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture tame() {
/* 48 */     return this.tame;
/*    */   }
/*    */   
/*    */   public ClientAsset.ResourceTexture angry() {
/* 48 */     return this.angry;
/*    */   }
/*    */   
/*    */   static {
/* 53 */     CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ClientAsset.ResourceTexture.CODEC.fieldOf("wild").forGetter(AssetInfo::wild), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("tame").forGetter(AssetInfo::tame), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("angry").forGetter(AssetInfo::angry)).apply((Applicative)instance, AssetInfo::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\WolfVariant$AssetInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
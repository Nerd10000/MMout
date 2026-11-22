/*    */ package net.minecraft.client.resources.model.sprite;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class Material extends Record {
/*    */   private final Identifier sprite;
/*    */   
/*    */   private final boolean forceTranslucent;
/*    */   
/*    */   public Material(Identifier sprite, boolean forceTranslucent) {
/*  9 */     this.sprite = sprite;
/*  9 */     this.forceTranslucent = forceTranslucent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/Material;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/Material;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/Material;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/Material;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/Material;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/Material;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier sprite() {
/*  9 */     return this.sprite;
/*    */   }
/*    */   
/*    */   public boolean forceTranslucent() {
/*  9 */     return this.forceTranslucent;
/*    */   }
/*    */   
/* 13 */   private static final Codec<Material> SIMPLE_CODEC = Identifier.CODEC.xmap(Material::new, Material::sprite);
/*    */   
/*    */   private static final Codec<Material> FULL_CODEC;
/*    */   
/*    */   public static final Codec<Material> CODEC;
/*    */   
/*    */   static {
/* 15 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("sprite").forGetter(Material::sprite), (App)Codec.BOOL.optionalFieldOf("force_translucent", Boolean.valueOf(false)).forGetter(Material::forceTranslucent)).apply((Applicative)i, Material::new));
/* 20 */     CODEC = Codec.either(SIMPLE_CODEC, FULL_CODEC).xmap(Either::unwrap, material -> material.forceTranslucent ? Either.right(material) : Either.left(material));
/*    */   }
/*    */   
/*    */   public Material(Identifier sprite) {
/* 26 */     this(sprite, false);
/*    */   }
/*    */   
/*    */   public Material withForceTranslucent(boolean forceTranslucent) {
/* 30 */     return new Material(this.sprite, forceTranslucent);
/*    */   }
/*    */   
/*    */   public static final class Baked extends Record {
/*    */     private final TextureAtlasSprite sprite;
/*    */     
/*    */     private final boolean forceTranslucent;
/*    */     
/*    */     public Baked(TextureAtlasSprite sprite, boolean forceTranslucent) {
/* 33 */       this.sprite = sprite;
/* 33 */       this.forceTranslucent = forceTranslucent;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/Material$Baked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/Material$Baked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/Material$Baked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/Material$Baked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/Material$Baked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/Material$Baked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public TextureAtlasSprite sprite() {
/* 33 */       return this.sprite;
/*    */     }
/*    */     
/*    */     public boolean forceTranslucent() {
/* 33 */       return this.forceTranslucent;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\Material.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
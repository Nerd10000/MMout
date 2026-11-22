/*    */ package net.minecraft.client.renderer.texture.atlas.sources;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class Region extends Record {
/*    */   private final Identifier sprite;
/*    */   
/*    */   private final double x;
/*    */   
/*    */   private final double y;
/*    */   
/*    */   private final double width;
/*    */   
/*    */   private final double height;
/*    */   
/*    */   public static final Codec<Region> CODEC;
/*    */   
/*    */   public Region(Identifier sprite, double x, double y, double width, double height) {
/* 57 */     this.sprite = sprite;
/* 57 */     this.x = x;
/* 57 */     this.y = y;
/* 57 */     this.width = width;
/* 57 */     this.height = height;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier sprite() {
/* 57 */     return this.sprite;
/*    */   }
/*    */   
/*    */   public double x() {
/* 57 */     return this.x;
/*    */   }
/*    */   
/*    */   public double y() {
/* 57 */     return this.y;
/*    */   }
/*    */   
/*    */   public double width() {
/* 57 */     return this.width;
/*    */   }
/*    */   
/*    */   public double height() {
/* 57 */     return this.height;
/*    */   }
/*    */   
/*    */   static {
/* 58 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("sprite").forGetter(Region::sprite), (App)Codec.DOUBLE.fieldOf("x").forGetter(Region::x), (App)Codec.DOUBLE.fieldOf("y").forGetter(Region::y), (App)Codec.DOUBLE.fieldOf("width").forGetter(Region::width), (App)Codec.DOUBLE.fieldOf("height").forGetter(Region::height)).apply((Applicative)i, Region::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\sources\Unstitcher$Region.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ public interface FontDescription {
/*    */   public static final Codec<FontDescription> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = Identifier.CODEC.flatComapMap(Resource::new, fontDescription -> {
/*    */           if (fontDescription instanceof Resource) {
/*    */             Resource resource = (Resource)fontDescription;
/*    */             return DataResult.success(resource.id());
/*    */           } 
/*    */           return DataResult.error(());
/*    */         });
/*    */   }
/*    */   
/* 20 */   public static final Resource DEFAULT = new Resource(Identifier.withDefaultNamespace("default"));
/*    */   
/*    */   public static final class Resource extends Record implements FontDescription {
/*    */     private final Identifier id;
/*    */     
/*    */     public Resource(Identifier id) {
/* 22 */       this.id = id;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/FontDescription$Resource;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$Resource;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/FontDescription$Resource;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$Resource;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/FontDescription$Resource;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/FontDescription$Resource;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier id() {
/* 22 */       return this.id;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class AtlasSprite extends Record implements FontDescription {
/*    */     private final Identifier atlasId;
/*    */     
/*    */     private final Identifier spriteId;
/*    */     
/*    */     public AtlasSprite(Identifier atlasId, Identifier spriteId) {
/* 25 */       this.atlasId = atlasId;
/* 25 */       this.spriteId = spriteId;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$AtlasSprite;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$AtlasSprite;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/FontDescription$AtlasSprite;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier atlasId() {
/* 25 */       return this.atlasId;
/*    */     }
/*    */     
/*    */     public Identifier spriteId() {
/* 25 */       return this.spriteId;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class PlayerSprite extends Record implements FontDescription {
/*    */     private final ResolvableProfile profile;
/*    */     
/*    */     private final boolean hat;
/*    */     
/*    */     public PlayerSprite(ResolvableProfile profile, boolean hat) {
/* 28 */       this.profile = profile;
/* 28 */       this.hat = hat;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ResolvableProfile profile() {
/* 28 */       return this.profile;
/*    */     }
/*    */     
/*    */     public boolean hat() {
/* 28 */       return this.hat;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\FontDescription.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
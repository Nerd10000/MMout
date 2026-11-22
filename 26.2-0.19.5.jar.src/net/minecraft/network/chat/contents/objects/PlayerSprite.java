/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.FontDescription;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ public final class PlayerSprite extends Record implements ObjectInfo {
/*    */   private final ResolvableProfile player;
/*    */   
/*    */   private final boolean hat;
/*    */   
/*    */   public static final MapCodec<PlayerSprite> MAP_CODEC;
/*    */   
/*    */   public PlayerSprite(ResolvableProfile player, boolean hat) {
/*  9 */     this.player = player;
/*  9 */     this.hat = hat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/objects/PlayerSprite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/objects/PlayerSprite;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResolvableProfile player() {
/*  9 */     return this.player;
/*    */   }
/*    */   
/*    */   public boolean hat() {
/*  9 */     return this.hat;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResolvableProfile.CODEC.fieldOf("player").forGetter(PlayerSprite::player), (App)Codec.BOOL.optionalFieldOf("hat", Boolean.valueOf(true)).forGetter(PlayerSprite::hat)).apply((Applicative)i, PlayerSprite::new));
/*    */   }
/*    */   
/*    */   public FontDescription fontDescription() {
/* 20 */     return (FontDescription)new FontDescription.PlayerSprite(this.player, this.hat);
/*    */   }
/*    */   
/*    */   public String defaultFallback() {
/* 25 */     return this.player.name().map(name -> "[" + name + " head]").orElse("[unknown player head]");
/*    */   }
/*    */   
/*    */   public MapCodec<PlayerSprite> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\objects\PlayerSprite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
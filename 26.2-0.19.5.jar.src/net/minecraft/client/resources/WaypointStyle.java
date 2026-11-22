/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class WaypointStyle extends Record {
/*    */   private final int nearDistance;
/*    */   
/*    */   private final int farDistance;
/*    */   
/*    */   private final List<Identifier> sprites;
/*    */   
/*    */   private final List<Identifier> spriteLocations;
/*    */   
/*    */   @VisibleForTesting
/*    */   public static final String ICON_LOCATION_PREFIX = "hud/locator_bar_dot/";
/*    */   
/*    */   public static final int DEFAULT_NEAR_DISTANCE = 128;
/*    */   
/*    */   public static final int DEFAULT_FAR_DISTANCE = 332;
/*    */   
/*    */   public WaypointStyle(int nearDistance, int farDistance, List<Identifier> sprites, List<Identifier> spriteLocations) {
/* 14 */     this.nearDistance = nearDistance;
/* 14 */     this.farDistance = farDistance;
/* 14 */     this.sprites = sprites;
/* 14 */     this.spriteLocations = spriteLocations;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/WaypointStyle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/WaypointStyle;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/WaypointStyle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/WaypointStyle;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/WaypointStyle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/WaypointStyle;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int nearDistance() {
/* 14 */     return this.nearDistance;
/*    */   }
/*    */   
/*    */   public int farDistance() {
/* 14 */     return this.farDistance;
/*    */   }
/*    */   
/*    */   public List<Identifier> sprites() {
/* 14 */     return this.sprites;
/*    */   }
/*    */   
/*    */   public List<Identifier> spriteLocations() {
/* 14 */     return this.spriteLocations;
/*    */   }
/*    */   
/* 26 */   private static final Codec<Integer> DISTANCE_CODEC = Codec.intRange(0, 60000000);
/*    */   
/*    */   public static final Codec<WaypointStyle> CODEC;
/*    */   
/*    */   static {
/* 32 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)DISTANCE_CODEC.optionalFieldOf("near_distance", Integer.valueOf(128)).forGetter(WaypointStyle::nearDistance), (App)DISTANCE_CODEC.optionalFieldOf("far_distance", Integer.valueOf(332)).forGetter(WaypointStyle::farDistance), (App)ExtraCodecs.nonEmptyList(Identifier.CODEC.listOf()).fieldOf("sprites").forGetter(WaypointStyle::sprites)).apply((Applicative)i, WaypointStyle::new)).validate(WaypointStyle::validate);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public DataResult<WaypointStyle> validate() {
/* 36 */     if (this.sprites.isEmpty())
/* 37 */       return DataResult.error(() -> "Must have at least one sprite icon"); 
/* 39 */     if (this.nearDistance <= 0)
/* 40 */       return DataResult.error(() -> "Near distance (" + this.nearDistance + ") must be greater than zero"); 
/* 42 */     if (this.nearDistance >= this.farDistance)
/* 43 */       return DataResult.error(() -> "Far distance (" + this.farDistance + ") cannot be closer or equal to near distance (" + this.nearDistance + ")"); 
/* 45 */     return DataResult.success(this);
/*    */   }
/*    */   
/*    */   public WaypointStyle(int nearDistance, int farDistance, List<Identifier> sprites) {
/* 49 */     this(nearDistance, farDistance, sprites, sprites
/*    */         
/* 51 */         .stream().map(sprite -> sprite.withPrefix("hud/locator_bar_dot/")).toList());
/*    */   }
/*    */   
/*    */   public Identifier sprite(float distance) {
/* 56 */     if (distance < this.nearDistance)
/* 57 */       return this.spriteLocations.getFirst(); 
/* 59 */     if (distance >= this.farDistance)
/* 60 */       return this.spriteLocations.getLast(); 
/* 62 */     if (this.spriteLocations.size() == 1)
/* 63 */       return this.spriteLocations.getFirst(); 
/* 66 */     if (this.spriteLocations.size() == 3)
/* 67 */       return this.spriteLocations.get(1); 
/* 69 */     int index = Mth.lerpInt((distance - this.nearDistance) / (this.farDistance - this.nearDistance), 1, this.spriteLocations.size() - 1);
/* 70 */     return this.spriteLocations.get(index);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\WaypointStyle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
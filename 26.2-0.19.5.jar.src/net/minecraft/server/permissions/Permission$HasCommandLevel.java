/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class HasCommandLevel extends Record implements Permission {
/*    */   private final PermissionLevel level;
/*    */   
/*    */   public static final MapCodec<HasCommandLevel> MAP_CODEC;
/*    */   
/*    */   public HasCommandLevel(PermissionLevel level) {
/* 44 */     this.level = level;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PermissionLevel level() {
/* 44 */     return this.level;
/*    */   }
/*    */   
/*    */   static {
/* 45 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PermissionLevel.CODEC.fieldOf("level").forGetter(HasCommandLevel::level)).apply((Applicative)i, HasCommandLevel::new));
/*    */   }
/*    */   
/*    */   public MapCodec<HasCommandLevel> codec() {
/* 51 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\Permission$HasCommandLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
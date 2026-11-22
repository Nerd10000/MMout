/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface Permission {
/*    */   public static final Codec<Permission> FULL_CODEC;
/*    */   
/*    */   public static final Codec<Permission> CODEC;
/*    */   
/*    */   static {
/* 12 */     FULL_CODEC = BuiltInRegistries.PERMISSION_TYPE.byNameCodec().dispatch(Permission::codec, c -> c);
/* 17 */     CODEC = Codec.either(FULL_CODEC, Identifier.CODEC).xmap(e -> (Permission)e.map((), Atom::create), permission -> {
/*    */           Atom atom = (Atom)permission;
/*    */           return (permission instanceof Atom) ? Either.right(atom.id()) : Either.left(permission);
/*    */         });
/*    */   }
/*    */   
/*    */   MapCodec<? extends Permission> codec();
/*    */   
/*    */   public static final class Atom extends Record implements Permission {
/*    */     private final Identifier id;
/*    */     
/*    */     public static final MapCodec<Atom> MAP_CODEC;
/*    */     
/*    */     public Atom(Identifier id) {
/* 25 */       this.id = id;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$Atom;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$Atom;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$Atom;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$Atom;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$Atom;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier id() {
/* 25 */       return this.id;
/*    */     }
/*    */     
/*    */     static {
/* 26 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(Atom::id)).apply((Applicative)i, Atom::new));
/*    */     }
/*    */     
/*    */     public MapCodec<Atom> codec() {
/* 32 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public static Atom create(String name) {
/* 36 */       return create(Identifier.withDefaultNamespace(name));
/*    */     }
/*    */     
/*    */     public static Atom create(Identifier id) {
/* 40 */       return new Atom(id);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class HasCommandLevel extends Record implements Permission {
/*    */     private final PermissionLevel level;
/*    */     
/*    */     public static final MapCodec<HasCommandLevel> MAP_CODEC;
/*    */     
/*    */     public HasCommandLevel(PermissionLevel level) {
/* 44 */       this.level = level;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/permissions/Permission$HasCommandLevel;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/permissions/Permission$HasCommandLevel;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public PermissionLevel level() {
/* 44 */       return this.level;
/*    */     }
/*    */     
/*    */     static {
/* 45 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PermissionLevel.CODEC.fieldOf("level").forGetter(HasCommandLevel::level)).apply((Applicative)i, HasCommandLevel::new));
/*    */     }
/*    */     
/*    */     public MapCodec<HasCommandLevel> codec() {
/* 51 */       return MAP_CODEC;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\Permission.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
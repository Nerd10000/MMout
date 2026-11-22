/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class WorldCoordinates extends Record implements Coordinates {
/*    */   private final WorldCoordinate x;
/*    */   
/*    */   private final WorldCoordinate y;
/*    */   
/*    */   private final WorldCoordinate z;
/*    */   
/*    */   public WorldCoordinates(WorldCoordinate x, WorldCoordinate y, WorldCoordinate z) {
/*  9 */     this.x = x;
/*  9 */     this.y = y;
/*  9 */     this.z = z;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WorldCoordinate x() {
/*  9 */     return this.x;
/*    */   }
/*    */   
/*    */   public WorldCoordinate y() {
/*  9 */     return this.y;
/*    */   }
/*    */   
/*    */   public WorldCoordinate z() {
/*  9 */     return this.z;
/*    */   }
/*    */   
/* 10 */   public static final WorldCoordinates ZERO_ROTATION = absolute(new Vec2(0.0F, 0.0F));
/*    */   
/*    */   public Vec3 getPosition(CommandSourceStack sender) {
/* 14 */     Vec3 pos = sender.getPosition();
/* 15 */     return new Vec3(this.x.get(pos.x), this.y.get(pos.y), this.z.get(pos.z));
/*    */   }
/*    */   
/*    */   public Vec2 getRotation(CommandSourceStack sender) {
/* 20 */     Vec2 rot = sender.getRotation();
/* 21 */     return new Vec2((float)this.x.get(rot.x), (float)this.y.get(rot.y));
/*    */   }
/*    */   
/*    */   public boolean isXRelative() {
/* 26 */     return this.x.isRelative();
/*    */   }
/*    */   
/*    */   public boolean isYRelative() {
/* 31 */     return this.y.isRelative();
/*    */   }
/*    */   
/*    */   public boolean isZRelative() {
/* 36 */     return this.z.isRelative();
/*    */   }
/*    */   
/*    */   public static WorldCoordinates parseInt(StringReader reader) throws CommandSyntaxException {
/* 40 */     int start = reader.getCursor();
/* 41 */     WorldCoordinate x = WorldCoordinate.parseInt(reader);
/* 42 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 43 */       reader.setCursor(start);
/* 44 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 46 */     reader.skip();
/* 47 */     WorldCoordinate y = WorldCoordinate.parseInt(reader);
/* 48 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 49 */       reader.setCursor(start);
/* 50 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 52 */     reader.skip();
/* 53 */     WorldCoordinate z = WorldCoordinate.parseInt(reader);
/* 54 */     return new WorldCoordinates(x, y, z);
/*    */   }
/*    */   
/*    */   public static WorldCoordinates parseDouble(StringReader reader, boolean centerCorrect) throws CommandSyntaxException {
/* 58 */     int start = reader.getCursor();
/* 59 */     WorldCoordinate x = WorldCoordinate.parseDouble(reader, centerCorrect);
/* 60 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 61 */       reader.setCursor(start);
/* 62 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 64 */     reader.skip();
/* 65 */     WorldCoordinate y = WorldCoordinate.parseDouble(reader, false);
/* 66 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 67 */       reader.setCursor(start);
/* 68 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 70 */     reader.skip();
/* 71 */     WorldCoordinate z = WorldCoordinate.parseDouble(reader, centerCorrect);
/* 72 */     return new WorldCoordinates(x, y, z);
/*    */   }
/*    */   
/*    */   public static WorldCoordinates absolute(double x, double y, double z) {
/* 76 */     return new WorldCoordinates(new WorldCoordinate(false, x), new WorldCoordinate(false, y), new WorldCoordinate(false, z));
/*    */   }
/*    */   
/*    */   public static WorldCoordinates absolute(Vec2 rotation) {
/* 80 */     return new WorldCoordinates(new WorldCoordinate(false, rotation.x), new WorldCoordinate(false, rotation.y), new WorldCoordinate(true, 0.0D));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\coordinates\WorldCoordinates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Date;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.world.level.storage.DataVersion;
/*    */ 
/*    */ public interface WorldVersion {
/*    */   DataVersion dataVersion();
/*    */   
/*    */   String id();
/*    */   
/*    */   String name();
/*    */   
/*    */   int protocolVersion();
/*    */   
/*    */   PackFormat packVersion(PackType paramPackType);
/*    */   
/*    */   Date buildTime();
/*    */   
/*    */   boolean stable();
/*    */   
/*    */   public static final class Simple extends Record implements WorldVersion {
/*    */     private final String id;
/*    */     
/*    */     private final String name;
/*    */     
/*    */     private final DataVersion dataVersion;
/*    */     
/*    */     private final int protocolVersion;
/*    */     
/*    */     private final PackFormat resourcePackVersion;
/*    */     
/*    */     private final PackFormat datapackVersion;
/*    */     
/*    */     private final Date buildTime;
/*    */     
/*    */     private final boolean stable;
/*    */     
/*    */     public Simple(String id, String name, DataVersion dataVersion, int protocolVersion, PackFormat resourcePackVersion, PackFormat datapackVersion, Date buildTime, boolean stable) {
/* 71 */       this.id = id;
/* 71 */       this.name = name;
/* 71 */       this.dataVersion = dataVersion;
/* 71 */       this.protocolVersion = protocolVersion;
/* 71 */       this.resourcePackVersion = resourcePackVersion;
/* 71 */       this.datapackVersion = datapackVersion;
/* 71 */       this.buildTime = buildTime;
/* 71 */       this.stable = stable;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/WorldVersion$Simple;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/WorldVersion$Simple;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/WorldVersion$Simple;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/WorldVersion$Simple;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/WorldVersion$Simple;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/WorldVersion$Simple;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public String id() {
/* 71 */       return this.id;
/*    */     }
/*    */     
/*    */     public String name() {
/* 71 */       return this.name;
/*    */     }
/*    */     
/*    */     public DataVersion dataVersion() {
/* 71 */       return this.dataVersion;
/*    */     }
/*    */     
/*    */     public int protocolVersion() {
/* 71 */       return this.protocolVersion;
/*    */     }
/*    */     
/*    */     public PackFormat resourcePackVersion() {
/* 71 */       return this.resourcePackVersion;
/*    */     }
/*    */     
/*    */     public PackFormat datapackVersion() {
/* 71 */       return this.datapackVersion;
/*    */     }
/*    */     
/*    */     public Date buildTime() {
/* 71 */       return this.buildTime;
/*    */     }
/*    */     
/*    */     public boolean stable() {
/* 71 */       return this.stable;
/*    */     }
/*    */     
/*    */     public PackFormat packVersion(PackType packType) {
/* 83 */       switch (packType) {
/*    */         default:
/* 83 */           throw new MatchException(null, null);
/*    */         case CLIENT_RESOURCES:
/*    */         
/*    */         case SERVER_DATA:
/*    */           break;
/*    */       } 
/* 83 */       return 
/*    */         
/* 85 */         this.datapackVersion;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\WorldVersion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
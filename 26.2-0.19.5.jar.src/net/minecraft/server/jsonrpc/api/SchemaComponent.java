/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class SchemaComponent<T> extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final URI ref;
/*    */   
/*    */   private final Schema<T> schema;
/*    */   
/*    */   public SchemaComponent(String name, URI ref, Schema<T> schema) {
/*  6 */     this.name = name;
/*  6 */     this.ref = ref;
/*  6 */     this.schema = schema;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/SchemaComponent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/SchemaComponent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/SchemaComponent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/SchemaComponent<TT;>;
/*    */   }
/*    */   
/*    */   public String name() {
/*  6 */     return this.name;
/*    */   }
/*    */   
/*    */   public URI ref() {
/*  6 */     return this.ref;
/*    */   }
/*    */   
/*    */   public Schema<T> schema() {
/*  6 */     return this.schema;
/*    */   }
/*    */   
/*    */   public Schema<T> asRef() {
/*  8 */     return Schema.ofRef(this.ref, this.schema.codec());
/*    */   }
/*    */   
/*    */   public Schema<List<T>> asArray() {
/* 12 */     return Schema.arrayOf(asRef(), this.schema.codec());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\api\SchemaComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface Input {
/*    */   public static final Codec<Input> CODEC;
/*    */   
/*    */   static {
/* 74 */     CODEC = Codec.xor(PostChainConfig.TextureInput.CODEC, PostChainConfig.TargetInput.CODEC).xmap(either -> (Input)either.map(Function.identity(), Function.identity()), input -> {
/*    */           // Byte code:
/*    */           //   0: aload_0
/*    */           //   1: dup
/*    */           //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */           //   5: pop
/*    */           //   6: astore_1
/*    */           //   7: iconst_0
/*    */           //   8: istore_2
/*    */           //   9: aload_1
/*    */           //   10: iload_2
/*    */           //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/client/renderer/PostChainConfig$Input;I)I
/*    */           //   16: lookupswitch default -> 44, 0 -> 54, 1 -> 66
/*    */           //   44: new java/lang/MatchException
/*    */           //   47: dup
/*    */           //   48: aconst_null
/*    */           //   49: aconst_null
/*    */           //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*    */           //   53: athrow
/*    */           //   54: aload_1
/*    */           //   55: checkcast net/minecraft/client/renderer/PostChainConfig$TextureInput
/*    */           //   58: astore_3
/*    */           //   59: aload_3
/*    */           //   60: invokestatic left : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*    */           //   63: goto -> 77
/*    */           //   66: aload_1
/*    */           //   67: checkcast net/minecraft/client/renderer/PostChainConfig$TargetInput
/*    */           //   70: astore #4
/*    */           //   72: aload #4
/*    */           //   74: invokestatic right : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*    */           //   77: areturn
/*    */           // Line number table:
/*    */           //   Java source line number -> byte code offset
/*    */           //   #76	-> 0
/*    */           //   #77	-> 54
/*    */           //   #78	-> 66
/*    */           // Local variable table:
/*    */           //   start	length	slot	name	descriptor
/*    */           //   59	7	3	texture	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*    */           //   72	5	4	target	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*    */           //   7	70	1	selector0$temp	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*    */           //   9	68	2	index$1	I
/*    */           //   0	78	0	input	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*    */         });
/*    */   }
/*    */   
/*    */   Set<Identifier> referencedTargets();
/*    */   
/*    */   String samplerName();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChainConfig$Input.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
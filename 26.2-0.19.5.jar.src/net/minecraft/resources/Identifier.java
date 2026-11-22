/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Locale;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class Identifier implements Comparable<Identifier> {
/*  20 */   public static final Codec<Identifier> CODEC = Codec.STRING.comapFlatMap(Identifier::read, Identifier::toString).stable();
/*     */   
/*  21 */   public static final StreamCodec<ByteBuf, Identifier> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(Identifier::parse, Identifier::toString);
/*     */   
/*  23 */   public static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)Component.translatable("argument.id.invalid"));
/*     */   
/*     */   public static final char NAMESPACE_SEPARATOR = ':';
/*     */   
/*     */   public static final String DEFAULT_NAMESPACE = "minecraft";
/*     */   
/*     */   public static final String REALMS_NAMESPACE = "realms";
/*     */   
/*     */   public static final String ALLOWED_NAMESPACE_CHARACTERS = "[a-z0-9_.-]";
/*     */   
/*     */   private final String namespace;
/*     */   
/*     */   private final String path;
/*     */   
/*     */   private Identifier(String namespace, String path) {
/*  35 */     assert isValidNamespace(namespace);
/*  36 */     assert isValidPath(path);
/*  37 */     this.namespace = namespace;
/*  38 */     this.path = path;
/*     */   }
/*     */   
/*     */   private static Identifier createUntrusted(String namespace, String path) {
/*  42 */     return new Identifier(assertValidNamespace(namespace, path), assertValidPath(namespace, path));
/*     */   }
/*     */   
/*     */   public static Identifier fromNamespaceAndPath(String namespace, String path) {
/*  46 */     return createUntrusted(namespace, path);
/*     */   }
/*     */   
/*     */   public static Identifier parse(String identifier) {
/*  50 */     return bySeparator(identifier, ':');
/*     */   }
/*     */   
/*     */   public static Identifier withDefaultNamespace(String path) {
/*  54 */     return new Identifier("minecraft", assertValidPath("minecraft", path));
/*     */   }
/*     */   
/*     */   public static Identifier tryParse(String identifier) {
/*  58 */     return tryBySeparator(identifier, ':');
/*     */   }
/*     */   
/*     */   public static Identifier tryBuild(String namespace, String path) {
/*  62 */     if (isValidNamespace(namespace) && isValidPath(path))
/*  63 */       return new Identifier(namespace, path); 
/*  65 */     return null;
/*     */   }
/*     */   
/*     */   public static Identifier bySeparator(String identifier, char separator) {
/*  69 */     int separatorIndex = identifier.indexOf(separator);
/*  70 */     if (separatorIndex >= 0) {
/*  71 */       String path = identifier.substring(separatorIndex + 1);
/*  72 */       if (separatorIndex != 0) {
/*  73 */         String namespace = identifier.substring(0, separatorIndex);
/*  74 */         return createUntrusted(namespace, path);
/*     */       } 
/*  77 */       return withDefaultNamespace(path);
/*     */     } 
/*  81 */     return withDefaultNamespace(identifier);
/*     */   }
/*     */   
/*     */   public static Identifier tryBySeparator(String identifier, char separator) {
/*  85 */     int separatorIndex = identifier.indexOf(separator);
/*  86 */     if (separatorIndex >= 0) {
/*  87 */       String path = identifier.substring(separatorIndex + 1);
/*  88 */       if (!isValidPath(path))
/*  89 */         return null; 
/*  91 */       if (separatorIndex != 0) {
/*  92 */         String namespace = identifier.substring(0, separatorIndex);
/*  93 */         return isValidNamespace(namespace) ? new Identifier(namespace, path) : null;
/*     */       } 
/*  96 */       return new Identifier("minecraft", path);
/*     */     } 
/* 100 */     return isValidPath(identifier) ? new Identifier("minecraft", identifier) : null;
/*     */   }
/*     */   
/*     */   public static DataResult<Identifier> read(String input) {
/*     */     try {
/* 105 */       return DataResult.success(parse(input));
/* 106 */     } catch (IdentifierException e) {
/* 107 */       return DataResult.error(() -> "Not a valid resource location: " + input + " " + e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getPath() {
/* 112 */     return this.path;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/* 116 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public Identifier withPath(String newPath) {
/* 120 */     return new Identifier(this.namespace, assertValidPath(this.namespace, newPath));
/*     */   }
/*     */   
/*     */   public Identifier withPath(UnaryOperator<String> modifier) {
/* 124 */     return withPath(modifier.apply(this.path));
/*     */   }
/*     */   
/*     */   public Identifier withPrefix(String prefix) {
/* 128 */     return withPath(prefix + prefix);
/*     */   }
/*     */   
/*     */   public Identifier withSuffix(String suffix) {
/* 132 */     return withPath(this.path + this.path);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 137 */     return this.namespace + ":" + this.namespace;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 142 */     if (this == o)
/* 143 */       return true; 
/* 146 */     if (o instanceof Identifier) {
/* 146 */       Identifier that = (Identifier)o;
/* 147 */       return (this.namespace.equals(that.namespace) && this.path.equals(that.path));
/*     */     } 
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 155 */     return 31 * this.namespace.hashCode() + this.path.hashCode();
/*     */   }
/*     */   
/*     */   public int compareTo(Identifier o) {
/* 161 */     int result = this.path.compareTo(o.path);
/* 162 */     if (result == 0)
/* 163 */       result = this.namespace.compareTo(o.namespace); 
/* 165 */     return result;
/*     */   }
/*     */   
/*     */   public Path resolveAgainst(Path root) {
/* 169 */     Path resultingPath = root.resolve(getNamespace(), new String[] { getPath() });
/* 170 */     Path normalizedPath = resultingPath.normalize();
/* 171 */     Path normalizedRoot = root.normalize();
/* 172 */     if (!normalizedPath.startsWith(normalizedRoot))
/* 173 */       throw new IllegalStateException(String.format(Locale.ROOT, "Identifier \"%s\" tried to access path \"%s\" from root \"%s\"", new Object[] { this, normalizedPath, normalizedRoot })); 
/* 175 */     return resultingPath;
/*     */   }
/*     */   
/*     */   public String toDebugFileName() {
/* 179 */     return toString().replace('/', '_').replace(':', '_');
/*     */   }
/*     */   
/*     */   public String toLanguageKey() {
/* 183 */     return this.namespace + "." + this.namespace;
/*     */   }
/*     */   
/*     */   public String toShortLanguageKey() {
/* 187 */     return this.namespace.equals("minecraft") ? this.path : toLanguageKey();
/*     */   }
/*     */   
/*     */   public String toShortString() {
/* 191 */     return this.namespace.equals("minecraft") ? this.path : toString();
/*     */   }
/*     */   
/*     */   public String toLanguageKey(String prefix) {
/* 195 */     return prefix + "." + prefix;
/*     */   }
/*     */   
/*     */   public String toLanguageKey(String prefix, String suffix) {
/* 199 */     return prefix + "." + prefix + "." + toLanguageKey();
/*     */   }
/*     */   
/*     */   private static String readGreedy(StringReader reader) {
/* 203 */     int start = reader.getCursor();
/* 204 */     while (reader.canRead() && isAllowedInIdentifier(reader.peek()))
/* 205 */       reader.skip(); 
/* 207 */     return reader.getString().substring(start, reader.getCursor());
/*     */   }
/*     */   
/*     */   public static Identifier read(StringReader reader) throws CommandSyntaxException {
/* 211 */     int start = reader.getCursor();
/* 212 */     String raw = readGreedy(reader);
/*     */     try {
/* 214 */       return parse(raw);
/* 215 */     } catch (IdentifierException ex) {
/* 216 */       reader.setCursor(start);
/* 217 */       throw ERROR_INVALID.createWithContext(reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Identifier readNonEmpty(StringReader reader) throws CommandSyntaxException {
/* 222 */     int start = reader.getCursor();
/* 223 */     String raw = readGreedy(reader);
/* 224 */     if (raw.isEmpty())
/* 225 */       throw ERROR_INVALID.createWithContext(reader); 
/*     */     try {
/* 228 */       return parse(raw);
/* 229 */     } catch (IdentifierException ex) {
/* 230 */       reader.setCursor(start);
/* 231 */       throw ERROR_INVALID.createWithContext(reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isAllowedInIdentifier(char c) {
/* 236 */     return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '_' || c == ':' || c == '/' || c == '.' || c == '-');
/*     */   }
/*     */   
/*     */   public static boolean isValidPath(String path) {
/* 244 */     for (int i = 0; i < path.length(); i++) {
/* 245 */       if (!validPathChar(path.charAt(i)))
/* 246 */         return false; 
/*     */     } 
/* 249 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isValidNamespace(String namespace) {
/* 254 */     if (namespace.equals(".."))
/* 255 */       return false; 
/* 257 */     for (int i = 0; i < namespace.length(); i++) {
/* 258 */       if (!validNamespaceChar(namespace.charAt(i)))
/* 259 */         return false; 
/*     */     } 
/* 262 */     return true;
/*     */   }
/*     */   
/*     */   private static String assertValidNamespace(String namespace, String path) {
/* 266 */     if (!isValidNamespace(namespace))
/* 267 */       throw new IdentifierException("Non [a-z0-9_.-] character in namespace of identifier: " + namespace + ":" + path); 
/* 269 */     return namespace;
/*     */   }
/*     */   
/*     */   public static boolean validPathChar(char c) {
/* 273 */     return (c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '/' || c == '.');
/*     */   }
/*     */   
/*     */   private static boolean validNamespaceChar(char c) {
/* 277 */     return (c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.');
/*     */   }
/*     */   
/*     */   private static String assertValidPath(String namespace, String path) {
/* 281 */     if (!isValidPath(path))
/* 282 */       throw new IdentifierException("Non [a-z0-9/._-] character in path of location: " + namespace + ":" + path); 
/* 284 */     return path;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\Identifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
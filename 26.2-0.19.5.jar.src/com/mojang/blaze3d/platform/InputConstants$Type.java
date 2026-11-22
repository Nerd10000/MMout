/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ 
/*     */ public enum Type {
/*     */   MOUSE, SCANCODE, KEYSYM;
/*     */   
/*     */   private final BiFunction<Integer, String, Component> displayTextSupplier;
/*     */   
/*     */   private final String defaultPrefix;
/*     */   
/*     */   static {
/* 185 */     KEYSYM = new Type("KEYSYM", 0, "key.keyboard", (value, name) -> {
/*     */           if ("key.keyboard.unknown".equals(name))
/*     */             return (Component)Component.translatable(name); 
/*     */           String systemName = GLFW.glfwGetKeyName(value.intValue(), -1);
/*     */           return (systemName != null) ? (Component)Component.literal(systemName.toUpperCase(Locale.ROOT)) : (Component)Component.translatable(name);
/*     */         });
/* 192 */     SCANCODE = new Type("SCANCODE", 1, "scancode", (value, name) -> {
/*     */           String systemName = GLFW.glfwGetKeyName(-1, value.intValue());
/*     */           return (systemName != null) ? (Component)Component.literal(systemName) : (Component)Component.translatable(name);
/*     */         });
/* 196 */     MOUSE = new Type("MOUSE", 2, "key.mouse", (value, name) -> Language.getInstance().has(name) ? (Component)Component.translatable(name) : (Component)Component.translatable("key.mouse", new Object[] { Integer.valueOf(value.intValue() + 1) }));
/*     */   }
/*     */   
/*     */   private static void addKey(Type type, String name, int value) {
/* 199 */     InputConstants.Key key = new InputConstants.Key(name, type, value);
/* 200 */     type.map.put(value, key);
/*     */   }
/*     */   
/*     */   static {
/* 206 */     addKey(KEYSYM, "key.keyboard.unknown", -1);
/* 208 */     addKey(MOUSE, "key.mouse.left", 0);
/* 209 */     addKey(MOUSE, "key.mouse.right", 1);
/* 210 */     addKey(MOUSE, "key.mouse.middle", 2);
/* 211 */     addKey(MOUSE, "key.mouse.4", 3);
/* 212 */     addKey(MOUSE, "key.mouse.5", 4);
/* 213 */     addKey(MOUSE, "key.mouse.6", 5);
/* 214 */     addKey(MOUSE, "key.mouse.7", 6);
/* 215 */     addKey(MOUSE, "key.mouse.8", 7);
/* 217 */     addKey(KEYSYM, "key.keyboard.0", 48);
/* 218 */     addKey(KEYSYM, "key.keyboard.1", 49);
/* 219 */     addKey(KEYSYM, "key.keyboard.2", 50);
/* 220 */     addKey(KEYSYM, "key.keyboard.3", 51);
/* 221 */     addKey(KEYSYM, "key.keyboard.4", 52);
/* 222 */     addKey(KEYSYM, "key.keyboard.5", 53);
/* 223 */     addKey(KEYSYM, "key.keyboard.6", 54);
/* 224 */     addKey(KEYSYM, "key.keyboard.7", 55);
/* 225 */     addKey(KEYSYM, "key.keyboard.8", 56);
/* 226 */     addKey(KEYSYM, "key.keyboard.9", 57);
/* 228 */     addKey(KEYSYM, "key.keyboard.a", 65);
/* 229 */     addKey(KEYSYM, "key.keyboard.b", 66);
/* 230 */     addKey(KEYSYM, "key.keyboard.c", 67);
/* 231 */     addKey(KEYSYM, "key.keyboard.d", 68);
/* 232 */     addKey(KEYSYM, "key.keyboard.e", 69);
/* 233 */     addKey(KEYSYM, "key.keyboard.f", 70);
/* 234 */     addKey(KEYSYM, "key.keyboard.g", 71);
/* 235 */     addKey(KEYSYM, "key.keyboard.h", 72);
/* 236 */     addKey(KEYSYM, "key.keyboard.i", 73);
/* 237 */     addKey(KEYSYM, "key.keyboard.j", 74);
/* 238 */     addKey(KEYSYM, "key.keyboard.k", 75);
/* 239 */     addKey(KEYSYM, "key.keyboard.l", 76);
/* 240 */     addKey(KEYSYM, "key.keyboard.m", 77);
/* 241 */     addKey(KEYSYM, "key.keyboard.n", 78);
/* 242 */     addKey(KEYSYM, "key.keyboard.o", 79);
/* 243 */     addKey(KEYSYM, "key.keyboard.p", 80);
/* 244 */     addKey(KEYSYM, "key.keyboard.q", 81);
/* 245 */     addKey(KEYSYM, "key.keyboard.r", 82);
/* 246 */     addKey(KEYSYM, "key.keyboard.s", 83);
/* 247 */     addKey(KEYSYM, "key.keyboard.t", 84);
/* 248 */     addKey(KEYSYM, "key.keyboard.u", 85);
/* 249 */     addKey(KEYSYM, "key.keyboard.v", 86);
/* 250 */     addKey(KEYSYM, "key.keyboard.w", 87);
/* 251 */     addKey(KEYSYM, "key.keyboard.x", 88);
/* 252 */     addKey(KEYSYM, "key.keyboard.y", 89);
/* 253 */     addKey(KEYSYM, "key.keyboard.z", 90);
/* 255 */     addKey(KEYSYM, "key.keyboard.f1", 290);
/* 256 */     addKey(KEYSYM, "key.keyboard.f2", 291);
/* 257 */     addKey(KEYSYM, "key.keyboard.f3", 292);
/* 258 */     addKey(KEYSYM, "key.keyboard.f4", 293);
/* 259 */     addKey(KEYSYM, "key.keyboard.f5", 294);
/* 260 */     addKey(KEYSYM, "key.keyboard.f6", 295);
/* 261 */     addKey(KEYSYM, "key.keyboard.f7", 296);
/* 262 */     addKey(KEYSYM, "key.keyboard.f8", 297);
/* 263 */     addKey(KEYSYM, "key.keyboard.f9", 298);
/* 264 */     addKey(KEYSYM, "key.keyboard.f10", 299);
/* 265 */     addKey(KEYSYM, "key.keyboard.f11", 300);
/* 266 */     addKey(KEYSYM, "key.keyboard.f12", 301);
/* 267 */     addKey(KEYSYM, "key.keyboard.f13", 302);
/* 268 */     addKey(KEYSYM, "key.keyboard.f14", 303);
/* 269 */     addKey(KEYSYM, "key.keyboard.f15", 304);
/* 270 */     addKey(KEYSYM, "key.keyboard.f16", 305);
/* 271 */     addKey(KEYSYM, "key.keyboard.f17", 306);
/* 272 */     addKey(KEYSYM, "key.keyboard.f18", 307);
/* 273 */     addKey(KEYSYM, "key.keyboard.f19", 308);
/* 274 */     addKey(KEYSYM, "key.keyboard.f20", 309);
/* 275 */     addKey(KEYSYM, "key.keyboard.f21", 310);
/* 276 */     addKey(KEYSYM, "key.keyboard.f22", 311);
/* 277 */     addKey(KEYSYM, "key.keyboard.f23", 312);
/* 278 */     addKey(KEYSYM, "key.keyboard.f24", 313);
/* 279 */     addKey(KEYSYM, "key.keyboard.f25", 314);
/* 281 */     addKey(KEYSYM, "key.keyboard.num.lock", 282);
/* 282 */     addKey(KEYSYM, "key.keyboard.keypad.0", 320);
/* 283 */     addKey(KEYSYM, "key.keyboard.keypad.1", 321);
/* 284 */     addKey(KEYSYM, "key.keyboard.keypad.2", 322);
/* 285 */     addKey(KEYSYM, "key.keyboard.keypad.3", 323);
/* 286 */     addKey(KEYSYM, "key.keyboard.keypad.4", 324);
/* 287 */     addKey(KEYSYM, "key.keyboard.keypad.5", 325);
/* 288 */     addKey(KEYSYM, "key.keyboard.keypad.6", 326);
/* 289 */     addKey(KEYSYM, "key.keyboard.keypad.7", 327);
/* 290 */     addKey(KEYSYM, "key.keyboard.keypad.8", 328);
/* 291 */     addKey(KEYSYM, "key.keyboard.keypad.9", 329);
/* 292 */     addKey(KEYSYM, "key.keyboard.keypad.add", 334);
/* 293 */     addKey(KEYSYM, "key.keyboard.keypad.decimal", 330);
/* 294 */     addKey(KEYSYM, "key.keyboard.keypad.enter", 335);
/* 295 */     addKey(KEYSYM, "key.keyboard.keypad.equal", 336);
/* 296 */     addKey(KEYSYM, "key.keyboard.keypad.multiply", 332);
/* 297 */     addKey(KEYSYM, "key.keyboard.keypad.divide", 331);
/* 298 */     addKey(KEYSYM, "key.keyboard.keypad.subtract", 333);
/* 300 */     addKey(KEYSYM, "key.keyboard.down", 264);
/* 301 */     addKey(KEYSYM, "key.keyboard.left", 263);
/* 302 */     addKey(KEYSYM, "key.keyboard.right", 262);
/* 303 */     addKey(KEYSYM, "key.keyboard.up", 265);
/* 305 */     addKey(KEYSYM, "key.keyboard.apostrophe", 39);
/* 306 */     addKey(KEYSYM, "key.keyboard.backslash", 92);
/* 307 */     addKey(KEYSYM, "key.keyboard.comma", 44);
/* 308 */     addKey(KEYSYM, "key.keyboard.equal", 61);
/* 309 */     addKey(KEYSYM, "key.keyboard.grave.accent", 96);
/* 310 */     addKey(KEYSYM, "key.keyboard.left.bracket", 91);
/* 311 */     addKey(KEYSYM, "key.keyboard.minus", 45);
/* 312 */     addKey(KEYSYM, "key.keyboard.period", 46);
/* 313 */     addKey(KEYSYM, "key.keyboard.right.bracket", 93);
/* 314 */     addKey(KEYSYM, "key.keyboard.semicolon", 59);
/* 315 */     addKey(KEYSYM, "key.keyboard.slash", 47);
/* 316 */     addKey(KEYSYM, "key.keyboard.space", 32);
/* 317 */     addKey(KEYSYM, "key.keyboard.tab", 258);
/* 319 */     addKey(KEYSYM, "key.keyboard.left.alt", 342);
/* 320 */     addKey(KEYSYM, "key.keyboard.left.control", 341);
/* 321 */     addKey(KEYSYM, "key.keyboard.left.shift", 340);
/* 322 */     addKey(KEYSYM, "key.keyboard.left.win", 343);
/* 323 */     addKey(KEYSYM, "key.keyboard.right.alt", 346);
/* 324 */     addKey(KEYSYM, "key.keyboard.right.control", 345);
/* 325 */     addKey(KEYSYM, "key.keyboard.right.shift", 344);
/* 326 */     addKey(KEYSYM, "key.keyboard.right.win", 347);
/* 328 */     addKey(KEYSYM, "key.keyboard.enter", 257);
/* 329 */     addKey(KEYSYM, "key.keyboard.escape", 256);
/* 331 */     addKey(KEYSYM, "key.keyboard.backspace", 259);
/* 332 */     addKey(KEYSYM, "key.keyboard.delete", 261);
/* 333 */     addKey(KEYSYM, "key.keyboard.end", 269);
/* 334 */     addKey(KEYSYM, "key.keyboard.home", 268);
/* 335 */     addKey(KEYSYM, "key.keyboard.insert", 260);
/* 336 */     addKey(KEYSYM, "key.keyboard.page.down", 267);
/* 337 */     addKey(KEYSYM, "key.keyboard.page.up", 266);
/* 339 */     addKey(KEYSYM, "key.keyboard.caps.lock", 280);
/* 340 */     addKey(KEYSYM, "key.keyboard.pause", 284);
/* 341 */     addKey(KEYSYM, "key.keyboard.scroll.lock", 281);
/* 343 */     addKey(KEYSYM, "key.keyboard.menu", 348);
/* 344 */     addKey(KEYSYM, "key.keyboard.print.screen", 283);
/* 345 */     addKey(KEYSYM, "key.keyboard.world.1", 161);
/* 346 */     addKey(KEYSYM, "key.keyboard.world.2", 162);
/*     */   }
/*     */   
/* 349 */   private final Int2ObjectMap<InputConstants.Key> map = (Int2ObjectMap<InputConstants.Key>)new Int2ObjectOpenHashMap();
/*     */   
/*     */   private static final String KEY_KEYBOARD_UNKNOWN = "key.keyboard.unknown";
/*     */   
/*     */   Type(String defaultPrefix, BiFunction<Integer, String, Component> displayTextSupplier) {
/* 356 */     this.defaultPrefix = defaultPrefix;
/* 357 */     this.displayTextSupplier = displayTextSupplier;
/*     */   }
/*     */   
/*     */   public InputConstants.Key getOrCreate(int value) {
/* 361 */     return (InputConstants.Key)this.map.computeIfAbsent(value, intValue -> {
/*     */           int humanReadableValue = intValue;
/*     */           if (this == MOUSE)
/*     */             humanReadableValue++; 
/*     */           String name = this.defaultPrefix + "." + this.defaultPrefix;
/*     */           return new InputConstants.Key(name, this, intValue);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\InputConstants$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */
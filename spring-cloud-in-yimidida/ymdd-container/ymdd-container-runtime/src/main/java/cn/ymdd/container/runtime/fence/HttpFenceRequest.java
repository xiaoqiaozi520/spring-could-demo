/*     */ package cn.ymdd.container.runtime.fence;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import javax.servlet.ReadListener;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ class HttpFenceRequest extends HttpServletRequestWrapper
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(HttpFenceRequest.class);
/*     */   private HttpFenceRequestBody bodies;
/*  28 */   boolean postedParameters = false;
/*     */   private BufferedReader buff;
/*     */ 
/*     */   public HttpFenceRequest(HttpServletRequest request)
/*     */   {
/*  32 */     super(request);
/*  33 */     if (HttpFenceTamper.isUrlEncoded(request)) {
/*  34 */       this.postedParameters = true;
/*     */     } else {
/*  36 */       this.bodies = new HttpFenceRequestBody(request);
/*  37 */       this.buff = new BufferedReader(new InputStreamReader(this.bodies));
/*  38 */       request.setAttribute("FENCE_REQ_ATTRS", readBuffer());
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServletInputStream getInputStream() throws IOException
/*     */   {
/*  44 */     if (this.postedParameters) {
/*  45 */       return super.getInputStream();
/*     */     }
/*  47 */     return this.bodies;
/*     */   }
/*     */ 
/*     */   public BufferedReader getReader()
/*     */     throws IOException
/*     */   {
/*  53 */     if (this.postedParameters) {
/*  54 */       return super.getReader();
/*     */     }
/*  56 */     return this.buff;
/*     */   }
/*     */ 
/*     */   final byte[] readBuffer()
/*     */   {
/*  61 */     if (this.bodies == null) {
/*  62 */       return null;
/*     */     }
/*  64 */     return this.bodies.readBuffer();
/*     */   }
/*     */ 
/*     */   final class HttpFenceRequestBody extends ServletInputStream {
/*     */     InputStream is;
/*     */     byte[] bodies;
/*     */ 
/*     */     HttpFenceRequestBody(HttpServletRequest request) {
/*  74 */       consumeMutilBodies(request);
/*     */     }
/*     */ 
/*     */     public int read() throws IOException
/*     */     {
/*  79 */       return this.is.read();
/*     */     }
/*     */ 
/*     */     byte[] consumeBufferStream(ServletInputStream sis) throws IOException {
/*  83 */       int len = 1024;
/*  84 */       byte[] temp = new byte[len];
/*  85 */       int c = -1;
/*  86 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  87 */       while ((c = sis.read(temp, 0, len)) != -1) {
/*  88 */         baos.write(temp, 0, c);
/*     */       }
/*  90 */       return baos.toByteArray();
/*     */     }
/*     */ 
/*     */     void consumeMutilBodies(HttpServletRequest request) {
/*  94 */       ServletInputStream originalSIS = null;
/*     */       try {
/*  96 */         originalSIS = request.getInputStream();
/*  97 */         this.bodies = consumeBufferStream(originalSIS);
/*  98 */         this.is = new ByteArrayInputStream(this.bodies);
/*     */       } catch (IOException e) {
/* 100 */         if (HttpFenceRequest.log.isErrorEnabled())
/* 101 */           HttpFenceRequest.log.error(e.getMessage(), e);
/*     */       }
/*     */       finally {
/* 104 */         consumeAfterCloseStream(originalSIS);
/*     */       }
/*     */     }
/*     */ 
/*     */     void consumeAfterCloseStream(ServletInputStream is) {
/* 109 */       if (is != null)
/*     */         try {
/* 111 */           is.close();
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/*     */     }
/*     */ 
/*     */     byte[] readBuffer() {
/* 118 */       return this.bodies;
/*     */     }
/*     */ 
/*     */     public boolean isFinished()
/*     */     {
/* 123 */       throw new RuntimeException("Not support");
/*     */     }
/*     */ 
/*     */     public boolean isReady()
/*     */     {
/* 128 */       throw new RuntimeException("Not support");
/*     */     }
/*     */ 
/*     */     public void setReadListener(ReadListener listener)
/*     */     {
/* 133 */       throw new RuntimeException("Not support");
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.fence.HttpFenceRequest
 * JD-Core Version:    0.6.0
 */
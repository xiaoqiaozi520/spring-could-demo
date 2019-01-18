/*     */ package cn.ymdd.container.runtime.fence;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.WriteListener;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ 
/*     */ class HttpFenceResponse extends HttpServletResponseWrapper
/*     */ {
/*     */   HttpFenceResponseBody bodies;
/*     */   PrintWriter writer;
/*     */ 
/*     */   public HttpFenceResponse(HttpServletResponse response)
/*     */   {
/*  23 */     super(response);
/*     */   }
/*     */ 
/*     */   public ServletOutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/*  31 */     if (this.bodies == null) {
/*  32 */       this.bodies = new HttpFenceResponseBody(getResponse());
/*     */     }
/*  34 */     return this.bodies;
/*     */   }
/*     */ 
/*     */   public PrintWriter getWriter() throws IOException
/*     */   {
/*  39 */     if (this.writer == null) {
/*  40 */       this.writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), getResponse().getCharacterEncoding()), true);
/*     */     }
/*  42 */     return this.writer;
/*     */   }
/*     */ 
/*     */   public void flushBuffer()
/*     */   {
/*  47 */     if (this.writer != null)
/*  48 */       this.writer.flush();
/*     */   }
/*     */ 
/*     */   final byte[] readBuffer()
/*     */   {
/*  53 */     if (this.bodies != null) {
/*  54 */       return this.bodies.read();
/*     */     }
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */   final void finish() throws IOException
/*     */   {
/*  61 */     if (this.writer != null) {
/*  62 */       this.writer.close();
/*     */     }
/*  64 */     if (this.bodies != null)
/*  65 */       this.bodies.close();
/*     */   }
/*     */ 
/*     */   final class HttpFenceResponseBody extends ServletOutputStream {
/*     */     final ByteArrayOutputStream bos;
/*     */     final ServletOutputStream sos;
/*     */ 
/*     */     HttpFenceResponseBody(ServletResponse httpServletResponse) throws IOException {
/*  75 */       this.sos = httpServletResponse.getOutputStream();
/*  76 */       this.bos = new ByteArrayOutputStream();
/*     */     }
/*     */ 
/*     */     byte[] read() {
/*  80 */       return this.bos.toByteArray();
/*     */     }
/*     */ 
/*     */     public void write(int val) throws IOException
/*     */     {
/*  85 */       if (this.sos != null) {
/*  86 */         this.sos.write(val);
/*  87 */         this.bos.write(val);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void write(byte[] byteArray) throws IOException
/*     */     {
/*  93 */       if (this.sos == null) {
/*  94 */         return;
/*     */       }
/*  96 */       write(byteArray, 0, byteArray.length);
/*     */     }
/*     */ 
/*     */     public void write(byte[] byteArray, int offset, int length) throws IOException
/*     */     {
/* 101 */       if (this.sos == null) {
/* 102 */         return;
/*     */       }
/* 104 */       this.sos.write(byteArray, offset, length);
/* 105 */       this.bos.write(byteArray, offset, length);
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void flush() throws IOException
/*     */     {
/* 114 */       if (this.sos == null) {
/* 115 */         return;
/*     */       }
/* 117 */       this.sos.flush();
/* 118 */       this.bos.flush();
/*     */     }
/*     */ 
/*     */     public boolean isReady()
/*     */     {
/* 123 */       throw new RuntimeException("Not support");
/*     */     }
/*     */ 
/*     */     public void setWriteListener(WriteListener listener)
/*     */     {
/* 128 */       throw new RuntimeException("Not support");
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.fence.HttpFenceResponse
 * JD-Core Version:    0.6.0
 */
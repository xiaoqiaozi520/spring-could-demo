/*     */
package cn.ymdd.container.runtime.fence;
/*     */
/*     */

import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import javax.servlet.ReadListener;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;

/*     */
/*     */ public final class HttpFenceStream extends ServletInputStream
        /*     */ {
    /*  21 */   private static final Logger log = LoggerFactory.getLogger(HttpFenceStream.class);
    /*  22 */   private final ByteArrayOutputStream cache = new ByteArrayOutputStream();
    /*     */   private final ServletInputStream data;
    /*  24 */   private Boolean enable = Boolean.valueOf(false);

    /*     */
    /*     */
    private HttpFenceStream(ServletInputStream data) {
        /*  27 */
        this.data = data;
        /*     */
    }

    /*     */
    /*     */
    public static final ServletInputStream createHttpFenceStream(HttpServletRequest request) {
        /*     */
        try {
            /*  32 */
            return new HttpFenceStream(request.getInputStream());
        } catch (Exception e) {
            /*     */
            throw new IllegalStateException(e);
        }
        /*  34 */

        /*     */
    }

    /*     */
    /*     */
    public boolean isFinished()
    /*     */ {
        /*  40 */
        return this.data.isFinished();
        /*     */
    }

    /*     */
    /*     */
    public boolean isReady()
    /*     */ {
        /*  45 */
        return this.data.isReady();
        /*     */
    }

    /*     */
    /*     */
    public void setReadListener(ReadListener readListener)
    /*     */ {
        /*  50 */
        if ((readListener instanceof StreamListener))
            /*  51 */ this.data.setReadListener(readListener);
            /*     */
        else
            /*  53 */       throw new IllegalStateException("Not support");
        /*     */
    }

    /*     */
    /*     */
    public int read() throws IOException
    /*     */ {
        /*     */
        try
            /*     */ {
            /*  60 */
            int bit = this.data.read();
            /*  61 */
            this.cache.write(bit);
            /*  62 */
            int i = bit;
            /*     */
            return i;
            /*     */
        } finally {
            /*  64 */
            readLock();
        }
        /*     */
    }

    /*     */
    /*     */
    public int read(byte[] b) throws IOException
    /*     */ {
        /*     */
        try
            /*     */ {
            /*  71 */
            int bit = super.read(b);
            /*  72 */
            this.cache.write(b, 0, bit);
            /*  73 */
            int i = bit;
            /*     */
            return i;
            /*     */
        } finally {
            /*  75 */
            readLock();
        }

        /*     */
    }

    /*     */
    /*     */
    public int read(byte[] b, int off, int len) throws IOException
    /*     */ {
        /*     */
        try
            /*     */ {
            /*  82 */
            int bit = super.read(b, off, len);
            /*  83 */
            this.cache.write(b, 0, bit);
            /*  84 */
            int i = bit;
            /*     */
            return i;
            /*     */
        } finally {
            /*  86 */
            readLock();
        }

        /*     */
    }

    /*     */
    /*     */
    public int readLine(byte[] b, int off, int len) throws IOException
    /*     */ {
        /*     */
        try
            /*     */ {
            /*  93 */
            int bit = super.readLine(b, off, len);
            /*  94 */
            this.cache.write(b, 0, bit);
            /*  95 */
            int i = bit;
            /*     */
            return i;
            /*     */
        } finally {
            /*  97 */
            readLock();
        }

        /*     */
    }

    /*     */
    /*     */
    public void readLock()
    /*     */ {
        /* 102 */
        if (!this.enable.booleanValue())
            /* 103 */ synchronized (this.data) {
            /* 104 */
            this.enable = Boolean.valueOf(true);
            /*     */
        }
        /*     */
    }

    /*     */
    /*     */
    public byte[] readData()
    /*     */ {
        /* 110 */
        if (this.enable.booleanValue()) {
            /* 111 */
            return this.cache.toByteArray();
            /*     */
        }
        /* 113 */
        throw new IllegalStateException("Invalid data");
        /*     */
    }

    /*     */
    /*     */
    public void close() throws IOException
    /*     */ {
        /*     */
        try
            /*     */ {
            /* 120 */
            super.close();
            /* 121 */
            this.cache.close();
            /*     */
            /* 123 */
            if (this.enable.booleanValue())
                /* 124 */ synchronized (this.data) {
                /* 125 */
                this.enable = Boolean.valueOf(false);
                /*     */
            }
            /*     */
        }
        /*     */ finally
            /*     */ {
            /* 123 */
            if (this.enable.booleanValue())
                /* 124 */ synchronized (this.data) {
                /* 125 */
                this.enable = Boolean.valueOf(false);
                /*     */
            }
            /*     */
        }
        /*     */
    }

    /*     */
    /*     */   public abstract class StreamListener implements ReadListener {
        /*     */
        public StreamListener() {
            /*     */
        }

        /*     */
        /*     */
        public void onError(Throwable t) {
            /* 135 */
            if (HttpFenceStream.log.isErrorEnabled())
                /* 136 */ HttpFenceStream.log.error("[ GULUDAI ] Request error '" + t.getMessage() + "'", t);
            /*     */
        }

        /*     */
        /*     */
        public void onDataAvailable()
        /*     */       throws IOException
        /*     */ {
            /* 142 */
            HttpFenceStream.this.readLock();
            /*     */
        }
        /*     */
    }
    /*     */
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.fence.HttpFenceStream
 * JD-Core Version:    0.6.0
 */
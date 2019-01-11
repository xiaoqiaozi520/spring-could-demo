/*    */ package cn.guludai.container.web.main;
/*    */ 
/*    */ import org.apache.catalina.connector.Connector;
/*    */ import org.apache.coyote.ProtocolHandler;
/*    */ import org.apache.coyote.http11.Http11NioProtocol;
/*    */ import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
/*    */ import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
/*    */ import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
/*    */ import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
/*    */ 
/*    */ public abstract class WebContainer
/*    */   implements EmbeddedServletContainerCustomizer
/*    */ {
/*    */   public final void customize(ConfigurableEmbeddedServletContainer container)
/*    */   {
/* 21 */     if ((container instanceof TomcatEmbeddedServletContainerFactory)) {
/* 22 */       TomcatEmbeddedServletContainerFactory factory = (TomcatEmbeddedServletContainerFactory)container;
/* 23 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*    */       {
/*    */         public void customize(Connector connector)
/*    */         {
/* 27 */           ProtocolHandler protocol = connector.getProtocolHandler();
/* 28 */           if ((protocol instanceof Http11NioProtocol)) {
/* 29 */             Http11NioProtocol http = (Http11NioProtocol)protocol;
/* 30 */             http.setProperty("acceptCount", String.valueOf(100));
/* 31 */             http.setMaxKeepAliveRequests(100);
/* 32 */             http.setConnectionTimeout(60000);
/* 33 */             http.setKeepAliveTimeout(60000);
/*    */           }
/*    */         }
/*    */       }
/*    */        });
/* 37 */       return;
/*    */     }
/* 39 */     throw new IllegalArgumentException("Not support container");
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-web\0.0.1-SNAPSHOT\guludai-container-web-0.0.1-20180428.084950-107.jar
 * Qualified Name:     cn.guludai.container.web.main.WebContainer
 * JD-Core Version:    0.6.0
 */